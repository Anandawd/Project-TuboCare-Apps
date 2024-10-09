package com.project.tubocare.article.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.firebase.firestore.DocumentSnapshot
import com.project.tubocare.article.domain.model.Article
import com.project.tubocare.core.data.local.Converters

@Entity(tableName = "articles")
@TypeConverters(Converters::class)
data class ArticleEntity(
    @PrimaryKey val documentId: String,
    val id: Int,
    val title: String,
    var content: List<String>,
    val imagePath: String,
    var localImagePath: String? = null
) {
    fun toArticle(): Article {
        return Article(
            documentId = documentId,
            id = id,
            title = title,
            content = content,
            imageUrl = imagePath,
            localImagePath = localImagePath
        )
    }

    companion object {
        fun fromDocument(document: DocumentSnapshot): ArticleEntity {
            val id = document.getLong("id")?.toInt() ?: 0
            val title = document.getString("title") ?: ""
            val imageUrl = document.getString("imageUrl") ?: ""
            val contentField = document.get("content")

            val content = when (contentField) {
                is String -> listOf(contentField)
                is List<*> -> contentField.filterIsInstance<String>()
                else -> emptyList()
            }

            return ArticleEntity(
                documentId = document.id,
                id = id,
                title = title,
                content = content,
                imagePath = imageUrl,
                localImagePath = null
            )
        }
    }
}