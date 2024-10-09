package com.project.tubocare.article.presentation.event

import com.project.tubocare.article.domain.model.Article

sealed class ArticleEvent {
    object GetArticles: ArticleEvent()
    object ResetStatus: ArticleEvent()
    object ClearToast: ArticleEvent()
    data class GetArticleById(val value: String): ArticleEvent()

    data class SelectArticle(val data: Article): ArticleEvent()
}