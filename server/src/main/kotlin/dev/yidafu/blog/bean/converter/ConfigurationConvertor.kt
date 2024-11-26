package dev.yidafu.blog.bean.converter

import dev.yidafu.blog.bean.dto.ConfigurationDTO
import dev.yidafu.blog.modal.ConfigurationModal
import org.mapstruct.Mapper

@Mapper
interface ConfigurationConvertor {
  fun toModal(dto: ConfigurationDTO): ConfigurationModal

  fun toModal(dotList: List<ConfigurationDTO>): List<ConfigurationModal>
}
