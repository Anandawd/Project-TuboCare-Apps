package com.project.tubocare.article.presentation.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.project.tubocare.R
import com.project.tubocare.article.domain.model.Article
import com.project.tubocare.article.presentation.event.ArticleEvent
import com.project.tubocare.article.presentation.state.ArticleState
import com.project.tubocare.ui.theme.TuboCareTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalLayoutApi::class
)
@Composable
fun ArticleDetailScreen(
    id: String,
    state: ArticleState,
    onEvent: (ArticleEvent) -> Unit,
    navigateToHome: () -> Unit
) {

    LaunchedEffect(Unit) {
        onEvent(ArticleEvent.GetArticleById(id))
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Scaffold(
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            containerColor = MaterialTheme.colorScheme.surface,
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.secondary,
                    ),
                    title = {
                        Text(
                            text = stringResource(id = R.string.title_article_detail),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(10.dp)
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navigateToHome() }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow_back),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary,
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior,
                )
            },
        ) { paddingvalues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(paddingvalues),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.background
                )
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = state.title,
                        style = MaterialTheme.typography.headlineLarge.copy(fontSize = 16.sp),
                        color = MaterialTheme.colorScheme.secondary,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(MaterialTheme.shapes.medium)
                    ) {
                        if (state.imageUrl.isBlank()) {
                            Image(
                                painter = painterResource(id = R.drawable.image_sample_article),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        AsyncImage(
                            model = state.imagePath ?: state.imageUrl,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    FlowRow(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        state.content.forEach { paragraph ->
                            if (paragraph.startsWith("***") && paragraph.endsWith("***")){
                                Text(
                                    text = paragraph.trimStart('*').trimEnd('*'),
                                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 14.sp),
                                    color = MaterialTheme.colorScheme.secondary,
                                    textAlign = TextAlign.Justify,
                                    lineHeight = 24.sp,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            } else if (paragraph.startsWith("**") && paragraph.endsWith("**")){
                                Text(
                                    text = paragraph.trimStart('*').trimEnd('*'),
                                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 16.sp),
                                    color = MaterialTheme.colorScheme.secondary,
                                    textAlign = TextAlign.Justify,
                                    lineHeight = 24.sp,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            } else if (paragraph.startsWith("     • ")){
                                Text(
                                    text = paragraph,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.secondary,
                                    textAlign = TextAlign.Justify,
                                    lineHeight = 24.sp,
                                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                                )
                            } else {
                                Text(
                                    text = paragraph,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.secondary,
                                    textAlign = TextAlign.Justify,
                                    lineHeight = 24.sp,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ArticleDetailScreenPreview() {
    TuboCareTheme {
        ArticleDetailScreen(
            id = "",
            state = ArticleState(
                documentId = "",
                id = 1,
                title = "Efek Samping Pengobatan Tuberkulosis dan Cara Mengatasinya",
                content = listOf(
                    "Tuberkulosis (TBC) adalah penyakit menular yang disebabkan oleh bakteri Mycobacterium tuberculosis. Bakteri ini biasanya menyerang paru-paru, namun juga bisa menyerang bagian tubuh lain seperti ginjal, tulang belakang, dan otak. TBC adalah salah satu penyakit menular yang paling mematikan di dunia, tetapi dapat dicegah dan diobati dengan benar.",
                    "",
                    "***Bagaimana Tuberkulosis Menular?***",
                    "",
                    "**Gejala Utama Tuberkulosis Paru:**",
                    "",
                    "     • Batuk yang berlangsung lebih dari tiga minggu",
                    "",
                    "     • Batuk berdarah",
                    "     • Nyeri dada",
                    "     • Penurunan berat badan tanpa sebab yang jelas",
                    "     • Demam",
                    "     • Keringat malam",
                    "     • Kelelahan",
                    "",
                    "**Diagnosis dan Pengobatan:** Untuk mendiagnosis TBC, biasanya dilakukan tes kulit tuberkulin atau tes darah untuk mendeteksi infeksi. Pemeriksaan lebih lanjut seperti rontgen dada dan tes dahak dapat dilakukan untuk memastikan diagnosis. Pengobatan TBC melibatkan pemberian kombinasi antibiotik selama setidaknya 6 bulan. Sangat penting untuk mengikuti pengobatan sesuai anjuran dokter untuk memastikan bahwa bakteri benar-benar dibasmi dan untuk mencegah resistensi antibiotik.",
                    "",
                    "**Pencegahan Tuberkulosis:** Pencegahan TBC dapat dilakukan dengan beberapa cara:",
                    "- Vaksinasi BCG (Bacillus Calmette-Guérin) terutama diberikan kepada bayi di daerah dengan prevalensi TBC tinggi.",
                    "- Identifikasi dan pengobatan infeksi TBC laten untuk mencegah perkembangan menjadi TBC aktif.",
                    "- Meningkatkan ventilasi dan sirkulasi udara di tempat-tempat ramai.",
                    "- Menerapkan etika batuk dan kebersihan diri."
                ),
                imageUrl = ""
            ),
            onEvent = {},
            navigateToHome = {}
        )
    }
}