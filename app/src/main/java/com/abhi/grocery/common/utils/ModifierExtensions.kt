package com.abhi.grocery.common.utils

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.topFadingEdge(fadeHeight: Dp = 80.dp, density: Float): Modifier {
    val brush = Brush.verticalGradient(
        0f to Color.Transparent,
        1f to Color.Black,
        endY = fadeHeight.value * density
    )
    return this
        .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
        .drawWithContent {
            drawContent()
            drawRect(brush = brush, blendMode = BlendMode.DstIn)
        }
}

fun Modifier.verticalFadingEdge(
    fadeHeight: Dp = 80.dp
): Modifier = this
    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()

        val fadeHeightPx = fadeHeight.toPx()

        // Top fade
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color.Transparent,
                    Color.Black
                ),
                startY = 0f,
                endY = fadeHeightPx
            ),
            blendMode = BlendMode.DstIn
        )

        // Bottom fade
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color.Black,
                    Color.Transparent
                ),
                startY = size.height - fadeHeightPx,
                endY = size.height
            ),
            blendMode = BlendMode.DstIn
        )
    }