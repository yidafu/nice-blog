package dev.yidafu.blog

import dev.yidafu.blog.admin.AdminVerticle
import dev.yidafu.blog.admin.handler.AdminHandlerModule
import dev.yidafu.blog.admin.services.AdminServiceModule
import dev.yidafu.blog.common.dao.DefaultSchema
import dev.yidafu.blog.common.dao.tables.BArticle
import dev.yidafu.blog.common.handler.CommonHandlerModule
import dev.yidafu.blog.common.services.CommonServiceModule
import dev.yidafu.blog.fe.FrontendVerticle
import dev.yidafu.blog.fe.handler.FeHandlerModule
import dev.yidafu.blog.fe.service.FeServiceModule
import io.vertx.kotlin.coroutines.CoroutineRouterSupport
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.coAwait
import jakarta.persistence.EntityManagerFactory
import jakarta.persistence.Persistence
import org.hibernate.reactive.stage.Stage
import org.jooq.impl.DSL
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.ksp.generated.module
import org.slf4j.LoggerFactory
import java.sql.DriverManager

class MainVerticle : CoroutineVerticle(), CoroutineRouterSupport {
private val log = LoggerFactory.getLogger(MainVerticle::class.java)
  private lateinit var emf: EntityManagerFactory
  private val developmentIdList = mutableListOf<String>()
  override suspend fun start() {

    vertx.executeBlocking { promise ->
      emf = Persistence
        .createEntityManagerFactory("nice-db", emptyMap<String, String>())
      promise.complete(true)
    }.coAwait()

    DSL.using(
      "jdbc:sqlite:./nice-blog.db",
      "",
      ""
    ).use { ctx ->
      ctx.ddl(DefaultSchema.DEFAULT_SCHEMA).queries().forEach { query ->
        log.info("jooq query {}",query)
       }
      ctx.selectCount().from(BArticle.B_ARTICLE)
        .fetch().forEach { r ->
          log.info("joop count {}", r.value1())
        }
    }

    val koin = startKoin {
      printLogger()
      val entityModule = module {
        factory<EntityManagerFactory> {
          emf
        }
        factory<Stage.SessionFactory> {
          emf.unwrap(Stage.SessionFactory::class.java)
        }
      }
      modules(
        entityModule,
        CommonHandlerModule().module,
        CommonServiceModule().module,

        FeServiceModule().module,
        FeHandlerModule().module,

        AdminHandlerModule().module,
        AdminServiceModule().module,
      )
    }


    log.info("start FrontendVerticle")
    vertx.deployVerticle(FrontendVerticle(koin.koin)).andThen { res ->
      log.info("start AdminVerticle")
      developmentIdList.add(res.result())
    }

    log.info("start AdminVerticle")
    vertx.deployVerticle(AdminVerticle(koin.koin)).andThen { res ->
      developmentIdList.add(res.result())

    }
  }

  override suspend fun stop() {
    super.stop()
    developmentIdList.forEach { id ->
      vertx.undeploy(id)
    }
  }
}
