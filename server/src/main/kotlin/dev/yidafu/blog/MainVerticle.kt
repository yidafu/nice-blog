package dev.yidafu.blog

import dev.yidafu.blog.handler.HandlerModule
import dev.yidafu.blog.routes.mountPostRoutes
import dev.yidafu.blog.service.ServiceModule
import io.vertx.config.ConfigStoreOptions
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Router
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
      modules(entityModule, HandlerModule().module, ServiceModule().module)
    }


    vertx.deployVerticle(FrontendVerticle(koin.koin))
    vertx.deployVerticle(SyncVerticle(koin.koin))
    vertx.deployVerticle(AdminVerticle(koin.koin))
  }
}
