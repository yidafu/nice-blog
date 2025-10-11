package dev.yidafu.blog.ksp.dev.yidafu.blog.ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.MemberName.Companion.member
import com.squareup.kotlinpoet.ksp.writeTo
import dev.yidafu.blog.ksp.ControllerRouteInfo
import dev.yidafu.blog.ksp.HttpMethod
import dev.yidafu.blog.ksp.KtorApplication
import dev.yidafu.blog.ksp.KtorApplicationDependencies
import dev.yidafu.blog.ksp.KtorApplicationResolve
import dev.yidafu.blog.ksp.KtorApplicationRouteAny
import dev.yidafu.blog.ksp.KtorApplicationRouteDelete
import dev.yidafu.blog.ksp.KtorApplicationRouteGet
import dev.yidafu.blog.ksp.KtorApplicationRoutePost
import dev.yidafu.blog.ksp.KtorApplicationRoutePut
import dev.yidafu.blog.ksp.KtorApplicationRouting
import dev.yidafu.blog.ksp.KtorRoutingContextCall
import dev.yidafu.blog.ksp.MethodInfo
import dev.yidafu.blog.ksp.toGeneratedClassName
import dev.yidafu.blog.ksp.toVariableName
import kotlin.collections.forEach

class RouteMapGenerator(
  private val controllerInfoList: List<ControllerRouteInfo>,
  private val codeGenerator: CodeGenerator,
) {
  fun generate() {
    controllerInfoList.forEach(::buildControllerRouteMapFile)
    controllerInfoList.groupBy { it.packageName }
      .toList()
      .forEachIndexed { idx, (packageName, infoList) ->
        buildControllerGroupMap(
          ClassName(packageName.toGeneratedClassName(), "CreateRoutes${idx + 1}"),
          "createRoutes${idx + 1}",
          infoList,
        )
      }

    buildRootRouteMapFile(controllerInfoList)
  }

  private fun buildControllerGroupMap(
    className: ClassName,
    funName: String,
    ctrlInfoList: List<ControllerRouteInfo>,
  ) {
    val fileSpec =
      FileSpec.builder(className)
        .addFunction(
          FunSpec.builder(funName)
            .receiver(KtorApplication)
            .addModifiers(KModifier.SUSPEND, KModifier.PUBLIC, KModifier.INLINE)
            .apply {
//              addCode("%M {\n", KtorApplicationRouting)
              ctrlInfoList.forEach { info ->
                val routerMapFunctionMember = ClassName(info.packageName.toGeneratedClassName(), info.routeMapFunctionName)
                addStatement("  %T()", routerMapFunctionMember)
              }
//              addCode("}")
            }
            .build(),
        ).build()

    fileSpec.writeTo(codeGenerator, false)
  }

  private fun buildRootRouteMapFile(infoList: List<ControllerRouteInfo>) {
//    buildControllerGroupMap(KtorApplication, "createRoute", infoList)
  }

  fun buildControllerRouteMapFile(controllerInfo: ControllerRouteInfo) {
    val className = ClassName(controllerInfo.packageName, controllerInfo.className)

    val routeMapClassName = ClassName(
      controllerInfo.packageName.toGeneratedClassName(),
      controllerInfo.routeMapClassName
    )
    val fileSpec =
      FileSpec
        .builder(routeMapClassName)
        .addFunction(
          FunSpec.builder(controllerInfo.routeMapFunctionName)
            .receiver(KtorApplication)
            .addModifiers(KModifier.SUSPEND, KModifier.PUBLIC, KModifier.INLINE)
            .apply {
              addStatement(
                "val %N = %M.%M<%T>()",
                className.toVariableName(),
                KtorApplicationDependencies,
                KtorApplicationResolve,
                className,
              )
              addCode("%M {\n", KtorApplicationRouting)
              controllerInfo.paths.forEach { method ->
                addCode(
                  buildRouteStatement(className, method),
                )
              }
              addCode("}")
            }
            .build(),
        ).build()

    fileSpec.writeTo(codeGenerator, false)
  }

  private fun buildRouteStatement(
    className: ClassName,
    method: MethodInfo,
  ): CodeBlock {
//    logger.warn("build method $method")
    val codeBlock = CodeBlock.builder()

    val methodMember =
      when (method.method) {
        HttpMethod.GET -> {
          KtorApplicationRouteGet
        }

        HttpMethod.POST -> {
          KtorApplicationRoutePost
        }

        HttpMethod.PUT -> {
          KtorApplicationRoutePut
        }

        HttpMethod.DELETE -> {
          KtorApplicationRouteDelete
        }

        HttpMethod.ANY -> KtorApplicationRouteAny
      }

    codeBlock.add("    %M(%S) {\n", methodMember, method.path)
    val handlerFunction: MemberName = className.member(method.funName)

    codeBlock.add(
      "      %N.%N(%N)\n",
      className.toVariableName(),
      handlerFunction,
      KtorRoutingContextCall,
    )
    codeBlock.add("    }\n")

    codeBlock.add("\n")
    return codeBlock.build()
  }
}
