package dev.yidafu.blog.common.converter

import dev.yidafu.blog.common.db.dao.ArticleEntity
import dev.yidafu.blog.common.vo.AdminArticleDetailVO
import dev.yidafu.blog.common.vo.ArticleVO
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper
interface ArticleConvertor {
  @Mappings(value = [Mapping(source = "TId", target = "id")])
  fun toVO(modal: ArticleEntity): ArticleVO

  @Mappings(value = [Mapping(source = "TId", target = "id")])
  fun toVO(modalList: List<ArticleEntity>): List<ArticleVO>

  @Mappings(value = [Mapping(source = "TId", target = "id")])
  fun toAdminVo(modal: ArticleEntity): AdminArticleDetailVO
}
