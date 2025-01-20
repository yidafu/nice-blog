package dev.yidafu.blog.admin.handler

import de.comahe.i18n4k.messages.MessageBundleLocalizedString
import dev.whyoleg.cryptography.CryptographyProvider
import dev.whyoleg.cryptography.DelicateCryptographyApi
import dev.whyoleg.cryptography.algorithms.MD5
import dev.whyoleg.cryptography.algorithms.RSA
import dev.whyoleg.cryptography.algorithms.SHA256
import dev.yidafu.blog.admin.views.pages.AdminLoginPage
import dev.yidafu.blog.common.*
import dev.yidafu.blog.common.ConstantKeys
import dev.yidafu.blog.common.ConstantKeys.AUTH_CURRENT_USERNAME
import dev.yidafu.blog.common.ConstantKeys.AUTH_RSA_PRIVATE_KEY
import dev.yidafu.blog.common.ConstantKeys.AUTH_RSA_PUBLIC_KEY
import dev.yidafu.blog.common.ext.html
import dev.yidafu.blog.common.services.ConfigurationService
import dev.yidafu.blog.common.services.UserService
import dev.yidafu.blog.common.vo.AdminLoginVO
import dev.yidafu.blog.i18n.AdminTxt
import io.vertx.ext.web.RoutingContext
import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory
import java.util.*
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@Single
class AuthHandler(
  private val configService: ConfigurationService,
  private val userService: UserService,
) {
  private val MAX_RETRY_COUNT = "5"
  private val log = LoggerFactory.getLogger(AuthHandler::class.java)

  private val provider = CryptographyProvider.Default

  private val keyPair =
    SuspendingLazy {
      log.info("generate new RSA key pair")

      val oaep = provider.get(RSA.OAEP)
      val keyPair = oaep.keyPairGenerator().generateKey()
      val publicKey = keyPair.publicKey.encodeToByteArray(RSA.PublicKey.Format.PEM).toString(Charsets.UTF_8)
      val privateKey = keyPair.privateKey.encodeToByteArray(RSA.PrivateKey.Format.PEM).toString(Charsets.UTF_8)

      publicKey to privateKey
    }

  @OptIn(DelicateCryptographyApi::class)
  private val md5 = provider.get(MD5)

  private val cache = TimedCache(log)

  class LoginError(
    val code: Int,
    val message: MessageBundleLocalizedString,
  )

  private val errorList =
    listOf(
      /**
       * 1. username is incorrect
       * 2. password is incorrect
       * 3. user not exist in user table
       */
      LoginError(1, AdminTxt.username_or_password_error),
      LoginError(2, AdminTxt.required_username_or_password),
      LoginError(3, AdminTxt.account_locked),
    )

  suspend fun genRsaKeyPair(ctx: RoutingContext) {
    val session = ctx.session()
    val (publicKey, privateKey) = keyPair.value()
    session.put(AUTH_RSA_PUBLIC_KEY, publicKey)
    session.put(AUTH_RSA_PRIVATE_KEY, privateKey)
    ctx.next()
  }

  suspend fun loginPage(ctx: RoutingContext) {
    log.info("render login page")

    val local = ctx.get<Locale>(ConstantKeys.LANGUAGE_CONTEXT)
    val publicKEy = ctx.session().get<String>(AUTH_RSA_PUBLIC_KEY)
    val errorCode = ctx.queryParam("errorCode").firstOrNull()
    val errorMessage =
      errorCode?.toInt().let { code ->
        errorList.find { e -> e.code == code }
      }?.message?.toString(local)

    val vo =
      AdminLoginVO(
        publicKEy.toString(),
        errorMessage,
      )
    log.info("render login page")
    ctx.html(AdminLoginPage::class, vo)
  }

  @OptIn(ExperimentalEncodingApi::class)
  suspend fun loginAction(ctx: RoutingContext) {
    val req = ctx.request()
    val body = req.formAttributes()
    val userName = body.get(FormKeys.USER_NAME)
    // get private key from session
    val session = ctx.session()
    val privateKey = session.get<String>(AUTH_RSA_PRIVATE_KEY)
    // clear old rsa key
    session.remove<String>(AUTH_RSA_PRIVATE_KEY)
    session.remove<String>(AUTH_RSA_PUBLIC_KEY)

    // check account is locked?
    cache.get(userName)?.let { count ->
      log.info("login retry count $count")
      if (count == MAX_RETRY_COUNT) {
        redirectLoginErrorPage(ctx, errorList[2])
        return
      }
    }
    // check username exist in database
    val userModal = userService.getUserByUsername(userName)
    if (userModal == null) {
      log.info("user {} not found", userName)
      redirectLoginErrorPage(ctx, errorList[0])
      return
    }

    // verify password
    val password = body.get(FormKeys.PASSWORD)
    val passwordText =
      CryptographyProvider.Default.get(RSA.OAEP)
        .privateKeyDecoder(SHA256)
        .decodeFromByteArray(
          RSA.PrivateKey.Format.PEM,
          privateKey.toByteArray(),
        )
        .decryptor()
        .decrypt(Base64.decode(password))
    val encodePasswordText = Base64.encode(md5.hasher().hash(passwordText))
    log.info("encodePasswordText $encodePasswordText, password = ${userModal.password}")
    if (userModal.password != encodePasswordText) {
      log.info("user {} login fail", userName)
      return redirectLoginErrorPage(ctx, errorList[1])
    }

    log.info("login user {}, password {}", userName, passwordText)
    session.put(AUTH_CURRENT_USERNAME, userModal.username)
    ctx.redirect(Routes.ADMIN_URL)
  }

  fun logoutAction(ctx: RoutingContext) {
    ctx.session().destroy()
    ctx.redirect(Routes.LOGIN_URL)
  }

  private fun redirectLoginErrorPage(
    ctx: RoutingContext,
    err: LoginError,
  ) {
    ctx.redirect(Routes.LOGIN_URL + "?errorCode=${err.code}")
  }

  fun checkLoginAction(ctx: RoutingContext) {
    val username = ctx.session().get<String>(AUTH_CURRENT_USERNAME)
    if (username == null) {
      ctx.session().destroy()
      ctx.redirect(Routes.LOGIN_URL)
    } else {
      ctx.next()
    }
  }
}
