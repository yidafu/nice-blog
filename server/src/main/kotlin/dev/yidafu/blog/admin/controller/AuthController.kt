package dev.yidafu.blog.admin.controller

import de.comahe.i18n4k.messages.MessageBundleLocalizedString
import dev.whyoleg.cryptography.CryptographyProvider
import dev.whyoleg.cryptography.DelicateCryptographyApi
import dev.whyoleg.cryptography.algorithms.MD5
import dev.whyoleg.cryptography.algorithms.RSA
import dev.whyoleg.cryptography.algorithms.SHA256
import dev.yidafu.blog.common.*
import dev.yidafu.blog.common.ConstantKeys
import dev.yidafu.blog.common.ConstantKeys.AUTH_CURRENT_USERNAME
import dev.yidafu.blog.common.ConstantKeys.AUTH_RSA_PRIVATE_KEY
import dev.yidafu.blog.common.ConstantKeys.AUTH_RSA_PUBLIC_KEY
import dev.yidafu.blog.common.services.ConfigurationService
import dev.yidafu.blog.common.services.UserService
import dev.yidafu.blog.common.vo.AdminLoginVO
import dev.yidafu.blog.i18n.AdminTxt
import dev.yidafu.blog.common.annotation.Controller
import dev.yidafu.blog.common.annotation.Get
import dev.yidafu.blog.common.annotation.Post
import dev.yidafu.blog.themes.PageNames
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import io.ktor.util.AttributeKey
import org.slf4j.LoggerFactory
import java.util.*
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@Controller
class AuthController(
  private val configService: ConfigurationService,
  private val userService: UserService,
) {
  companion object {
    private const val MAX_RETRY_COUNT = "5"
  }

  private val log = LoggerFactory.getLogger(AuthController::class.java)

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

  /**
   * 1. username is incorrect
   * 2. password is incorrect
   * 3. user not exist in user table
   */
  private val errorList =
    listOf(
      LoginError(1, AdminTxt.username_or_password_error),
      LoginError(2, AdminTxt.required_username_or_password),
      LoginError(3, AdminTxt.account_locked),
    )

  suspend fun genRsaKeyPair(call: ApplicationCall) {
    val (publicKey, privateKey) = keyPair.value()
    call.sessions.set(AUTH_RSA_PUBLIC_KEY, publicKey)
    call.sessions.set(AUTH_RSA_PRIVATE_KEY, privateKey)
    // Ktor中不需要显式调用next()，拦截器会自动继续
  }

  @Get(Routes.LOGIN_URL)
  suspend fun loginPage(call: ApplicationCall) {
    log.info("render login page")
    val local =
      call.attributes.getOrNull<Locale>(
        AttributeKey<Locale>(ConstantKeys.LANGUAGE_CONTEXT),
      ) ?: Locale.getDefault()
    val publicKEy = call.sessions.get(AUTH_RSA_PUBLIC_KEY) ?: ""
    val errorCode = call.request.queryParameters["errorCode"]
    val errorMessage =
      errorCode?.toIntOrNull()?.let { code ->
        errorList.find { e -> e.code == code }
      }?.message?.toString(local)

    val vo =
      AdminLoginVO(
        publicKEy.toString(),
        errorMessage,
      )
    log.info("render login page")
    call.respondText("Rendering page: ${PageNames.ADMIN_LOGIN} with data: $vo")
  }

  @Post(Routes.LOGIN_URL)
  @OptIn(ExperimentalEncodingApi::class)
  suspend fun loginAction(call: ApplicationCall) {
    val body = call.receiveParameters()
    val userName = requireNotNull(body[FormKeys.USER_NAME])
    // get private key from session
    val privateKey = call.sessions.get(AUTH_RSA_PRIVATE_KEY) as String? ?: return redirectLoginErrorPage(call, errorList[0])
    // clear old rsa key
    call.sessions.clear(AUTH_RSA_PRIVATE_KEY)
    call.sessions.clear(AUTH_RSA_PUBLIC_KEY)

    // check account is locked?
    cache.get(userName)?.let { count ->
      log.info("login retry count $count")
      if (count == MAX_RETRY_COUNT) {
        redirectLoginErrorPage(call, errorList[2])
        return
      }
    }
    // check username exist in database
    val userModal = userService.getUserByUsername(userName)
    if (userModal == null) {
      log.info("user {} not found", userName)
      redirectLoginErrorPage(call, errorList[0])
      return
    }

    // verify password
    val password = requireNotNull(body[FormKeys.PASSWORD])
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
      return redirectLoginErrorPage(call, errorList[1])
    }

    log.info("login user {}, password {}", userName, passwordText)
    call.sessions.set(AUTH_CURRENT_USERNAME, userModal.username)
    call.respondRedirect(Routes.ADMIN_URL)
  }

  @Get(Routes.LOGOUT_URL)
  suspend fun logoutAction(call: ApplicationCall) {
    call.sessions.clear(AUTH_CURRENT_USERNAME)
    call.respondRedirect(Routes.LOGIN_URL)
  }

  private suspend fun redirectLoginErrorPage(
    call: ApplicationCall,
    err: LoginError,
  ) {
    call.respondRedirect(Routes.LOGIN_URL + "?errorCode=${err.code}")
  }

  @Get(Routes.ADMIN_URL + "/*")
  suspend fun checkLoginAction(call: ApplicationCall) {
    val username = call.sessions.get(AUTH_CURRENT_USERNAME)
    if (username == null) {
      call.sessions.clear(AUTH_CURRENT_USERNAME)
      call.respondRedirect(Routes.LOGIN_URL)
    } else {
      // Ktor中不需要显式调用next()，拦截器会自动继续
    }
  }
}
