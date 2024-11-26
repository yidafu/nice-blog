package dev.yidafu.blog.common.bean.converter

import dev.yidafu.blog.common.bean.dto.ConfigurationDTO
import dev.yidafu.blog.common.modal.ConfigurationModal
import org.mapstruct.Mapper

@Mapper
interface ConfigurationConvertor {
  fun toModal(dto: ConfigurationDTO): ConfigurationModal

  fun toModal(dotList: List<ConfigurationDTO>): List<ConfigurationModal>
}
