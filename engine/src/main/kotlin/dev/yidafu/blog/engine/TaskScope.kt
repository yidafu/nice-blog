package dev.yidafu.blog.dev.yidafu.blog.engine

import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.createScope
import org.koin.core.scope.Scope

class TaskScope() : KoinScopeComponent {
  override val scope: Scope by lazy {
    createScope(this)
  }

  fun close() {
    scope.close()
  }
}
