package dev.yidafu.blog.common

import java.io.IOException
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes

object FileUtils {
  @Throws(IOException::class)
  fun copy(
    sourceDir: Path,
    targetDir: Path,
  ) {
    if (Files.notExists(sourceDir)) {
      throw NoSuchFileException("Source directory does not exist: $sourceDir")
    }

    if (Files.notExists(targetDir)) {
      Files.createDirectories(targetDir)
    }

    Files.walkFileTree(
      sourceDir,
      object : SimpleFileVisitor<Path>() {
        override fun preVisitDirectory(
          dir: Path,
          attrs: BasicFileAttributes,
        ): FileVisitResult {
          val targetPath = targetDir.resolve(sourceDir.relativize(dir))
          if (Files.notExists(targetPath)) {
            Files.createDirectory(targetPath)
          }
          return FileVisitResult.CONTINUE
        }

        override fun visitFile(
          file: Path,
          attrs: BasicFileAttributes,
        ): FileVisitResult {
          val targetPath = targetDir.resolve(sourceDir.relativize(file))
          Files.copy(file, targetPath, StandardCopyOption.REPLACE_EXISTING)
          return FileVisitResult.CONTINUE
        }

        override fun visitFileFailed(
          file: Path,
          exc: IOException,
        ): FileVisitResult {
          throw exc
        }
      },
    )
  }
}
