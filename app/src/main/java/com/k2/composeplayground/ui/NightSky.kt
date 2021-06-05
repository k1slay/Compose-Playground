package com.k2.composeplayground.ui

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import kotlin.random.Random

@Composable
@Preview
fun NightSky(
    modifier: Modifier = Modifier,
    starMaxSize: Float = 14F,
    starMinSize: Float = 2F,
    moonSize: Float = 120F,
    maxStars: Int = 200,
    showMoon: Boolean = false
) {

    var moonOffset = Offset(0F, 0F)
    val offsets = arrayListOf<Offset>()
    val starSizes = mutableListOf<State<Float>>()
    val baseColor = if (isSystemInDarkTheme()) Color.White else Color.Black

    val infiniteColorTransition = rememberInfiniteTransition()
    val moonColor by infiniteColorTransition.animateColor(
        initialValue = baseColor,
        targetValue = if (isSystemInDarkTheme()) Color(0xFFDDDDCC) else Color.DarkGray,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    if (starSizes.isNullOrEmpty()) {
        for (i in 0..maxStars) {
            val minSize = Random.nextDouble((starMinSize.toDouble()), (starMaxSize * 0.2)).toFloat()
            val maxSize = Random.nextDouble((starMaxSize * 0.8), (starMaxSize.toDouble())).toFloat()
            val duration = Random.nextInt(2500, 3500)
            val infiniteTransition = rememberInfiniteTransition()
            val starSize = infiniteTransition.animateFloat(
                initialValue = minSize,
                targetValue = maxSize,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = duration, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )
            starSizes.add(starSize)
        }
    }

    Canvas(modifier = modifier) {
        if (offsets.isNullOrEmpty()) {
            val height = size.height
            val width = size.width
            moonOffset = Offset(
                Random.nextDouble((width * 0.2), (width * 0.8)).toFloat(),
                Random.nextDouble((height * 0.2), (height * 0.4)).toFloat()
            )
            for (n in 0..maxStars) {
                val x = Random.nextDouble(width.toDouble())
                val y = Random.nextDouble(height.toDouble())
                offsets.add(Offset(x.toFloat(), y.toFloat()))
            }
        }
        for ((index, offset) in offsets.withIndex()) {
            if (index % 10 == 0) {
                drawCircle(
                    color = baseColor,
                    radius = starSizes[index].value,
                    center = offset,
                )
            } else {
                drawRect(
                    color = baseColor,
                    topLeft = offset,
                    size = Size(starSizes[index].value, starSizes[index].value)
                )
            }
        }
        if (showMoon) {
            drawCircle(
                color = moonColor,
                radius = moonSize,
                center = moonOffset,
            )
        }
    }

}
