package dev.yidafu.blog

import dev.yidafu.blog.engine.*
import org.koin.core.context.startKoin
import org.koin.core.qualifier.StringQualifier
import org.koin.ksp.generated.module

suspend fun main() {
  val koin =
    startKoin {
      modules(EngineModule().module)
    }
  val scope = koin.koin.createScope("uuid", StringQualifier(TaskScope.NAME), TaskScope::class)

  scope.declare(GitConfig("https://github.com/yidafu/yidafu.dev.git", "master", "uuid"))
//  scope.declare<Logger>(StdLogger())
  scope.get<BaseGitSynchronousTask>().sync()
  scope.close()
}
