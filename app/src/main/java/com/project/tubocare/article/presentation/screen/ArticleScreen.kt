package com.project.tubocare.appointment.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.project.tubocare.R
import com.project.tubocare.article.domain.model.Article
import com.project.tubocare.article.presentation.compenent.ArticleCard
import com.project.tubocare.article.presentation.event.ArticleEvent
import com.project.tubocare.article.presentation.state.ArticleState
import com.project.tubocare.ui.theme.ChangeStatusBarColor
import com.project.tubocare.ui.theme.TuboCareTheme
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun ArticleScreen(
    state: ArticleState,
    onEvent: (ArticleEvent) -> Unit,
    navigateToDetail: (Article) -> Unit,
    innerPadding: PaddingValues = PaddingValues(0.dp)
) {
    val pagerState = rememberPagerState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .padding(innerPadding),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.secondary,
                ),
                title = {
                    Text(
                        text = stringResource(id = R.string.title_article),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(10.dp)
                    )
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { paddingvalues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingvalues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalDivider(color = MaterialTheme.colorScheme.background)
            Column(
                modifier = Modifier.padding(10.dp),
            ) {
                HorizontalPager(
                    count = state.articleList.size,
                    state = pagerState,
                ) { page ->
                    ArticleCard(
                        article = state.articleList[page],
                        isLoading = state.isLoading,
                        navigateToDetail = { navigateToDetail(state.articleList[page]) }
                    )
                }
                Spacer(modifier = Modifier.height(40.dp))
                HorizontalPagerIndicator(
                    pagerState = pagerState,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    activeColor = MaterialTheme.colorScheme.secondary,
                    inactiveColor = MaterialTheme.colorScheme.background
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview
@Composable
private fun ArticleScreenPreview() {
    TuboCareTheme {
        ArticleScreen(
            state = ArticleState(
                articleList = listOf(
                    Article(
                        documentId = "",
                        id = 1,
                        title = "Efek Samping Pengobatan Tuberkulosis dan Cara Mengatasinya",
                        content = listOf(
                            "Tuberkulosis (TBC) adalah penyakit menular yang disebabkan oleh bakteri Mycobacterium tuberculosis. Bakteri ini biasanya menyerang paru-paru, namun juga bisa menyerang bagian tubuh lain seperti ginjal, tulang belakang, dan otak. TBC adalah salah satu penyakit menular yang paling mematikan di dunia, tetapi dapat dicegah dan diobati dengan benar.",
                            "",
                            "**Bagaimana Tuberkulosis Menular?** TBC menyebar melalui udara ketika seseorang yang terinfeksi TBC aktif batuk, bersin, atau berbicara. Droplet yang mengandung bakteri TBC bisa terhirup oleh orang lain yang berada di dekatnya, sehingga mereka bisa terinfeksi. Namun, tidak semua orang yang terinfeksi bakteri TBC akan langsung sakit; kondisi ini dikenal sebagai infeksi TBC laten. Orang dengan infeksi TBC laten tidak menularkan penyakit ini dan tidak memiliki gejala.",
                            "",
                            "**Gejala Utama Tuberkulosis Paru:**",
                            "- Batuk yang berlangsung lebih dari tiga minggu",
                            "- Batuk berdarah",
                            "- Nyeri dada",
                            "- Penurunan berat badan tanpa sebab yang jelas",
                            "- Demam",
                            "- Keringat malam",
                            "- Kelelahan",
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
                    Article(
                        documentId = "",
                        id = 1,
                        title = "Efek Samping Pengobatan Tuberkulosis dan Cara Mengatasinya",
                        content = listOf(
                            "Tuberkulosis (TBC) adalah penyakit menular yang disebabkan oleh bakteri Mycobacterium tuberculosis. Bakteri ini biasanya menyerang paru-paru, namun juga bisa menyerang bagian tubuh lain seperti ginjal, tulang belakang, dan otak. TBC adalah salah satu penyakit menular yang paling mematikan di dunia, tetapi dapat dicegah dan diobati dengan benar.",
                            "",
                            "**Bagaimana Tuberkulosis Menular?** TBC menyebar melalui udara ketika seseorang yang terinfeksi TBC aktif batuk, bersin, atau berbicara. Droplet yang mengandung bakteri TBC bisa terhirup oleh orang lain yang berada di dekatnya, sehingga mereka bisa terinfeksi. Namun, tidak semua orang yang terinfeksi bakteri TBC akan langsung sakit; kondisi ini dikenal sebagai infeksi TBC laten. Orang dengan infeksi TBC laten tidak menularkan penyakit ini dan tidak memiliki gejala.",
                            "",
                            "**Gejala Utama Tuberkulosis Paru:**",
                            "- Batuk yang berlangsung lebih dari tiga minggu",
                            "- Batuk berdarah",
                            "- Nyeri dada",
                            "- Penurunan berat badan tanpa sebab yang jelas",
                            "- Demam",
                            "- Keringat malam",
                            "- Kelelahan",
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
                )
            ),
            onEvent = {},
            navigateToDetail = {}
        )
    }
}


/*Column {
              LazyRow {
                  items(state.articleList){ article ->
                      CustomArticleCard(
                          article = article,
                          navigateToDetail = { navigateToDetail(article) }
                      )
                      Spacer(modifier = Modifier.width(16.dp))
                  }
              }
          }*/
/*Column(
    modifier = Modifier
        .padding(horizontal = 10.dp, vertical = 10.dp)
) {
    HorizontalPager(
        state = pagerState,
    ) { page ->
        CustomArticleCard(
            article = article_items[0],
            navigateToDetails = { onClickArticle }
        )

        Spacer(modifier = Modifier.width(40.dp))

        CustomArticleCard(
            article = article_items[1],
            navigateToDetails = { onClickArticle }
        )

        Spacer(modifier = Modifier.width(40.dp))

        CustomArticleCard(
            article = article_items[2],
            navigateToDetails = { onClickArticle }
        )
    }
    Spacer(modifier = Modifier.height(30.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pagerState.pageCount) { iteration ->
            val color =
                if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.background else Color.White
            Box(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .background(color)
                    .height(30.dp)
                    .width(100.dp)
            )
        }
    }
    Spacer(modifier = Modifier.weight(1f))
}*/