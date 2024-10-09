package com.project.tubocare.notification.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.tubocare.R
import com.project.tubocare.core.util.formatLocalDateTimeToDayMonthYear
import com.project.tubocare.core.util.formatLocalDateTimeToDayMonthYearHourMinute
import com.project.tubocare.core.util.formatLocalTimeToHourMinute
import com.project.tubocare.notification.domain.model.NotificationData
import com.project.tubocare.ui.theme.TuboCareTheme
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Date


@Composable
fun NotificationCard(
    notificationData: NotificationData,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .border(BorderStroke(1.dp, MaterialTheme.colorScheme.background)),
    ) {
        Box(
            modifier = Modifier
                .clip(MaterialTheme.shapes.large)
                .padding(10.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(MaterialTheme.shapes.small)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_notification_stroke),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .weight(1f),
                ) {
                    Text(
                        text = notificationData.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.secondary,
                        maxLines = 1,
                        overflow = TextOverflow.Clip
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = notificationData.desc,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = formatLocalDateTimeToDayMonthYearHourMinute(notificationData.timestamp),
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationCardPreview() {
    TuboCareTheme {
        Column(
            modifier = Modifier.fillMaxSize().padding(20.dp)
        ) {
            NotificationCard(
                notificationData = NotificationData(
                    id = "1",
                    name = "Pengambilan Obat",
                    desc = "Jangan lupa jadwal pengambilan obat pukul 17:00 di Puskesmas Dinoyo",
                    timestamp = LocalDateTime.now()
                )
            )
        }
    }
}