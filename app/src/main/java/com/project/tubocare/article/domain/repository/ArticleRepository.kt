package com.project.tubocare.article.domain.repository

import com.project.tubocare.article.data.local.ArticleEntity
import com.project.tubocare.article.domain.model.Article
import com.project.tubocare.core.util.Resource
import kotlinx.coroutines.flow.Flow

interface ArticleRepository {
    suspend fun getArticles(): Flow<Resource<List<Article>>>
    suspend fun getArticleById(id: String) : Flow<Resource<Article?>>
    suspend fun updateLocalArticle(articleEntity: ArticleEntity)
}
