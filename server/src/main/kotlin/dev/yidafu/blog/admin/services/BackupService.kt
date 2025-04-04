package dev.yidafu.blog.admin.services

import dev.yidafu.blog.common.BlogConfig
import dev.yidafu.blog.common.ConfigurationKeys
import dev.yidafu.blog.common.FileUtils
import dev.yidafu.blog.common.dao.DefaultSchema
import dev.yidafu.blog.common.ext.formatString
import dev.yidafu.blog.common.services.BaseService
import dev.yidafu.blog.common.services.ConfigurationService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.commons.compress.archivers.tar.TarArchiveEntry
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream
import org.jooq.CloseableDSLContext
import org.jooq.DDLExportConfiguration
import org.jooq.DDLFlag
import org.jooq.conf.ParamType
import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory
import org.sqlite.FileException
import java.io.*
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Single
class BackupService(
  context: CloseableDSLContext,
  private val configService: ConfigurationService,
) : BaseService(context) {
  val log = LoggerFactory.getLogger(BackupService::class.java)

  suspend fun createBackupPackage(): Path {
    val backupDir = configService.getByKey(ConfigurationKeys.BACKUP_ROOT_DIR).configValue
    val backupDirName =
      TEMP_BACKUP_DIR_PREFIX +
        DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss").format(LocalDateTime.now())
    val tarFilePath = Path.of(backupDir, "$backupDirName.tar")

    val sqlContent = createBackupSql()

    return withContext(Dispatchers.IO) {
      // create temp backup directory. write backup.sql and move upload to temp directory
      val tempBackupDir = createBackupDirectory(sqlContent)

      log.info("create temp tar file $tarFilePath")
      // create tar file
      createTar(tempBackupDir, tarFilePath)

      log.info("cleanup temp directory $tempBackupDir")
      cleanupDirectory(tempBackupDir)

      return@withContext tarFilePath
    }
  }

  suspend fun restoreForBackup(backupTarPath: Path) {
    val restoreDir = Files.createTempDirectory(TEMP_RESTORE_DIR_PREFIX)

    extractTar(backupTarPath, restoreDir)

    log.info("restart backup sql")
    val backDir = restoreDir.resolve("backup")
    val backupSqlFile = backDir.resolve(BACKUP_SQL_FILE).toFile()

    val sqlContent = backupSqlFile.readText()
    // 插入数据
    runDB {
      it.execute(sqlContent)
    }

    log.info("restore uploaded file")
    // copy temp upload directory to upload file
    FileUtils.copy(
      backDir.resolve(UPLOAD_DIR_NAME),
      Path.of(BlogConfig.DEFAULT_UPLOAD_DIRECTORY),
    )

    cleanupDirectory(restoreDir)
  }

  internal suspend fun createBackupSql() =
    runDB { context ->
      val sqlResult =
        mutableListOf(
          """
          -- Generate backup sql at ${LocalDateTime.now().formatString()}

          """.trimIndent(),
        )

      // step1: export DDL
      val dbConfig =
        DDLExportConfiguration()
          .flags(DDLFlag.TABLE, DDLFlag.PRIMARY_KEY, DDLFlag.UNIQUE, DDLFlag.INDEX, DDLFlag.COMMENT)
          .createTableIfNotExists(true)
          .createSchemaIfNotExists(true)
          .createSequenceIfNotExists(true)
          .createIndexIfNotExists(true)

      sqlResult.add("-- generate ddl begin")
      context.ddl(DefaultSchema.DEFAULT_SCHEMA, dbConfig).forEach {
        sqlResult.add(it.sql + ";")
      }
      sqlResult.add("-- generate ddl end")

      // step2: export data to inert sql
      for (table in DefaultSchema.DEFAULT_SCHEMA.tableStream()) {
        val count = context.fetchCount(table)
        if (count > 0) {
          val queryCount = count / QUERY_BATCH
          (0..queryCount).forEach { idx ->
            sqlResult.add("")
            sqlResult.add("-------------------------------- Table(${table.name}) Data --------------------------------")
            val records = context.selectFrom(table).limit(idx * QUERY_BATCH, QUERY_BATCH).fetchArray()
            records.forEach { r ->
              val insertSql =
                context.insertInto(table, *table.fields())
                  .values(r.fields().map { f -> r.getValue(f) }).getSQL(ParamType.INLINED)
              sqlResult.add(insertSql)
            }
            sqlResult.add("-------------------------------- Table(${table.name}) Data --------------------------------")
          }
        }
      }

      sqlResult.joinToString("\n")
    }

  private suspend fun createBackupDirectory(sqlContent: String): Path =
    withContext(Dispatchers.IO) {
      val tempBackupDir = Files.createTempDirectory("backup-")

      log.info("start backup task at $tempBackupDir")

      val tempSqlFile = tempBackupDir.resolve(BACKUP_SQL_FILE).toFile()
      tempSqlFile.writeText(sqlContent)
      log.info("write backup sql to ${tempSqlFile.absolutePath}")

      val tempUploadDirPath = tempBackupDir.resolve(UPLOAD_DIR_NAME)

      log.info("copy upload/ directory to backup directory")
      FileUtils.copy(
        Path.of(BlogConfig.DEFAULT_UPLOAD_DIRECTORY),
        tempUploadDirPath,
      )

      tempBackupDir
    }

  private fun createTar(
    sourcePath: Path,
    targetPath: Path,
  ) {
    Files.newOutputStream(targetPath).use { outStream ->
      TarArchiveOutputStream(BufferedOutputStream(outStream)).use { outTar ->

        outTar.setLongFileMode(TarArchiveOutputStream.LONGFILE_POSIX)

        fun addEntry(
          sPath: Path,
          base: String = "",
        ) {
          if (Files.notExists(sPath)) {
            throw FileNotFoundException("file not exists: $sPath")
          }
          val entryName = base + sPath.fileName.toString()
          val entry = TarArchiveEntry(sPath.toFile(), entryName)
          outTar.putArchiveEntry(entry)
          if (Files.isRegularFile(sPath)) {
            // 必须设置大小（否则内容为空）
            entry.size = Files.size(sPath)
            Files.copy(sPath, outTar)
            outTar.closeArchiveEntry()
          } else if (Files.isDirectory(sPath)) {
            outTar.closeArchiveEntry()
            Files.newDirectoryStream(sPath).forEach { dPath ->
              addEntry(dPath, "$entryName/")
            }
          }
        }

        val rootEntry = TarArchiveEntry(sourcePath.toFile(), UPLOAD_DIR_NAME)
        outTar.putArchiveEntry(rootEntry)
        outTar.closeArchiveEntry()
        Files.newDirectoryStream(sourcePath).forEach { f ->
          addEntry(f, "$UPLOAD_DIR_NAME/")
        }
      }
    }
  }

  private fun cleanupDirectory(path: Path) {
    Files.walkFileTree(
      path,
      object : SimpleFileVisitor<Path>(), FileVisitor<Path> {
        override fun visitFile(
          file: Path?,
          attrs: BasicFileAttributes,
        ): FileVisitResult {
          if (file != null) {
            Files.delete(file)
          }
          return FileVisitResult.CONTINUE
        }

        override fun postVisitDirectory(
          dir: Path?,
          exc: IOException?,
        ): FileVisitResult {
          dir?.let { Files.delete(it) }
          return FileVisitResult.CONTINUE
        }
      },
    )
  }

  /**
   * 解压 TAR 文件到目标目录
   * @param tarPath 输入的 TAR 文件路径
   * @param outputDir 目标解压目录路径
   * @param overwrite 是否覆盖已存在的文件
   * @throws IOException 解压过程中发生 IO 错误
   */
  @Throws(IOException::class)
  suspend fun extractTar(
    tarPath: Path,
    outputPath: Path,
    overwrite: Boolean = true,
  ) = withContext(Dispatchers.IO) {
    if (!Files.exists(outputPath)) {
      Files.createDirectories(outputPath)
    }

    FileInputStream(tarPath.toFile()).use { fis ->
      BufferedInputStream(fis).use { bis ->
        TarArchiveInputStream(bis).use { tarInput ->
          var entry: TarArchiveEntry?
          while (tarInput.nextEntry.also { entry = it } != null) {
            entry?.let { processEntry(it, tarInput, outputPath, overwrite) }
          }
        }
      }
    }
  }

  private suspend fun processEntry(
    entry: TarArchiveEntry,
    tarInput: TarArchiveInputStream,
    outputPath: Path,
    overwrite: Boolean,
  ) = withContext(Dispatchers.IO) {
    // 安全校验：防止路径遍历攻击
    val targetPath = outputPath.resolve(entry.name).normalize()
    if (!targetPath.startsWith(outputPath)) {
      throw IOException("非法路径: ${entry.name}")
    }

    when {
      entry.isDirectory -> {
        Files.createDirectories(targetPath)
      }

      entry.isFile -> {
        // 创建父目录
        Files.createDirectories(targetPath.parent)

        // 检查文件是否存在
        if (Files.exists(targetPath) && !overwrite) {
          return@withContext
        }

        // 写入文件内容
        Files.copy(tarInput, targetPath)
      }

      else -> {
        throw FileException("不支持的文件类型: ${entry.name}")
      }
    }
  }

  companion object {
    const val QUERY_BATCH = 1000
    const val BACKUP_SQL_FILE = "backup.sql"
    const val UPLOAD_DIR_NAME = "upload"
    const val TEMP_BACKUP_DIR_PREFIX = "backup-"
    const val TEMP_RESTORE_DIR_PREFIX = "restore-"
  }
}
