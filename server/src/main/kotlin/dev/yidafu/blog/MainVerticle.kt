package dev.yidafu.blog

import dev.yidafu.blog.admin.AdminVerticle
import dev.yidafu.blog.common.db.ExposedDatabase
import dev.yidafu.blog.common.services.ExposedBaseService
import dev.yidafu.blog.fe.FrontendVerticle
import io.vertx.core.AbstractVerticle
import io.vertx.core.Promise
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module
import org.slf4j.LoggerFactory

class MainVerticle : AbstractVerticle() {
  private val log = LoggerFactory.getLogger(MainVerticle::class.java)

  override fun start(startPromise: Promise<Void>) {
    // 初始化Koin
    val module =
      module {
        // 初始化Exposed数据库
        single { ExposedBaseService::class.java }

        // 扫描所有服务类并注入
//        scan("dev.yidafu.blog")
      }

    startKoin {
      modules(module)
    }

    // 初始化Exposed数据库
    ExposedDatabase.init()
    ExposedDatabase.createTables()

    log.info("Database initialized with Exposed")

    // 部署其他Verticle
    vertx.deployVerticle(FrontendVerticle::class.java.name)
    vertx.deployVerticle(AdminVerticle::class.java.name)

    startPromise.complete()
  }
}
