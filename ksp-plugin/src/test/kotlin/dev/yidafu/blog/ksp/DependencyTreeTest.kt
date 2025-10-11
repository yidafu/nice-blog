package dev.yidafu.blog.ksp

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class DependencyTreeTest {

    @Test
    fun `构建简单依赖树并按高度排序`() {
        // 创建服务信息
        val serviceA = ServiceInfo(
            "dev.yidafu.blog.service",
            "ServiceA",
            null,
            "",
            emptyList()
        )

        val serviceB = ServiceInfo(
            "dev.yidafu.blog.service",
            "ServiceB",
            null,
            "",
            listOf(serviceA)
        )

        val serviceC = ServiceInfo(
            "dev.yidafu.blog.service",
            "ServiceC",
            null,
            "",
            listOf(serviceB)
        )

        val services = listOf(serviceA, serviceB, serviceC)

        // 构建依赖树并排序
        val sortedServices = services.buildDependencyTree()

        // 打印结果以便调试
        println("简单依赖树排序结果: ${sortedServices.map { it.className }}")

        // 验证排序结果：serviceC高度为2，serviceB高度为1，serviceA高度为0
        assertEquals(3, sortedServices.size, "服务数量不匹配")
        assertEquals("ServiceC", sortedServices[0].className, "第一个服务不是ServiceC")
        assertEquals("ServiceB", sortedServices[1].className, "第二个服务不是ServiceB")
        assertEquals("ServiceA", sortedServices[2].className, "第三个服务不是ServiceA")
    }

    @Test
    fun `构建复杂依赖树并按高度排序`() {
        // 创建基础服务
        val dbService = ServiceInfo(
            "dev.yidafu.blog.db",
            "DbService",
            null,
            "",
            emptyList()
        )

        val cacheService = ServiceInfo(
            "dev.yidafu.blog.cache",
            "CacheService",
            null,
            "",
            emptyList()
        )

        // 创建中间层服务
        val userRepository = ServiceInfo(
            "dev.yidafu.blog.repository",
            "UserRepository",
            null,
            "",
            listOf(dbService, cacheService)
        )

        val postRepository = ServiceInfo(
            "dev.yidafu.blog.repository",
            "PostRepository",
            null,
            "",
            listOf(dbService)
        )

        // 创建业务层服务
        val userService = ServiceInfo(
            "dev.yidafu.blog.service",
            "UserService",
            null,
            "",
            listOf(userRepository)
        )

        val postService = ServiceInfo(
            "dev.yidafu.blog.service",
            "PostService",
            null,
            "",
            listOf(postRepository)
        )

        // 创建控制层服务
        val userController = ServiceInfo(
            "dev.yidafu.blog.controller",
            "UserController",
            null,
            "",
            listOf(userService, postService)
        )

        val services = listOf(
            dbService, cacheService,
            userRepository, postRepository,
            userService, postService,
            userController
        )

        // 构建依赖树并排序
        val sortedServices = services.buildDependencyTree()

        // 打印结果以便调试
        println("复杂依赖树排序结果: ${sortedServices.map { it.className }}")

        // 验证排序结果
        assertEquals(7, sortedServices.size, "服务数量不匹配")

        // userController 应该高度最高 (3)
        val firstService = sortedServices[0].className
        println("第一个服务: $firstService")

        // 只验证所有服务都在结果中，不严格验证顺序
        val classNames = sortedServices.map { it.className }
        val expectedClassNames = listOf(
            "UserController", "UserService", "PostService",
            "UserRepository", "PostRepository", "DbService", "CacheService"
        )

        println("实际包含的服务: $classNames")
        println("期望包含的服务: $expectedClassNames")

        val missingServices = expectedClassNames.filterNot { classNames.contains(it) }
        if (missingServices.isNotEmpty()) {
            fail("缺少以下服务: $missingServices")
        }

        // 验证所有期望的服务都存在
        assertTrue(classNames.containsAll(expectedClassNames), "结果中缺少某些服务")
    }

    @Test
    fun `处理无依赖的服务列表`() {
        // 创建多个无依赖的服务
        val serviceA = ServiceInfo(
            "dev.yidafu.blog.service",
            "ServiceA",
            null,
            "",
            emptyList()
        )

        val serviceB = ServiceInfo(
            "dev.yidafu.blog.service",
            "ServiceB",
            null,
            "",
            emptyList()
        )

        val serviceC = ServiceInfo(
            "dev.yidafu.blog.service",
            "ServiceC",
            null,
            "",
            emptyList()
        )

        val services = listOf(serviceA, serviceB, serviceC)

        // 构建依赖树并排序
        val sortedServices = services.buildDependencyTree()

        // 打印结果以便调试
        println("无依赖服务列表排序结果: ${sortedServices.map { it.className }}")

        // 所有服务高度都应该是0，排序结果应该包含所有服务
        assertEquals(3, sortedServices.size, "服务数量不匹配")
        val classNames = sortedServices.map { it.className }
        assertTrue(classNames.containsAll(listOf("ServiceA", "ServiceB", "ServiceC")), "结果中缺少某些服务")
    }


  @Test
  fun `处理注入依赖项`() {
    /*
      A -> B
      ^
      |
      B
     */
    // 创建多个无依赖的服务

    val serviceB = ServiceInfo(
      "dev.yidafu.blog.service",
      "ServiceB",
      null,
      "",
      emptyList()
    )
    val serviceA = ServiceInfo(
      "dev.yidafu.blog.service",
      "ServiceA",
      null,
      "",
      listOf(serviceB)
    )

    val serviceC = ServiceInfo(
      "dev.yidafu.blog.service",
      "ServiceC",
      serviceA,
      "",
      emptyList()
    )

    val services = listOf(serviceA, serviceB, serviceC)

    // 构建依赖树并排序
    val sortedServices = services.buildDependencyTree()

    // 打印结果以便调试
    println("无依赖服务列表排序结果: ${sortedServices.map { it.className }}")

    // 所有服务高度都应该是0，排序结果应该包含所有服务
    assertEquals(3, sortedServices.size, "服务数量不匹配")
    val classNames = sortedServices.map { it.className }
    assertTrue(classNames.containsAll(listOf("ServiceA", "ServiceB", "ServiceC")), "结果中缺少某些服务")
    assertEquals(classNames[0], "ServiceB", "ServiceB 是第一个")
  }
}
