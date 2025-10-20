package dev.yidafu.nicemaker.core.annotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
@Component
annotation class Service(
  val name: String = "",
)
