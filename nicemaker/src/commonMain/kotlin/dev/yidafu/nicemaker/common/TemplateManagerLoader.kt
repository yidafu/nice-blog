package dev.yidafu.nicemaker.common

import dev.yidafu.nicemaker.themes.TemplateManager

/**
 * 主题管理器加载器
 * 使用手动注册机制（KMP 兼容）
 */
object TemplateManagerLoader {
  private val managers: MutableList<TemplateManager> = mutableListOf()

  /**
   * 注册主题管理器
   */
  fun register(manager: TemplateManager) {
    if (!managers.any { it.getName() == manager.getName() }) {
      managers.add(manager)
      println("Registered TemplateManager: ${manager.getName()}")
    }
  }

  /**
   * 获取所有已注册的主题管理器
   */
  fun getAll(): List<TemplateManager> = managers.toList()

  /**
   * 根据名称获取主题管理器
   */
  fun getTemplateManager(name: String): TemplateManager {
    return managers.find { it.getName() == name }
      ?: throw IllegalArgumentException("TemplateManager($name) not found. Available: ${managers.map { it.getName() }}")
  }

  /**
   * 清除所有注册（用于测试）
   */
  fun clear() {
    managers.clear()
  }
}
