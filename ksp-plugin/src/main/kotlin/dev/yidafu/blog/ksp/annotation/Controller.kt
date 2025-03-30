package dev.yidafu.blog.ksp.annotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Controller(
  val path: String = "/"
)
