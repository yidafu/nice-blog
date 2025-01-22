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
          scope<TaskScope> {
            scoped<Logger> {
              StdLogger()
            }
            scoped<GitConfig> {
              GitConfig("", branch = "")
            }
            scoped<SynchronousListener> {
              DefaultSynchronousListener()
            }
            scoped<BaseGitSynchronousTask> {
              // default SynchronousTask
              GitSynchronousTask(get<GitConfig>(), get(), get(), get())
            }
            scoped<ArticleManager> {
              DefaultArticleManager()
            }
          }
        }

      modules(testModule)
    }
  val scope = koin.koin.createScope<TaskScope>()
  scope.declare<Logger>(StdLogger())

  scope.declare(GitConfig("https://github.com/yidafu/yidafu.dev.git", "master", "uuid"))
  scope.get<BaseGitSynchronousTask>().sync()
  scope.close()
}
