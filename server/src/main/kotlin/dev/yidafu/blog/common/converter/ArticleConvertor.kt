package dev.yidafu.blog.common.converter

import dev.yidafu.blog.common.modal.ArticleModel
import dev.yidafu.blog.common.vo.ArticleVO
import org.mapstruct.Mapper

@Mapper
interface ArticleConvertor {
  fun toVO(modal: ArticleModel): ArticleVO

  fun toVO(modalList: List<ArticleModel>): List<ArticleVO>
}
