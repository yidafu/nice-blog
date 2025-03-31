package dev.yidafu.blog.common.modal

import dev.yidafu.blog.common.json.LocalDateTimeSerializer
import jakarta.persistence.*
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Entity
@Table(
  name = "b_access_log",
//  indexes = [Index(name = "idx_combo", columnList = "source_url, access_time")],
)
@Serializable
class AccessLogModal(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null,
  @Column(name = "uid", nullable = false, length = 16)
  var uid: String,
  @Column(
    name = "access_time",
    columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP",
    insertable = false,
    updatable = false,
  )
  @Serializable(with = LocalDateTimeSerializer::class)
  val accessTime: LocalDateTime? = null,
  @Column(name = "source_url", nullable = false, length = 512)
  var sourceUrl: String,
  @Column(name = "referrer_url", length = 512)
  var referrerUrl: String? = null,
  @Column(name = "ip", length = 64)
  var ip: String? = null,
  @Column(name = "ua", length = 64)
  var ua: String? = null,
)
