-keep class dev.yidafu.blog.** { *; }

# Vert.x 核心类
-keep class io.vertx.core.** { *; }
-keep class io.vertx.ext.web.** { *; }
-keep class io.vertx.ext.auth.** { *; }

# Vert.x SPI 和 Verticle 类
-keep class io.vertx.core.spi.** { *; }
-keep class * implements io.vertx.core.spi.VerticleFactory { *; }
-keep @io.vertx.core.spi.Verticle class * { *; }

# 反射相关类
-keep class io.vertx.core.impl.** { *; }
-keep class io.vertx.core.json.** { *; }

# Netty 相关类
-keep class io.netty.** { *; }
-keep class io.netty.channel.** { *; }
-keep class io.netty.handler.** { *; }
-keep class io.netty.buffer.** { *; }

# 日志相关类
-keep class org.slf4j.** { *; }
-keep class ch.qos.logback.** { *; }

# 事件总线相关类
-keep class io.vertx.core.eventbus.** { *; }
-keep class io.vertx.core.eventbus.impl.** { *; }

# HTTP 相关类
-keep class io.vertx.core.http.** { *; }
-keep class io.vertx.ext.web.** { *; }

# 集群相关类
-keep class com.hazelcast.** { *; }
-keep class io.vertx.spi.cluster.hazelcast.** { *; }


# 通用规则
-keepattributes *Annotation*,Signature,SourceFile,LineNumberTable
-keep class java.io.Serializable { *; }
-keep class java.lang.Comparable { *; }
