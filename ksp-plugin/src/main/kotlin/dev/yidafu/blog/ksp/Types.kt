package dev.yidafu.blog.ksp

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterSpec

val VertxType = ClassName("io.vertx.core", "Vertx")
val RouterType = ClassName("io.vertx.ext.web", "Router")

val CoroutineRouterSupportType = ClassName("io.vertx.kotlin.coroutines", "CoroutineRouterSupport")

val RouterParameterType = ParameterSpec(
  "router",
  RouterType
)


val CreateRouteClassName = ClassName("dev.yidafu.blog.ksp", "CreateRoute")
