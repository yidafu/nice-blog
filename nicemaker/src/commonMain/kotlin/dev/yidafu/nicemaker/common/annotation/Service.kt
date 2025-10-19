package dev.yidafu.nicemaker.common.annotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
@Component
annotation class Service(
  val name: String = "",
)
