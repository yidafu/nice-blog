package dev.yidafu.blog.ksp.annotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
@Component
annotation class Service(
  val name: String = "",
)
