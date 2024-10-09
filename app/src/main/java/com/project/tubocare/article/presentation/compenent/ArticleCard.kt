package com.project.tubocare.article.presentation.compenent

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.project.tubocare.R
import com.project.tubocare.article.domain.model.Article
import com.project.tubocare.core.presentation.components.CustomPrimaryButton
import com.project.tubocare.ui.theme.TuboCareTheme

@Composable
fun ArticleCard(
    article: Article,
    isLoading: Boolean = false,
    navigateToDetail: (Article) -> Unit
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = article.title,
                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 16.sp),
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(MaterialTheme.shapes.medium)
            ) {
                if (article.imageUrl.isBlank()) {
                    Image(
                        painter = painterResource(id = R.drawable.image_sample_article),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                AsyncImage(
                    model = article.localImagePath ?: article.imageUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Column(modifier = Modifier.height(120.dp)) {
                article.content.take(1).forEach { paragraph ->
                    Text(
                        text = paragraph,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary,
                        lineHeight = 20.sp,
                        textAlign = TextAlign.Justify,
                        maxLines = 6,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            CustomPrimaryButton(
                value = stringResource(id = R.string.btn_baca_selengkap),
                onClick = { navigateToDetail(article) },
                isLoading = isLoading
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomArticleCardPreview() {
    TuboCareTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            ArticleCard(
                article = Article(
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
                isLoading = false,
                navigateToDetail = {}
            )
        }
    }
}