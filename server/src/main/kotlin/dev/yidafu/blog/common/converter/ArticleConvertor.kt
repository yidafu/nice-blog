package dev.yidafu.blog.common.converter

import dev.yidafu.blog.common.dao.tables.records.BArticleRecord
import dev.yidafu.blog.common.modal.ArticleModel
import dev.yidafu.blog.common.vo.AdminArticleDetailVO
import dev.yidafu.blog.common.vo.ArticleVO
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget

@Mapper
interface ArticleConvertor {
  fun toVO(modal: ArticleModel): ArticleVO

  fun toVO(modalList: List<ArticleModel>): List<ArticleVO>

  fun toAdminVo(modal: ArticleModel): AdminArticleDetailVO

  fun recordToModal(record: BArticleRecord?): ArticleModel?

  fun recordToModal(records: List<BArticleRecord>): List<ArticleModel>

  fun mapToRecord(
    articleModel: ArticleModel?,
    @MappingTarget record: BArticleRecord?,
  )
}
