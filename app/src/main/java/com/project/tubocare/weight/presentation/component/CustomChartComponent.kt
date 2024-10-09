/*
package com.project.tubocare.weight.presentation.component

import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madrapps.plot.line.DataPoint
import com.madrapps.plot.line.LineGraph
import com.madrapps.plot.line.LinePlot
import com.project.tubocare.ui.theme.CustomBlue
import com.project.tubocare.ui.theme.CustomDarkBlue
import com.project.tubocare.ui.theme.TuboCareTheme
import com.project.tubocare.weight.presentation.component.chart.DataPointXLine
import com.project.tubocare.weight.presentation.component.chart.DataPoints
import com.project.tubocare.weight.presentation.component.chart.RoundRectangle
import com.project.tubocare.weight.presentation.component.chart.toPx
import java.text.DecimalFormat

*//*

*/
/*@Preview
@Composable
fun CustomLineChart() {
    val steps = 5
    val pointsData = listOf(
        Point(0f,40f ),
        Point(0f,70f ),
        Point(2f,30f ),
        Point(3f,60f ),
        Point(4f,70f ),
    )

    val xAxisData = AxisData.Builder()
        .build()
}*//*
*/
/*


@Composable
fun SampleLineGraph(lines: List<List<DataPoint>>) {
    val pointValues = remember { mutableStateListOf<DataPointXLine>() }
    LineGraph(
        plot = LinePlot(
            listOf(
                LinePlot.Line(
                    lines[0],
                    LinePlot.Connection(
                        color = MaterialTheme.colorScheme.secondary,
                        strokeWidth = 2.dp
                    ),
                    LinePlot.Intersection(
                        color = MaterialTheme.colorScheme.secondary,
                        radius = 6.dp
                    ),
                    LinePlot.Highlight(
                        color = MaterialTheme.colorScheme.primary,
                        radius = 1.dp
                    ),

                    )
            ),
            grid = LinePlot.Grid(
                color = MaterialTheme.colorScheme.secondary,
                steps = 3,
                lineWidth = 1.dp,
            ),
        ), modifier = Modifier
            .height(200.dp),
        onSelectionStart = {},
        onSelectionEnd = { pointValues.clear() },
        onSelection = { xLine, points ->
            pointValues.clear()
            points.forEach {
                pointValues.add(
                    DataPointXLine(
                        xPos = xLine,
                        yPos = it.y,
                        value = it.y
                    )
                )
            }
        }
    )
}

@Preview
@Composable
private fun SampleLineGraphPreview() {
    TuboCareTheme {
        val dataPoints1 = listOf(
            DataPoint(0f, 0f),
            DataPoint(1f, 20f),
            DataPoint(2f, 50f),
            DataPoint(3f, 10f),
            DataPoint(4f, 0f),
        )

        SampleLineGraph(listOf(dataPoints1))
    }
}

@Composable
internal fun LineGraph1(lines: List<List<DataPoint>>) {
    LineGraph(
        plot = LinePlot(
            listOf(
                LinePlot.Line(
                    lines[0],
                    LinePlot.Connection(CustomDarkBlue, 2.dp),
                    LinePlot.Intersection(CustomDarkBlue, 5.dp),
                    LinePlot.Highlight(Color.Yellow, 5.dp),
                    LinePlot.AreaUnderLine(Color.Red, 0.3f)
                ),
                LinePlot.Line(
                    lines[1],
                    LinePlot.Connection(Color.Gray, 2.dp),
                    LinePlot.Intersection { center, _ ->
                        val px = 2.dp.toPx()
                        val topLeft = Offset(center.x - px, center.y - px)
                        drawRect(Color.Gray, topLeft, Size(px * 2, px * 2))
                    },
                ),
            ),
            selection = LinePlot.Selection(
                highlight = LinePlot.Connection(
                    Color.Yellow,
                    strokeWidth = 2.dp,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(40f, 20f))
                )
            ),
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun LineGraph1Preview() {
    TuboCareTheme {
        LineGraph1(listOf(DataPoints.dataPoints1, DataPoints.dataPoints2))
    }
}

@Composable
internal fun LineGraph2(lines: List<List<DataPoint>>) {
    LineGraph(
        plot = LinePlot(
            listOf(
                LinePlot.Line(
                    lines[1],
                    LinePlot.Connection(Color.Gray, 2.dp),
                    null,
                    LinePlot.Highlight { center ->
                        val color = Color.Gray
                        drawCircle(color, 9.dp.toPx(), center, alpha = 0.3f)
                        drawCircle(color, 6.dp.toPx(), center)
                        drawCircle(Color.White, 3.dp.toPx(), center)
                    },
                ),
                LinePlot.Line(
                    lines[0],
                    LinePlot.Connection(Color.Blue, 3.dp),
                    LinePlot.Intersection(Color.Blue, 6.dp) { center, point ->
                        val x = point.x
                        val rad = if (x % 4f == 0f) 6.dp else 3.dp
                        drawCircle(
                            Color.Blue,
                            rad.toPx(),
                            center,
                        )
                    },
                    LinePlot.Highlight { center ->
                        val color = Color.Blue
                        drawCircle(color, 9.dp.toPx(), center, alpha = 0.3f)
                        drawCircle(color, 6.dp.toPx(), center)
                        drawCircle(Color.White, 3.dp.toPx(), center)
                    },
                    LinePlot.AreaUnderLine(Color.Blue, 0.1f)
                ),
            ), LinePlot.Grid(Color.Gray), paddingRight = 16.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}


@Preview(showBackground = true)
@Composable
fun LineGraph2Preview() {
    TuboCareTheme {
        LineGraph2(listOf(DataPoints.dataPoints1, DataPoints.dataPoints2))
    }
}

@Composable
internal fun LineGraph5(lines: List<List<DataPoint>>) {
    LineGraph(
        plot = LinePlot(
            listOf(
                LinePlot.Line(
                    lines[0],
                    LinePlot.Connection(color = CustomDarkBlue),
                    LinePlot.Intersection(color = CustomDarkBlue),
                    LinePlot.Highlight(color = CustomBlue),
                )
            ),
            horizontalExtraSpace = 10.dp,
            xAxis = LinePlot.XAxis(unit = 1f, roundToInt = false),
            yAxis = LinePlot.YAxis(steps = 4, roundToInt = false),
            grid = LinePlot.Grid(CustomDarkBlue, steps = 4),
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun LineGraph5Preview() {
    TuboCareTheme {
        LineGraph5(listOf(DataPoints.dataPoints1, DataPoints.dataPoints2))
    }
}

*//*

@Composable
fun ChartViewLayout(
    item: DataPoint
) {
    val dataPoints = listOf(
        DataPoint(0f, 0f),
        DataPoint(1f, 20f),
        DataPoint(2f, 50f),
        DataPoint(3f, 10f),
        DataPoint(4f, 0f),
    )
    val linePlotLines = remember { mutableStateListOf<LinePlot.Line>() }
    val pointValues = remember { mutableStateListOf<DataPointXLine>() }
    val recompose = remember { mutableStateOf(0L) }
    val context = LocalContext.current

    // paint
    val textPaint = remember {
        mutableStateOf(android.graphics.Paint())
    }

    // Handle data set
    LaunchedEffect(key1 = dataPoints) {
        linePlotLines.clear()
        val dataPoints = arrayListOf<DataPoint>()
        var xPos = 0f
        val list = serialize.deSerializeList(datasetString)
        val numberList = arrayListOf<Float>()
        list.forEach { anyData ->
            val dataValue = anyData.toString().toFloatOrNull()
            if (dataValue != null) {
                numberList.add(dataValue)
                dataPoints.add(DataPoint(x = xPos, y = dataValue))

                // calculate animated xPos
                xPos += 1f
            }
        }

        if (dataPoints.isNotEmpty()) {
            linePlotLines.add(
                LinePlot.Line(
                    dataPoints,
                    LinePlot.Connection(
                        color = CustomDarkBlue,
                        strokeWidth = 2.dp
                    ),
                    LinePlot.Intersection(
                        color = CustomDarkBlue,
                        radius = 6.dp
                    ),
                    LinePlot.Highlight(
                        color = CustomBlue,
                        radius = 1.dp
                    ),
                )
            )
        }

        textPaint.value = Paint().asFrameworkPaint().apply {
            isAntiAlias = true
            textSize = txtSize
            textAlign = android.graphics.Paint.Align.CENTER
            typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL)
        }

        recompose.value++
    }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(1.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        if (linePlotLines.isNotEmpty()){
            LineGraph(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp),
                plot = LinePlot(
                    lines = linePlotLines,
                    grid = LinePlot.Grid(MaterialTheme.colorScheme.secondary, steps = 4),
                    selection = LinePlot.Selection(
                        enabled = true,
                        highlight = LinePlot.Connection(
                            color = Color.Yellow,
                            strokeWidth = 1.dp,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(40f, 20f))
                        ),
                    ),
                    isZoomAllowed = false
                ),
                onSelectionStart = {},
                onSelectionEnd = { pointValues.clear() },
                onSelection = { xLine, points ->
                    pointValues.clear()
                    points.forEach {
                        pointValues.add(DataPointXLine(
                            xPos = xLine,
                            yPos = it.y,
                            value = it.y
                            )
                        )
                    }
                }
            )
        } else {

        }
    }
}*/
