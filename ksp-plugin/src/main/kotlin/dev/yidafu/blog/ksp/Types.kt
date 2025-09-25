package dev.yidafu.blog.ksp

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.MemberName

val GlobalContextType = ClassName("org.koin.core.context", "GlobalContext")

val KtorApplication = ClassName("io.ktor.server.application", "Application")

val KtorApplicationRoute = ClassName("io.ktor.server.routing", "Route")
val KtorApplicationRouting = MemberName(KtorApplicationRoute.packageName, "routing")
val KtorApplicationRouteGet = MemberName(KtorApplicationRoute.packageName, "get")
val KtorApplicationRoutePost = MemberName(KtorApplicationRoute.packageName, "post")
val KtorApplicationRoutePut = MemberName(KtorApplicationRoute.packageName, "put")
val KtorApplicationRouteDelete = MemberName(KtorApplicationRoute.packageName, "delete")
val KtorRoutingContext = ClassName("io.ktor.server.routing", "RoutingContext")
val KtorRouting = ClassName("io.ktor.server.routing", "Routing")
val KtorRoutingContextCall = MemberName(KtorRoutingContext, "call")
val KtorApplicationRouteAny = MemberName("dev.yidafu.blog.common.ext", "any")

val RootDIClass = ClassName("dev.yidafu.blog", "RootDI")

val KtorPluginDI = ClassName("io.ktor.server.plugins.di", "DI")
val KtorApplicationDependencies = MemberName(KtorPluginDI.packageName, "dependencies")
val KtorApplicationProvide = MemberName(KtorPluginDI.packageName, "provide")
