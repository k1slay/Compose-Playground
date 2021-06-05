package com.k2.composeplayground.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlin.math.sin

const val waveCount = 6
const val period = 5F
const val vSteps = 7
const val hSteps = 7
private var amplitude = 75
private var lineWidth = 8.0F
private var spacing = 24
private var boxHeight = 0

val colors = listOf(
    Color(0xFF9D2946),
    Color(0xFFA54347),
    Color(0xFFC9885C),
    Color(0xFFF0C46C),
    Color(0xFF95C54E),
    Color(0xFF82AD8A),
)

val colorsEdge = listOf(
    Color(0xFFC54EED),
    Color(0xFF7148D7),
    Color(0xFF513FB2),
    Color(0xFF486A8A),
    Color(0xFF407877),
    Color(0xFF518C72),
)

val colorsBorders = listOf(
    Color(0xFF613875),
    Color(0xFF813167),
    Color(0xFF932D50),
    Color(0xFFA8284A),
    Color(0xFFB8453E),
    Color(0xFFD8AE5F),
)

val colorsCenter = listOf(
    Color(0xFF53916A),
    Color(0xFF4A751D),
    Color(0xFF709103),
    Color(0xFFD9C666),
    Color(0xFFE5AA3E),
    Color(0xFFB02548),
)

val colorsAdjacent = listOf(
    Color(0xFF2C32B3),
    Color(0xFF466EA2),
    Color(0xFF4C7E82),
    Color(0xFF578C7A),
    Color(0xFF7CA568),
    Color(0xFFB4C557),
)

val yPoints = mutableListOf<Float>()

@Composable
fun Waves(modifier: Modifier = Modifier) {

    var start: Int
    var end: Int
    var boxWidth = 0
    lineWidth = with(LocalDensity.current) { 2.5.dp.toPx() }
    spacing = with(LocalDensity.current) { 7.0.dp.toPx() }.toInt()
    boxHeight = with(LocalDensity.current) { 48.dp.toPx() }.toInt()
    amplitude = with(LocalDensity.current) { 21.5.dp.toPx() }.toInt()

    val phases = mutableListOf<State<Float>>()
    for (i in 0..waveCount) {
        val infiniteTransition = rememberInfiniteTransition()
        val phase = infiniteTransition.animateFloat(
            initialValue = i.toFloat(),
            targetValue = i + 9.65F,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )
        phases.add(phase)
    }

    Canvas(modifier = modifier) {
        if (boxWidth == 0) {
            boxWidth = (size.width / hSteps - spacing).toInt()
        }
        if (yPoints.isNullOrEmpty()) {
            populateYPoints(size.center.y)
        }
        for ((index, y) in yPoints.withIndex()) {
            start = spacing / 2
            end = start + boxWidth
            for (step in 1..hSteps) {
                val waveColors = getColorList(step, index + 1)
                makeWaves(startX = start, endX = end, centerY = y, phases, waveColors)
                start += boxWidth + spacing
                end += boxWidth + spacing
            }
        }
    }
}

fun DrawScope.makeWaves(
    startX: Int, endX: Int, centerY: Float, phases: List<State<Float>>, colors: List<Color>
) {
    for (i in 1..waveCount) {
        val offsets = mutableListOf<Offset>()
        for (x in startX..endX step 20) {
            val y = centerY + amplitude * sin(period * x.toDouble() + (phases[i].value * 0.65))
            offsets.add(Offset(x.toFloat(), y.toFloat()))
        }
        drawPoints(
            points = offsets,
            pointMode = PointMode.Polygon,
            brush = Brush.linearGradient(colors = listOf(colors[i - 1], colors[i - 1])),
            strokeWidth = lineWidth
        )
    }
}

fun populateYPoints(centerY: Float) {
    val reps = (vSteps - 1) / 2
    var topY = centerY - ((boxHeight + spacing) * (reps + 1))
    for (r in 1..reps) {
        topY += boxHeight + spacing
        yPoints.add(topY)
    }
    yPoints.add(centerY)
    var bottomY = centerY
    for (r in 1..reps) {
        bottomY += boxHeight + spacing
        yPoints.add(bottomY)
    }
}


/**
 * BAD CODE!! LOOK AWAY!!
 */
fun getColorList(xIndex: Int, yIndex: Int): List<Color> {
    return if (xIndex.isAtEdge(hSteps) && yIndex.isAtEdge(vSteps)) {
        colorsEdge
    } else if ((xIndex.isMiddleOf(hSteps) && yIndex.isAtEdge(vSteps))
        || (yIndex.isMiddleOf(vSteps) && xIndex.isAtEdge(hSteps))
        || xIndex.isMiddleOf(hSteps) && yIndex.isMiddleOf(vSteps)
    ) {
        colorsCenter
    } else if (xIndex.isAtEdge(hSteps) || yIndex.isAtEdge(vSteps)) {
        colorsBorders
    } else if (
        (xIndex.adjacentToMiddleOf(hSteps) || xIndex.isMiddleOf(hSteps))
        && (yIndex.adjacentToMiddleOf(vSteps) || yIndex.isMiddleOf(vSteps))
    ) {
        colorsAdjacent
    } else {
        colors
    }
}

fun Int.adjacentToMiddleOf(number: Int): Boolean {
    val middle = number.middle()
    return this == middle - 1 || this == middle + 1
}

fun Int.middle(): Int {
    return if (this % 2 == 0) {
        this / 2
    } else {
        this / 2 + 1
    }
}

fun Int.isMiddleOf(number: Int): Boolean {
    return this == number.middle()
}

fun Int.isAtEdge(max: Int): Boolean {
    return this == 1 || this == max
}
