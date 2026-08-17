package com.abhi.grocery.common.utils


import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class ScreenSize(
    val width: Dp,
    val height: Dp
)

@Composable
fun rememberScreenSize(): ScreenSize {

    val configuration = LocalConfiguration.current

    return remember(configuration) {
        ScreenSize(
            width = configuration.screenWidthDp.dp,
            height = configuration.screenHeightDp.dp
        )
    }
}