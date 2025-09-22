package dev.yidafu.blog.common.converter

import dev.yidafu.blog.common.db.dao.ConfigurationEntity
import dev.yidafu.blog.common.modal.ConfigurationModal
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper
interface ConfigurationConvertor {
  @Mappings(value = [Mapping(source = "TId", target = "id")])
  fun toModal(data: ConfigurationEntity): ConfigurationModal

  @Mappings(value = [Mapping(source = "TId", target = "id")])
  fun toModalList(dotList: List<ConfigurationEntity>): List<ConfigurationModal>
}
