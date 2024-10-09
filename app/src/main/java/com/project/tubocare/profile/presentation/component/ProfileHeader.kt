package com.project.tubocare.profile.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.project.tubocare.R
import com.project.tubocare.profile.presentation.state.ProfileState

@Composable
fun ProfileHeader(state: ProfileState, age: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(
                RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomEnd = 30.dp,
                    bottomStart = 30.dp
                )
            )
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileImage(state)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = state.name,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = if (age != 0) "$age Tahun" else "",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White
            )
        }
    }
}

@Composable
fun ProfileImage(state: ProfileState) {
    when {
        state.localImagePath.isNullOrBlank() && state.gender == "Laki-laki" -> {
            Image(
                painter = painterResource(id = R.drawable.avatar_man),
                contentDescription = "photo profile",
                modifier = Modifier.size(100.dp)
            )
        }
        state.localImagePath.isNullOrBlank() && state.gender == "Perempuan" -> {
            Image(
                painter = painterResource(id = R.drawable.avatar_girl),
                contentDescription = "photo profile",
                modifier = Modifier.size(100.dp)
            )
        }
        else -> {
            AsyncImage(
                model = state.localImagePath ?: state.imageUrl,
                contentDescription = "photo profile",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }
    }
}