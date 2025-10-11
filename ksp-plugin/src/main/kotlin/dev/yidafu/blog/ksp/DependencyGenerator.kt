package dev.yidafu.blog.ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
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

              componentInfoList.filterIsInstance<ServiceInfo>()
                .buildDependencyTree()
                .forEach { info ->
                  addCode(buildComponentClass(info))
                }
              componentInfoList.filterIsInstance<ControllerInfo>()
                .forEach { info ->
                  addCode(buildComponentClass(info))
                }
              addCode("}")
            }
            .build(),
        ).build()

    fileSpec.writeTo(codeGenerator, false)
  }

  fun buildComponentClass(info: ComponentInfo): CodeBlock {
    val builder = CodeBlock.builder()

    if (info is ServiceInfo) {
      val injectClass: ComponentInfo = info.parentInterface ?: info
      if (info.serviceName.isNotEmpty()) {
        builder.add(
          "  key<%T>(%S) {\n",
          ClassName(injectClass.packageName, injectClass.className),
          info.serviceName,
        )
        builder.add(
          "    %M {\n",
          KtorApplicationProvide,
        )
        builder.add(
          "      %T(",
          ClassName(info.packageName, info.className),
        )
        info.injectComponent.forEach { _ ->
          builder.add("\n      this@%M.resolve(),", KtorApplicationDependencies)
        }
        builder.add(")\n    }\n")

        builder.add("  }\n\n")
      } else {
        builder.add(
          "  %M<%T> {\n    %T(",
          KtorApplicationProvide,
          ClassName(injectClass.packageName, injectClass.className),
          ClassName(info.packageName, info.className),
        )
        info.injectComponent.forEach { _ ->
          builder.add("\n    this@%M.resolve(),", KtorApplicationDependencies)
        }
        builder.add("\n    )\n  }\n\n")

      }

    } else if (info is ControllerInfo) {
      val ctrlClass = ClassName(info.packageName, info.className)

      builder.add(
        "  %M<%T> {\n    %T(",
        KtorApplicationProvide,
        ctrlClass,
        ctrlClass,
      )
      logger.warn("Component Parameter count ${info.injectComponent.size}")
      info.injectComponent.forEach { _ ->
        builder.add("\n    this@%M.resolve(),", KtorApplicationDependencies)
      }
      builder.add("\n    )\n  }\n\n")
    }
    return builder.build()
  }
}
