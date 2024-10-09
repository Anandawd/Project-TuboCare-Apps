package com.project.tubocare.article.presentation.compenent

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.project.tubocare.article.domain.model.Article

@Composable
fun ArticleList(
    modifier: Modifier = Modifier,
    articles: List<Article>,
    onClick: (Article) -> Unit
) {

}