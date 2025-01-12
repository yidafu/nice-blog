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

  private fun readResource(filename: String): String {
    return MainVerticle::class.java.classLoader
      .getResourceAsStream(filename)
        ?.bufferedReader()?.readText() ?: ""
  }

  override suspend fun start() {

    val jooqContext: CloseableDSLContext = DSL.using(
      "jdbc:sqlite:./nice-blog.db",
      "",
      ""
    )

      val dbConfig =  DDLExportConfiguration()
        .flags(DDLFlag.TABLE)
        .createTableIfNotExists(true)
        .createSchemaIfNotExists(true)
        .createSequenceIfNotExists(true)


    jooqContext.ddl(DefaultSchema.DEFAULT_SCHEMA, dbConfig).queries().forEach { query ->
      query.execute()
    }
    jooqContext.selectCount().from(BArticle.B_ARTICLE)
      .fetch().forEach { r ->
          log.info("joop count {}", r.value1())
        }
    log.info("execute setup sql")
    jooqContext.query(readResource("META-INF/sql/setup.sql")).execute()

    val koin = startKoin {
      printLogger()

      val JooqModule = module {
        factory<CloseableDSLContext> { jooqContext }
      }
      modules(
        JooqModule,
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
