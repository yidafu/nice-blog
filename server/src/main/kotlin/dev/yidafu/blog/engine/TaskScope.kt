package dev.yidafu.blog.engine

import dev.yidafu.blog.engine.TaskScope.Companion.NAME
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Scope
import org.koin.core.annotation.Scoped
import org.koin.core.annotation.Single

@Module
@ComponentScan
class EngineModule

@Single
class SingleTest()

@Scope(name = NAME)
@Scoped
class TaskScope {
  companion object {
    const val NAME = "TaskScope"
  }
}
