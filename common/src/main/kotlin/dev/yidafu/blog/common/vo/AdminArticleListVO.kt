package dev.yidafu.blog.common.vo

import dev.yidafu.blog.common.modal.ArticleModel

class AdminArticleListVO(
  page: Int,
  pageSize: Int,
  total: Int,
  list: List<ArticleModel>,
) : PaginationVO<ArticleModel>(page, pageSize, total, list)
