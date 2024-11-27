package dev.yidafu.blog

import dev.yidafu.blog.admin.AdminVerticle
import dev.yidafu.blog.admin.handler.AdminHandlerModule
import dev.yidafu.blog.admin.services.AdminServiceModule
import dev.yidafu.blog.common.handler.HandlerModule
import dev.yidafu.blog.common.services.CommonServiceModule
import dev.yidafu.blog.fe.FrontendVerticle
import dev.yidafu.blog.fe.handler.FeHandlerModule
import dev.yidafu.blog.fe.service.FeServiceModule
import dev.yidafu.blog.sync.SyncVerticle
import dev.yidafu.blog.sync.handler.SyncHandlerModule
import io.vertx.kotlin.coroutines.CoroutineRouterSupport
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.coAwait
import jakarta.persistence.EntityManagerFactory
import jakarta.persistence.Persistence
import org.hibernate.reactive.stage.Stage
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.ksp.generated.module


class MainVerticle : CoroutineVerticle(), CoroutineRouterSupport {

  private lateinit var emf: EntityManagerFactory

  override suspend fun start() {
    val server =
      vertx
        .createHttpServer()

    val props = mapOf(
      "javax.persistence.jdbc.url" to "jdbc:mysql://localhost:3306/nice-blog?allowPublicKeyRetrieval=true&amp;useSSL=false",
      "javax.persistence.jdbc.driver" to "com.mysql.jdbc.Driver"
    )

    vertx.executeBlocking { promise ->
      emf = Persistence
        .createEntityManagerFactory("nice-db", emptyMap<String, String>())

      promise.complete(true)
    }.coAwait()


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
        HandlerModule().module,
        CommonServiceModule().module,
        FeServiceModule().module,
        AdminHandlerModule().module,
        AdminServiceModule().module,
        SyncHandlerModule().module,
        FeHandlerModule().module,
        FeServiceModule().module,
      )
    }


    vertx.deployVerticle(FrontendVerticle(koin.koin))
    vertx.deployVerticle(AdminVerticle(koin.koin))
    vertx.deployVerticle(SyncVerticle(koin.koin))
  }
}
