package com.project.tubocare.article.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.project.tubocare.article.data.local.ArticleDao
import com.project.tubocare.article.data.local.ArticleEntity
import com.project.tubocare.article.domain.model.Article
import com.project.tubocare.article.domain.repository.ArticleRepository
import com.project.tubocare.auth.util.Constants.ARTICLES_COLLECTION
import com.project.tubocare.core.data.local.AppDatabase
import com.project.tubocare.core.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class ArticleRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val articleDao: ArticleDao
) : ArticleRepository {
    override suspend fun getArticles(): Flow<Resource<List<Article>>> = callbackFlow {
        val localArticles = articleDao.getArticles().map { it.toArticle() }
        trySend(Resource.Success(localArticles))

        val snapshotStateListener = firestore.collection(ARTICLES_COLLECTION)
            .addSnapshotListener { snapshot, error ->
                val response = if (snapshot != null) {
                    val articles =
                        snapshot.documents.map { ArticleEntity.fromDocument(it).toArticle() }
                    val articlesEntity = snapshot.documents.map { document ->
                        ArticleEntity.fromDocument(document)
                    }
                    CoroutineScope(Dispatchers.IO).launch {
                        articleDao.insertArticles(articlesEntity)
                    }
                    Log.d("ArticleRepository", "Received articles: $articles")
                    Resource.Success(articles)
                } else {
                    Resource.Error(error?.message)
                }
                trySend(response)
            }
        awaitClose { snapshotStateListener.remove() }
    }

    override suspend fun getArticleById(id: String): Flow<Resource<Article?>> = flow {
        try {
            val localArticleEntity = articleDao.getArticleById(id)?.toArticle()
            emit(Resource.Success(localArticleEntity))

            val snapshot = firestore.collection(ARTICLES_COLLECTION)
                .document(id)
                .get()
                .await()

            val remoteArticleEntity = ArticleEntity.fromDocument(snapshot)
            val article = remoteArticleEntity.toArticle()
            articleDao.insertArticle(remoteArticleEntity)
            emit(Resource.Success(article))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Terjadi kesalahan yang tidak terduga."))
        }
    }

    override suspend fun updateLocalArticle(articleEntity: ArticleEntity) {
        articleDao.insertArticle(articleEntity)
    }
}