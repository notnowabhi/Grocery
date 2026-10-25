package com.abhi.grocery.common.utils

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

private const val FadeMillis = 300

@Composable
fun FadeInOut(
    visible: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        enter = fadeIn(tween(FadeMillis)),
        exit = fadeOut(tween(200))
    ) {
        content()
    }
}

@Composable
fun FadingOverlay(
    visible: Boolean,
    content: @Composable () -> Unit
) {
    AnimatedContent(
        targetState = visible,
        modifier = Modifier.fillMaxSize(),
        transitionSpec = {
            fadeIn(tween(FadeMillis)) togetherWith fadeOut(tween(FadeMillis)) using null
        },
        label = "fadingOverlay"
    ) { shown ->
        if (shown) content()
    }
}

@Composable
fun <T> FadingOverlay(
    value: T?,
    content: @Composable (T) -> Unit
) {
    AnimatedContent(
        targetState = value,
        modifier = Modifier.fillMaxSize(),
        transitionSpec = {
            fadeIn(tween(FadeMillis)) togetherWith fadeOut(tween(FadeMillis)) using null
        },
        label = "fadingOverlay"
    ) { current ->
        if (current != null) content(current)
    }
}
