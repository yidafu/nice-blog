package dev.yidafu.blog.common.modal

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.sql.Date
import java.time.LocalDate
import java.time.LocalTime


@MappedSuperclass
  open class BaseModel {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    var id: Long? = null

    @CreationTimestamp
    @Column(name = "created_at")
    var createdAt: LocalTime? = null

    @UpdateTimestamp
    @Column(name = "updated_at")
    var updatedAt: LocalTime? = null
  }

