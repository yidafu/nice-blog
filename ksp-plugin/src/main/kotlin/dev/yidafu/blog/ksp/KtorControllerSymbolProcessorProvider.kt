package dev.yidafu.blog.ksp

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.Modifier
import dev.yidafu.blog.common.annotation.*
import dev.yidafu.blog.common.annotation.Any
import dev.yidafu.blog.ksp.dev.yidafu.blog.ksp.RouteMapGenerator

fun KSClassDeclaration.resolveInjectParameters(logger: KSPLogger): List<ComponentInfo> {
  logger.warn("primaryConstructor parameters ${this.primaryConstructor?.parameters?.size ?: -1}")
  return this.primaryConstructor?.parameters?.mapNotNull { parameter ->
    val ksType = parameter.type.resolve().declaration

    if (ksType is KSClassDeclaration) {
      ComponentInfo(
        ksType.packageName.asString(),
        ksType.simpleName.asString(),
        emptyList(),
      )
    } else {
      null
    }
  }?.toList() ?: emptyList()
}

class VertexControllerSymbolProcessorProvider : SymbolProcessorProvider {
  override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
    return KtorControllerSymbolProcessor(environment)
  }
}

class KtorControllerSymbolProcessor(private val environment: SymbolProcessorEnvironment) : SymbolProcessor {
  private val logger = environment.logger

  private val controllerInfoList = mutableListOf<ControllerRouteInfo>()
  private val componentInfoList = mutableListOf<ComponentInfo>()
  init {
    environment.logger.warn("KSP options: ${environment.options}")
  }
  override fun process(resolver: Resolver): List<KSAnnotated> {
      resolver.getSymbolsWithAnnotation(Service::class.qualifiedName!!)
        .filterIsInstance<KSClassDeclaration>()
        .forEach { symbol ->
          logger.warn("@Component annotation ${symbol.packageName.asString()}.${symbol.simpleName.asString()}")
          val serviceAnnotation =
            symbol.annotations.firstOrNull { a ->
              a.shortName.asString() == Service::class.simpleName
            }
          if (serviceAnnotation != null) {
            val name = serviceAnnotation.arguments.firstOrNull()?.value as String?
            val interfaceInfo: InterfaceInfo? =
              symbol.superTypes
                .map { it.resolve() }
                .mapNotNull { it.declaration } // 获取类型声明
                .filterIsInstance<KSClassDeclaration>() // 确保是类声明
                .filter { it.classKind == ClassKind.INTERFACE } // 过滤出接口
                .firstOrNull()?.let {
                  InterfaceInfo(it.packageName.asString(), it.simpleName.asString())
                }
            // 找到最近的 Interface

            componentInfoList.add(
              ServiceInfo(
                packageName = symbol.packageName.asString(),
                className = symbol.simpleName.asString(),
                parentInterface = interfaceInfo,
                serviceName = name ?: "",
                symbol.resolveInjectParameters(logger),
              ),
            )
            logger.warn("find Service => ${symbol.simpleName.asString()}")
          }
        }

      resolver
        .getSymbolsWithAnnotation(Controller::class.qualifiedName!!)
        .filterIsInstance<KSClassDeclaration>()
        .forEach { symbol ->

          val ctrlAnnotaion =
            symbol.annotations.firstOrNull { a ->
              logger.warn("@Controller annotation 1 ${a.shortName.asString()} ${Controller::class.simpleName}")

              a.shortName.asString() == Controller::class.simpleName
            }
          if (ctrlAnnotaion != null) {
            componentInfoList.add(
              ControllerInfo(
                packageName = symbol.packageName.asString(),
                className = symbol.simpleName.asString(),
                injectComponent = symbol.resolveInjectParameters(logger),
              ),
            )
          }

          val rootPath = ctrlAnnotaion?.arguments?.firstOrNull()?.value as String?
          val packageName = symbol.packageName.asString()
          val className = symbol.simpleName.asString()
          logger.warn("@Controller annotation package $packageName class name $className")

          val methodList =
            symbol.getAllFunctions().map { func ->
              func.annotations.filter {
                it.shortName.getShortName() in
                  listOf(
                    Get::class.simpleName,
                    Post::class.simpleName,
                    Put::class.simpleName,
                    Delete::class.simpleName,
                    Any::class.simpleName,
                  )
              }.map { methodAnnotation ->
                val path = methodAnnotation.arguments.firstOrNull()?.value as String?
                val method =
                  when (methodAnnotation.shortName.getShortName()) {
                    Get::class.simpleName -> HttpMethod.GET
                    Post::class.simpleName -> HttpMethod.POST
                    Put::class.simpleName -> HttpMethod.PUT
                    Delete::class.simpleName -> HttpMethod.DELETE
                    Any::class.simpleName -> HttpMethod.ANY
                    else -> throw IllegalArgumentException("unknown method")
                  }
//            logger.warn("Method annotation function $method=>$path ${func.simpleName.getShortName()}")

                MethodInfo(
                  method,
                  path ?: "",
                  func.simpleName.asString(),
                  func.modifiers.contains(Modifier.SUSPEND),
                )
              }
            }.flatten().toList()
//        logger.warn("controller info $rootPath $className ${methodList.size}")

          controllerInfoList.add(

            ControllerRouteInfo(
              rootPath ?: "/",
              packageName,
              className,
              methodList,
            )
          )
        }

    return emptyList()
  }

  override fun finish() {
    super.finish()
    DependencyGenerator(componentInfoList, environment.codeGenerator, environment.logger).generate()
    RouteMapGenerator(controllerInfoList, environment.codeGenerator).generate()
  }
}
