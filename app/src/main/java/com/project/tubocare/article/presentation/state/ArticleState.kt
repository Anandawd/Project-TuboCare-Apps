package com.project.tubocare.article.presentation.state

import com.project.tubocare.article.domain.model.Article
import com.project.tubocare.core.util.Resource

data class ArticleState(
    val articleList: List<Article> = emptyList(),

    val documentId: String = "",
    val id: Int = 0,
    val title: String = "",
    val content: List<String> = emptyList(),
    val imageUrl: String = "",
    val imagePath: String? = null,

    val isLoading: Boolean = false,
    val isSuccessGetDetail: Boolean = false,

    val errorGetDetail: String? = null,
    val selectionArticle: Article? = null
)
