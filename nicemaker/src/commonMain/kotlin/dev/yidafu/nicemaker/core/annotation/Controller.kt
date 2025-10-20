package dev.yidafu.nicemaker.core.annotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
@Component
annotation class Controller(
  val path: String = "/",
)
