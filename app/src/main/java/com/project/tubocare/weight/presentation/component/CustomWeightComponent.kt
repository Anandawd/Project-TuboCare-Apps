package com.project.tubocare.weight.presentation.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.tubocare.R
import com.project.tubocare.ui.theme.TuboCareTheme

@Composable
fun CustomIMTBox(
    heightTitle: String,
    weightTitle: String,
    heightValue: Int = 0,
    weightValue: Int = 0,
) {
    val weightSuffix = stringResource(id = R.string.weight_suffix)
    val heightSuffix = stringResource(id = R.string.height_suffix)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(MaterialTheme.shapes.small)
            .border(
                color = MaterialTheme.colorScheme.secondary,
                width = 1.dp,
                shape = MaterialTheme.shapes.small
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = buildAnnotatedString {
                    append(heightTitle)
                    if (heightValue != 0) {
                        withStyle(
                            style = SpanStyle(fontWeight = FontWeight.Bold)
                        ) {
                            append("$heightValue $heightSuffix")
                        }
                    } else {
                        withStyle(
                            style = SpanStyle(color = MaterialTheme.colorScheme.secondary)
                        ) {
                            append(" - ")
                        }
                    }
                },
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary
            )
            VerticalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.background
            )
            Text(
                text = buildAnnotatedString {
                    append(weightTitle)
                    if (weightValue != 0) {
                        withStyle(
                            style = SpanStyle(fontWeight = FontWeight.Bold)
                        ) {
                            append("$weightValue $weightSuffix")
                        }
                    } else {
                        withStyle(
                            style = SpanStyle(color = MaterialTheme.colorScheme.secondary)
                        ){
                            append(" - ")
                        }
                    }
                },
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun CustomIMTBoxPreview() {
    TuboCareTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            CustomIMTBox(
                heightTitle = stringResource(id = R.string.height_box_title),
                weightTitle = stringResource(id = R.string.weight_box_title),
                heightValue = 0,
                weightValue = 60
            )
        }

    }
}