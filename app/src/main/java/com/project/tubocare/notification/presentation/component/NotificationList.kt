package com.project.tubocare.notification.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.tubocare.R
import com.project.tubocare.core.util.formatLocalDateToDayMonthYear
import com.project.tubocare.notification.domain.model.NotificationData
import com.project.tubocare.ui.theme.CustomGrayBlue
import com.project.tubocare.ui.theme.TuboCareTheme
import java.time.LocalDateTime

@Composable
fun NotificationList(
    notificationDataList: List<NotificationData>,
) {

    val groupedNotification = notificationDataList
        .sortedByDescending { it.timestamp }
        .groupBy { it.timestamp.toLocalDate() }

    Column {
        if (notificationDataList.isEmpty()) {
            EmptyNotificationScreen()
        }

        LazyColumn(
            modifier = Modifier,
        ) {
            groupedNotification.forEach { (date, notifications) ->
                item {
                    Text(
                        text = formatLocalDateToDayMonthYear(date),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(6.dp)
                    )
                }
                items(notifications){ notification ->
                    NotificationCard(
                        notificationData = notification
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyNotificationScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painterResource(id = R.drawable.img_404_2),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Belum ada notifikasi",
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.offset(y= (-50).dp)
        )
        Spacer(modifier = Modifier.height(4.dp).offset(y= (-50).dp))
        Text(
            text = "Tambahkan jadwal minum obat dan\njadwal kontrol untuk mendapatkan notifikasi",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.offset(y= (-50).dp)
        )
        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationListPreview() {
    TuboCareTheme {
        Column(Modifier.fillMaxSize().padding(20.dp)) {
            NotificationList(
                notificationDataList = emptyList(),
            )
        }
    }
}