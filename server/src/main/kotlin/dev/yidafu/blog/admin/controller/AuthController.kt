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
import dev.yidafu.blog.common.ext.render
import dev.yidafu.blog.common.services.ConfigurationService
import dev.yidafu.blog.common.services.UserService
import dev.yidafu.blog.i18n.AdminTxt
import dev.yidafu.blog.common.annotation.Controller
import dev.yidafu.blog.common.annotation.Get
import dev.yidafu.blog.common.annotation.Post
import dev.yidafu.blog.common.ext.AdminSession
import dev.yidafu.blog.common.ext.updateUsername
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

  suspend fun genRsaKeyPair(call: ApplicationCall): String {
    val (publicKey, privateKey) = keyPair.value()

    call.sessions.set(AdminSession(
      username = "",
      publicKey = publicKey,
      privateKey = privateKey
    ))

    return publicKey
  }

  @Get(Routes.LOGIN_URL)
  suspend fun loginPage(call: ApplicationCall) {
    val publicKeyForFrontend = genRsaKeyPair(call)

    val local =
      call.attributes.getOrNull<Locale>(
        AttributeKey<Locale>(ConstantKeys.LANGUAGE_CONTEXT),
      ) ?: Locale.getDefault()
    val errorCode = call.request.queryParameters["errorCode"]
    val errorMessage =
      errorCode?.toIntOrNull()?.let { code ->
        errorList.find { e -> e.code == code }
      }?.message?.toString(local)

    call.render(
      PageNames.ADMIN_LOGIN,
      mapOf(
        "publicKey" to publicKeyForFrontend,
        "errorMessage" to (errorMessage ?: ""),
      ),
    )
  }

  @Post(Routes.LOGIN_URL)
  @OptIn(ExperimentalEncodingApi::class)
  suspend fun loginAction(call: ApplicationCall) {
    val body = call.receiveParameters()
    val userName = requireNotNull(body[FormKeys.USER_NAME])

    val adminSession = call.sessions.get<AdminSession>()
    val privateKeyStr = requireNotNull(adminSession?.privateKey) {
      "Private key not found in session"
    }

    // 检查账户是否被锁定
    cache.get(userName)?.let { count ->
      if (count == MAX_RETRY_COUNT) {
        log.warn("Account locked: {}", userName)
        redirectLoginErrorPage(call, errorList[2])
        return
      }
    }

    // 检查用户是否存在
    val userModal = userService.getUserByUsername(userName)
    if (userModal == null) {
      log.warn("User not found: {}", userName)
      redirectLoginErrorPage(call, errorList[0])
      return
    }

    // 解密并验证密码
    val password = requireNotNull(body[FormKeys.PASSWORD])
    val passwordText =
      CryptographyProvider.Default.get(RSA.OAEP)
        .privateKeyDecoder(SHA256)
        .decodeFromByteArray(
          RSA.PrivateKey.Format.PEM,
          privateKeyStr.toByteArray(Charsets.UTF_8),
        )
        .decryptor()
        .decrypt(Base64.decode(password))

    val encodePasswordText = Base64.encode(md5.hasher().hash(passwordText))

    if (userModal.password != encodePasswordText) {
      log.warn("Login failed: {}", userName)
      return redirectLoginErrorPage(call, errorList[1])
    }

    log.info("User logged in: {}", userName)
    call.sessions.updateUsername(userModal.username)
    call.respondRedirect(Routes.ADMIN_URL)
  }

  @Get(Routes.LOGOUT_URL)
  suspend fun logoutAction(call: ApplicationCall) {
    call.sessions.clear<AdminSession>()
    call.respondRedirect(Routes.LOGIN_URL)
  }

  private suspend fun redirectLoginErrorPage(
    call: ApplicationCall,
    err: LoginError,
  ) {
    call.respondRedirect(Routes.LOGIN_URL + "?errorCode=${err.code}")
  }

  // checkLoginAction 已移至 AuthInterceptor.kt
}
