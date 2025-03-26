package dev.yidafu.blog.ksp

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.MemberName.Companion.member
import com.squareup.kotlinpoet.ksp.writeTo
import dev.yidafu.blog.ksp.annotation.*

class VertexControllerSymbolProcessorProvider : SymbolProcessorProvider {
  override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
    return VertexControllerSymbolProcessor(environment)
  }
}

enum class HttpMethod {
  GET,
  POST,
  PUT,
  DELETE
}

data class MethodInfo(
  val method: HttpMethod,
  val path: String,
  val funName: String,
)

data class ControllerInfo(
  val rootPath: String,
  val packageName: String,
  val className: String,
  val paths: List<MethodInfo>,
) {
  val routeMapFunctionName: String
      get() = "create${className}Route"

  val routeMapClassName: String
    get() = "$className\$RouteMap"
}

class VertexControllerSymbolProcessor(private val environment: SymbolProcessorEnvironment) : SymbolProcessor {
  private val logger = environment.logger

  private val controllerInfoList = mutableListOf<ControllerInfo>()

  override fun process(resolver: Resolver): List<KSAnnotated> {
    val infoList = resolver
      .getSymbolsWithAnnotation(Controller::class.qualifiedName!!)
      .filterIsInstance<KSClassDeclaration>()
      .map { symbol ->
        val rootPath =
          symbol.annotations.firstOrNull { a ->
            logger.warn("@Controller annotation 1 ${a.shortName.asString()} ${Controller::class.simpleName}")

            a.shortName.asString() == Controller::class.simpleName
          }?.arguments?.firstOrNull()?.value as String?
        val packageName = symbol.packageName.asString()
        val className = symbol.simpleName.asString()
        logger.warn("@Controller annotation package $packageName class name $className")

        val methodList = symbol.getAllFunctions().map { func ->
          func.annotations.filter {
            it.shortName.getShortName() in listOf(
              Get::class.simpleName,
              Post::class.simpleName,
              Put::class.simpleName,
              Delete::class.simpleName
            )
          }.map { methodAnnotation ->
            val path = methodAnnotation.arguments.first().value as String
            val method = when (methodAnnotation.shortName.getShortName()) {
              Get::class.simpleName -> HttpMethod.GET
              Post::class.simpleName -> HttpMethod.POST
              Put::class.simpleName -> HttpMethod.PUT
              Delete::class.simpleName -> HttpMethod.DELETE
              else -> throw IllegalArgumentException("unknown method")
            }
            logger.warn("Method annotation function $method=>$path ${func.simpleName.getShortName()}")

            MethodInfo(
              method,
              path,
              func.simpleName.asString()
            )
          }
        }.flatten().toList()
        logger.warn("controller info $rootPath $className ${methodList.size}")

        ControllerInfo(
          rootPath ?: "/",
          packageName,
          className,
          methodList
        )
      }.toList()

    controllerInfoList.addAll(infoList)

    return emptyList()
  }

  override fun finish() {
    super.finish()
    controllerInfoList.forEach(::buildControllerRouteMapFile)
    controllerInfoList.groupBy { it.packageName }
      .forEach { (packageName, infoList) ->
        buildControllerGroupMap(
          ClassName(packageName, "CreateRoutes"),
          "createRoutes",
          infoList,
        )
      }

    buildRootRouteMapFile(controllerInfoList)
  }


  private fun buildControllerGroupMap(
    className: ClassName,
    funName: String,
    ctrlInfoList: List<ControllerInfo>,
  ) {
    val fileSpec = FileSpec.builder(className)
      .addFunction(
        FunSpec.builder(funName)
          .receiver(CoroutineRouterSupportType)
          .apply {
            addParameter(RouterParameterType)
            ctrlInfoList.forEach { info ->
              val routerMapFunctionMember =  ClassName(info.packageName, info.routeMapFunctionName)
              addStatement("%T(%N)", routerMapFunctionMember, RouterParameterType)
            }
          }
          .build()
      ).build()

    fileSpec.writeTo(environment.codeGenerator, false)
  }

  private fun buildRootRouteMapFile(infoList: List<ControllerInfo>) {
    buildControllerGroupMap(CreateRouteClassName, "createRoute", infoList)
  }

  fun buildControllerRouteMapFile(controllerInfo: ControllerInfo) {
    val className = ClassName(controllerInfo.packageName, controllerInfo.className)

    val routeMapClassName = ClassName(controllerInfo.packageName, controllerInfo.routeMapClassName)
    val fileSpec = FileSpec
      .builder(routeMapClassName)
      .addFunction(
        FunSpec.builder(controllerInfo.routeMapFunctionName)
          .receiver(CoroutineRouterSupportType)
          .apply {
            addParameter(RouterParameterType)
            addStatement("val koin = %T.get()", GlobalContextType)
            addStatement("val controller = koin.get<%T>()", className)
            controllerInfo.paths.forEach { method ->
              addCode(buildRouteStatement(className, method))
            }
          }
          .build()
      ).build()

    fileSpec.writeTo(environment.codeGenerator, false)
  }

  private fun buildRouteStatement(className: ClassName, method: MethodInfo): CodeBlock {

    logger.warn("build method $method")
    val methodMember = when (method.method) {
      HttpMethod.GET -> {
        RouterType.member("get")
      }

      HttpMethod.POST -> {
        RouterType.member("post")
      }

      HttpMethod.PUT -> {
        RouterType.member("put")
      }

      HttpMethod.DELETE -> {
        RouterType.member("delete")
      }
    }

    val handlerFunction: MemberName = className.member(method.funName)



    return CodeBlock.builder().addStatement(
      "router.%N(%S).%N(requestHandler = %N::%N)",
      methodMember,
      method.path,
      RouterType.member("coHandler"),
      "controller",
      handlerFunction,
    )
      .build()
  }
}
