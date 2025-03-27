package dev.yidafu.blog.common

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class SuspendingLazy<T>(initializer: suspend () -> T) {
  private var initializer: (suspend () -> T)? = initializer
  private var mutex: Mutex? = Mutex()
  private var mValue: T? = null

  suspend fun value(): T {
    val m = mutex ?: return mValue as T // Warning: unchecked cast.
    m.withLock {
      val i = initializer ?: return mValue as T // Warning: unchecked cast.
      val v = i()
      mValue = v
      initializer = null
      mutex = null
      return v
    }
  }
}
