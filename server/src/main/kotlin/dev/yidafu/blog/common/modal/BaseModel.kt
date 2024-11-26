package dev.yidafu.blog.common.modal

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.sql.Date


  @MappedSuperclass
  open class BaseModel {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    val id: Long? = null

    @CreationTimestamp
    @Column(name = "created_at")
    val createdAt: Date? = null

    @UpdateTimestamp
    @Column(name = "updated_at")
    val updatedAt: Date? = null
  }

