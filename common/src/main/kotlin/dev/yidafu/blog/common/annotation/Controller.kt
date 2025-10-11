package dev.yidafu.blog.common.annotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
@Component
annotation class Controller(
  val path: String = "/",
)
