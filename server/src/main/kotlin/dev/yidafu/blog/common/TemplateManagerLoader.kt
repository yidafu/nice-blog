package dev.yidafu.blog.common

import dev.yidafu.blog.themes.TemplateManager
import java.util.*

object TemplateManagerLoader {
  private var managers: List<TemplateManager> = mutableListOf()
  fun load() {
    managers = ServiceLoader.load(TemplateManager::class.java).toList()
    managers.forEach {
      println("Loaded TemplateManager: ${it.getName()}\n\t${it.getDescription()}")
    }
  }

  fun getTemplateManager(name: String): TemplateManager {
    return managers.find { it.getName() == name }
      ?: throw IllegalArgumentException("TemplateManager($name) not found")
  }
}
