package com.project.tubocare.article.domain.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.project.tubocare.R
import com.project.tubocare.article.data.local.ArticleEntity

data class Article(
    val documentId: String,
    val id: Int,
    val title: String,
    val content: List<String>,
    val imageUrl: String,
    var localImagePath: String? = null
){
    fun toArticleEntity(): ArticleEntity {
        return ArticleEntity(
            documentId = documentId,
            id = id,
            title = title,
            content = content,
            imagePath = imageUrl,
            localImagePath = localImagePath
        )
    }
}
