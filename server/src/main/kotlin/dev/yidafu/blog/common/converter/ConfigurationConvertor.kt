package dev.yidafu.blog.common.converter

import dev.yidafu.blog.common.dao.tables.records.BConfigurationRecord
import dev.yidafu.blog.common.dto.ConfigurationDTO
import dev.yidafu.blog.common.modal.ConfigurationModal
import org.mapstruct.Mapper

@Mapper
interface ConfigurationConvertor {
  fun toModal(dto: ConfigurationDTO): ConfigurationModal

  fun toModal(dotList: List<ConfigurationDTO>): List<ConfigurationModal>

  fun recordToModal(recordList: List<BConfigurationRecord>): List<ConfigurationModal>

  fun recordToModal(record: BConfigurationRecord?): ConfigurationModal
}
