package dev.yidafu.blog.dev.yidafu.blog.admin.service

import dev.yidafu.blog.admin.services.BackupService
import dev.yidafu.blog.common.BlogConfig
import dev.yidafu.blog.common.services.ConfigurationService
import io.kotest.core.spec.style.StringSpec
import io.kotest.koin.KoinExtension
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jooq.CloseableDSLContext
import org.jooq.impl.DSL
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.io.path.exists
import kotlin.io.path.extension

val myKoinModule =
  module {
    single<CloseableDSLContext> {
      DSL.using(
        "jdbc:sqlite::resource:backup.db",
        "",
        "",
      )
    }
    single {
      ConfigurationService(get())
    }
    single {
      BackupService(get(), get())
    }
  }

/**
 * suspend 函数执行命令（协程友好）
 * @param command 命令数组
 * @param timeoutMs 超时时间
 */
suspend fun executeCommandSuspend(
  command: List<String>,
  timeoutMs: Long = 30_000,
): String =
  withContext(Dispatchers.IO) {
    suspendCoroutine { continuation ->
      val process =
        ProcessBuilder(command)
          .redirectErrorStream(true)
          .start()

      val output = StringBuilder()
      val readerThread =
        Thread {
          BufferedReader(InputStreamReader(process.inputStream)).use { reader ->
            var line: String?
            while (reader.readLine().also { line = it } != null) {
              output.appendLine(line)
            }
          }
        }.apply { start() }

      Thread {
        try {
          if (!process.waitFor(timeoutMs, TimeUnit.MILLISECONDS)) {
            process.destroyForcibly()
            continuation.resumeWithException(
              TimeoutException("Timeout after $timeoutMs ms"),
            )
          } else {
            readerThread.join() // 确保输出读取完成
            continuation.resumeWith(
              Result.success(output.toString().trim()),
            )
          }
        } catch (e: Exception) {
          continuation.resumeWithException(e)
        } finally {
          process.destroy()
        }
      }.apply { start() }
    }
  }

fun createTempResource(filename: String): Path {
  val outPath =
    Files.createTempFile(
      "test-resource",
      "." + Path.of(filename).extension,
    )

  BackupService::class.java.classLoader
    .getResourceAsStream(filename)
    ?.use { iStream ->
      Files.copy(iStream, outPath, StandardCopyOption.REPLACE_EXISTING)
    }

  return outPath!!
}

class BackupServiceTest : StringSpec(), KoinTest {
  override fun extensions() = listOf(KoinExtension(myKoinModule))

  private val backupService by inject<BackupService>()

  init {
    "should export ddl sql" {
      val sql = backupService.createBackupSql()
      sql shouldContain "create table if not exists B_ARTICLE"
      sql shouldContain "insert into B_USER"
    }

    "should export backup package" {
      val packageFile = backupService.createBackupPackage()
      val output = executeCommandSuspend(listOf("tar", "-tvf", packageFile.toString()))
      packageFile.toFile().exists() shouldBe true
      output shouldContain "backup.sql"
      output shouldContain "upload/"
    }

    "should restore backup tar file" {
      val tarFile = createTempResource("backup-test.tar")
      backupService.restoreForBackup(tarFile)
      Paths.get(BlogConfig.DEFAULT_UPLOAD_DIRECTORY, "react.png").exists().shouldBeTrue()
    }
  }
}
