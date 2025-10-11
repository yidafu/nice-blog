package dev.yidafu.blog.ksp

class TreeNode<T>(
  val data: T,
  var children: Set<TreeNode<T>>,
  var height: Int = 0,
)

/**
 * 构建TreeNode树并更新高度，然后根据高度排序后返回
 */
fun List<ServiceInfo>.buildDependencyTree(): List<ServiceInfo> {
  // 1. 创建节点映射，用于快速查找组件
  val nodeMap = mutableMapOf<String, TreeNode<ServiceInfo>>()
  val allNodes = mutableListOf<TreeNode<ServiceInfo>>()

  // 2. 为每个ServiceInfo创建TreeNode节点
  forEach {
    val node = TreeNode(it, emptySet())
    nodeMap[it.fullName] = node
    allNodes.add(node)
  }

  // 3. 建立父子关系
  forEach { serviceInfo ->
    val parentNode = nodeMap[serviceInfo.fullName]
    val parentList = mutableListOf<ServiceInfo>()
    parentList.addAll(serviceInfo.injectComponent.filterIsInstance<ServiceInfo>())
    if (serviceInfo.parentInterface != null && serviceInfo.parentInterface is ServiceInfo) {
      parentList.add(serviceInfo.parentInterface)
    }

      val childNode = nodeMap[serviceInfo.fullName]
    parentList.forEach { parent ->
      val parentNode = nodeMap[parent.fullName]
      val newChildren = parentNode?.children?.toMutableSet() ?: mutableSetOf()
      if (childNode != null) {
        newChildren.add(childNode)
        parentNode?.children = newChildren
      }
    }
  }

  // 4. 更新所有节点的高度
  val rootNodes = allNodes.filter { node ->
    // 根节点是那些没有被任何其他节点引用的节点
    !allNodes.any { parentNode ->
      parentNode.children.any { child -> child.data.fullName == node.data.fullName }
    }
  }

  // 递归更新节点高度
  fun updateHeight(node: TreeNode<ServiceInfo>): TreeNode<ServiceInfo> {
    if (node.children.isEmpty()) {
      node.height = 0
      return node
    }

    val updatedChildren = node.children.map { updateHeight(it) }
    val maxChildHeight = updatedChildren.maxOfOrNull { it.height } ?: 0
    node.height = maxChildHeight + 1
    return node
  }

// 更新所有根节点的高度
  val updatedRootNodes = rootNodes.map { updateHeight(it) }

// 5. 收集所有节点并按高度排序
  val allUpdatedNodes = mutableListOf<TreeNode<ServiceInfo>>()

  fun collectAllNodes(node: TreeNode<ServiceInfo>) {
    allUpdatedNodes.add(node)
    node.children.forEach { collectAllNodes(it) }
  }

  updatedRootNodes.forEach { collectAllNodes(it) }

// 按高度降序排序并返回ServiceInfo列表
  return allUpdatedNodes.sortedByDescending { it.height }.map { it.data }.toSet().toList()
}
