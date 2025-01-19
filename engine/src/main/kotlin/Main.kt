package dev.yidafu.blog

import dev.yidafu.blog.dev.yidafu.blog.engine.*
import org.koin.core.context.startKoin
import org.koin.dsl.module

suspend fun main() {
//  LocalSynchronousTask(LocalSyncContext()).sync()

  val koin =
    startKoin {
      val testModule =
        module {
          single<Logger> { StdLogger() }
          single<GitSynchronousTaskTemplate> {
            LocalSynchronousTask(
              GitConfig("https://github.com/yidafu/example-blog.git", branch = "master"),
              DefaultSynchronousListener(),
            )
          }
          single<ArticleManager> {
            DefaultArticleManager()
          }
        }

      modules(testModule)
    }

  koin.koin.get<GitSynchronousTaskTemplate>().sync()
}
