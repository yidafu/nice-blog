package dev.yidafu.blog.common

import java.nio.file.Paths

object BlogConfig {
   val DEFAULT_UPLOAD_DIRECTORY: String = Paths.get(System.getProperty("user.dir"), "upload").toString()
}
