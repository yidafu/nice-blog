package dev.yidafu.blog.common.converter

import dev.yidafu.blog.common.db.dao.SyncTaskEntity
import dev.yidafu.blog.common.dto.SyncTaskDTO
import dev.yidafu.blog.common.vo.AdminSyncTaskVO
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper
interface SyncTaskConvertor {
  @Mappings(value = [Mapping(source = "TId", target = "id")])
  fun toDTO(modal: SyncTaskEntity): SyncTaskDTO

  @Mappings(value = [Mapping(source = "TId", target = "id")])
  fun toVO(modal: SyncTaskEntity): AdminSyncTaskVO
//  @Mappings(value = [Mapping(source = "TId", target = "id")])
  fun toVOList(modal: List<SyncTaskEntity>): List<AdminSyncTaskVO>
}
