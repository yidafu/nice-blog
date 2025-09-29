package dev.yidafu.blog.ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ksp.writeTo

class DependencyGenerator(
  private val componentInfoList: List<ComponentInfo>,
  private val codeGenerator: CodeGenerator,
  private val logger: KSPLogger,
) {
  fun generate() {
    val fileSpec =
      FileSpec.builder(RootDIClass)
        .addFunction(
          FunSpec.builder(NiceDIFunctionName)
            .receiver(KtorApplication)
            .addModifiers(KModifier.PUBLIC, KModifier.INLINE)
            .apply {
              addCode("%M {\n", KtorApplicationDependencies)
              componentInfoList.forEach { info ->
                if (info is ServiceInfo) {
                  val injectClass: ComponentInfo = info.parentInterface ?: info
                  addCode(
                    "  %M<%T> {\n    %T(",
                    KtorApplicationProvide,
                    ClassName(injectClass.packageName, injectClass.className),
                    ClassName(info.packageName, info.className),
                  )
                  info.injectComponent.forEach { _ ->
                    addCode("\n    this@%M.resolve(),", KtorApplicationDependencies)
                  }
                  addCode("\n    )\n  }\n\n")
                } else if (info is ControllerInfo) {
                  val ctrlClass = ClassName(info.packageName, info.className)

                  addCode(
                    "  %M<%T> {\n    %T(",
                    KtorApplicationProvide,
                    ctrlClass,
                    ctrlClass,
                  )
                  logger.warn("Component Parameter count ${info.injectComponent.size}")
                  info.injectComponent.forEach { _ ->
                    addCode("\n    this@%M.resolve(),", KtorApplicationDependencies)
                  }
                  addCode("\n    )\n  }\n\n")
                }
              }
              addCode("}")
            }
            .build(),
        ).build()

    fileSpec.writeTo(codeGenerator, false)
  }
}
