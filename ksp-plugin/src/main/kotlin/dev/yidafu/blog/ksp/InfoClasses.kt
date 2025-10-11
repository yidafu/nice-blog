package dev.yidafu.blog.ksp

enum class HttpMethod {
  GET,
  POST,
  PUT,
  DELETE,
  ANY,
}

data class MethodInfo(
  val method: HttpMethod,
  val path: String,
  val funName: String,
  val isSuspend: Boolean = true,
)

class ControllerRouteInfo(
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

class InterfaceInfo(
  packageName: String,
  className: String,
) : ComponentInfo(packageName, className, emptyList())

open class ComponentInfo(
  val packageName: String,
  val className: String,
  val injectComponent: List<ComponentInfo>,
) {
  val fullName = "$packageName.$className"
}

class ControllerInfo(
  packageName: String,
  className: String,
  injectComponent: List<ComponentInfo>,
) : ComponentInfo(packageName, className, injectComponent)

class ServiceInfo(
  packageName: String,
  className: String,
  val parentInterface: ComponentInfo?,
  val serviceName: String = "",
  injectComponent: List<ComponentInfo>,
) : ComponentInfo(packageName, className, injectComponent)
