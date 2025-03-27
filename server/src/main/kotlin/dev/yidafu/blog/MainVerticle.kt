package dev.yidafu.blog

import dev.yidafu.blog.admin.AdminVerticle
import dev.yidafu.blog.admin.controller.AdminControllerModule
import dev.yidafu.blog.admin.manager.SynchronousManager
import dev.yidafu.blog.admin.services.AdminServiceModule
import dev.yidafu.blog.common.controller.CommonControllerModule
import dev.yidafu.blog.common.dao.DefaultSchema
import dev.yidafu.blog.common.services.CommonServiceModule
import dev.yidafu.blog.engine.*
import dev.yidafu.blog.fe.FrontendVerticle
import dev.yidafu.blog.fe.controller.FeControllerModule
import dev.yidafu.blog.fe.service.FeServiceModule
import io.vertx.kotlin.coroutines.CoroutineRouterSupport
import io.vertx.kotlin.coroutines.CoroutineVerticle
import org.jooq.CloseableDSLContext
import org.jooq.DDLExportConfiguration
import org.jooq.DDLFlag
import org.jooq.impl.DSL
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.ksp.generated.module
import org.slf4j.LoggerFactory

class MainVerticle : CoroutineVerticle(), CoroutineRouterSupport {
  private val log = LoggerFactory.getLogger(MainVerticle::class.java)
  private val developmentIdList = mutableListOf<String>()
  private lateinit var dslContext: CloseableDSLContext

  private fun readResource(filename: String): List<String> {
    return MainVerticle::class.java.classLoader
      .getResourceAsStream(filename)
      ?.bufferedReader()?.lines()?.toList() ?: emptyList()
  }

  override suspend fun start() {
    val jooqContext: CloseableDSLContext =
      DSL.using(
        "jdbc:sqlite:./nice-blog.db",
        "",
        "",
      )
    dslContext = jooqContext

    val dbConfig =
      DDLExportConfiguration()
        .flags(DDLFlag.TABLE, DDLFlag.PRIMARY_KEY, DDLFlag.UNIQUE, DDLFlag.INDEX, DDLFlag.COMMENT)
        .createTableIfNotExists(true)
        .createSchemaIfNotExists(true)
        .createSequenceIfNotExists(true)

    jooqContext.ddl(DefaultSchema.DEFAULT_SCHEMA, dbConfig)
      .queries()
      .forEach { query -> query.execute() }

    log.info("execute setup sql")
    readResource("META-INF/sql/setup.sql")
      .filterNot { it.isBlank() }
      .filterNot { it.startsWith("--") }
      .forEach { jooqContext.execute(it) }

    val koin =
      startKoin {
        printLogger()

        val jooqModule =
          module {
            factory<CloseableDSLContext> { jooqContext }
            single<SynchronousManager> {
              SynchronousManager(get(), get())
            }
          }

        modules(
          jooqModule,
          CommonServiceModule().module,
          FeServiceModule().module,
          AdminServiceModule().module,
          CommonControllerModule().module,
          FeControllerModule().module,
          AdminControllerModule().module,
          EngineModule().module,
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
    dslContext.close()
    developmentIdList.forEach { id ->
      vertx.undeploy(id)
    }
  }
}
