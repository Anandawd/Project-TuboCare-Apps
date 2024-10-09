package com.project.tubocare.weight.presentation.component.circural

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.tubocare.R
import com.project.tubocare.ui.theme.CustomBlue
import com.project.tubocare.ui.theme.CustomOrange
import com.project.tubocare.ui.theme.CustomRed
import com.project.tubocare.ui.theme.CustomSoftBlue
import com.project.tubocare.ui.theme.TuboCareTheme

@Composable
fun CustomCircularComponent(
    canvasSize: Dp = 180.dp,
    indicatorValue: Float,
    maxIndicatorValue: Float = 40f,
    backgroundIndicatorColor: Color = MaterialTheme.colorScheme.background,
    backgroundIndicatorStrokeWidth: Float = 50f,
    foregroundIndicatorColor: Color = MaterialTheme.colorScheme.primary,
    foregroundIndicatorStrokeWidth: Float = 50f
) {

    var allowedIndicatorValue by remember {
        mutableStateOf(maxIndicatorValue)
    }

    allowedIndicatorValue = if (indicatorValue <= maxIndicatorValue) {
        indicatorValue
    } else {
        maxIndicatorValue
    }

    var animatedIndicatorValue by remember { mutableStateOf(0f) }
    LaunchedEffect(key1 = allowedIndicatorValue) {
        animatedIndicatorValue = allowedIndicatorValue
    }

    val percentage = (animatedIndicatorValue / maxIndicatorValue) * 100

    val sweepAngle by animateFloatAsState(
        targetValue = (2.0 * percentage).toFloat(),
        animationSpec = tween(1000)
    )

    val receivedValue by animateFloatAsState(
        targetValue = allowedIndicatorValue,
        animationSpec = tween(1000)
    )

    val animatedTextColor by animateColorAsState(
        targetValue = if (allowedIndicatorValue == 0f) {
            MaterialTheme.colorScheme.background
        } else {
            MaterialTheme.colorScheme.secondary
        },
        animationSpec = tween(1000)
    )

    val animatedForegroundBackgroundColor by animateColorAsState(
        targetValue = when {
            indicatorValue < 18.5f -> CustomOrange
            indicatorValue in 18.5f..24.9f -> CustomBlue
            indicatorValue in 25.0f..29.9f -> CustomOrange
            indicatorValue > 30.0f -> CustomRed
            else -> CustomSoftBlue
        },
        animationSpec = tween(1000)
    )

    val textCategoryState = when {
        indicatorValue < 0.1f -> "Memuat"
        indicatorValue in 0.1f..18.5f -> "Kurang"
        indicatorValue in 18.5f..24.9f -> "Normal"
        indicatorValue in 25.0f..29.9f -> "Berlebih"
        indicatorValue > 30.0f -> "Obesitas"
        else -> "Memuat"
    }

    val animatedCategoryBackgroundColor by animateColorAsState(
        targetValue = when {
            indicatorValue < 0.1f -> CustomSoftBlue
            indicatorValue in 0.1f..18.5f -> CustomOrange
            indicatorValue in 18.5f..24.9f -> CustomBlue
            indicatorValue in 25.0f..29.9f -> CustomOrange
            indicatorValue > 30.0f -> CustomRed
            else -> CustomSoftBlue
        },
        animationSpec = tween(1000)
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .size(canvasSize)
                .drawBehind {
                    val componentSize = size / 1.25f
                    backgroundIndicator(
                        componentSize = componentSize,
                        indicatorColor = backgroundIndicatorColor,
                        indicatorStrokeWidth = backgroundIndicatorStrokeWidth
                    )
                    foregroundIndicator(
                        sweepAngle = sweepAngle,
                        componentSize = componentSize,
                        indicatorColor = animatedForegroundBackgroundColor,
                        indicatorStrokeWidth = foregroundIndicatorStrokeWidth
                    )
                },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            EmbeddedTextElement(
                textValue = receivedValue,
                textColor = animatedTextColor
            )
            Spacer(modifier = Modifier.height(10.dp))
            CategoryElement(
                textValue = textCategoryState,
                backgroundColor = animatedCategoryBackgroundColor,
                canvasSize = canvasSize,
                modifier = Modifier
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomCircularComponentPreview() {
    TuboCareTheme {
        CustomCircularComponent(
            indicatorValue = 18f,
            maxIndicatorValue = 30f,
        )
    }
}

@Composable
fun EmbeddedTextElement(
    textValue: Float,
    textColor: Color,
) {
    Text(
        text = String.format("%.1f", textValue),
        style = MaterialTheme.typography.headlineLarge,
        color = textColor,
        textAlign = TextAlign.Center
    )

}

@Composable
fun CategoryElement(
    textValue: String,
    backgroundColor: Color,
    canvasSize: Dp,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .size(height = 30.dp, width = canvasSize / 1.25f)
            .clip(MaterialTheme.shapes.extraSmall)
            .background(color = backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = textValue,
            style = MaterialTheme.typography.bodySmall,
            fontSize = 12.sp,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CategoryElementPreview() {
    TuboCareTheme {
        CategoryElement(
            textValue = stringResource(id = R.string.imt_normal),
            backgroundColor = MaterialTheme.colorScheme.primary,
            canvasSize = 160.dp,
            modifier = Modifier
        )
    }
}


fun DrawScope.backgroundIndicator(
    componentSize: Size,
    indicatorColor: Color,
    indicatorStrokeWidth: Float
) {
    drawArc(
        size = componentSize,
        color = indicatorColor,
        startAngle = 170f,
        sweepAngle = 200f,
        useCenter = false,
        style = Stroke(
            width = indicatorStrokeWidth,
            cap = StrokeCap.Round
        ),
        topLeft = Offset(
            x = (size.width - componentSize.width) / 2f,
            y = (size.height - componentSize.height) / 2f
        )
    )

}

fun DrawScope.foregroundIndicator(
    sweepAngle: Float,
    componentSize: Size,
    indicatorColor: Color,
    indicatorStrokeWidth: Float
) {
    drawArc(
        size = componentSize,
        color = indicatorColor,
        startAngle = 170f,
        sweepAngle = sweepAngle,
        useCenter = false,
        style = Stroke(
            width = indicatorStrokeWidth,
            cap = StrokeCap.Round
        ),
        topLeft = Offset(
            x = (size.width - componentSize.width) / 2f,
            y = (size.height - componentSize.height) / 2f
        )
    )

}