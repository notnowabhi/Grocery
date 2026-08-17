package com.abhi.grocery.main.home.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.abhi.grocery.main.home.presentation.screen.StoreItemType
import com.abhi.grocery.main.uicomponents.StoreItemTypeSelectorView

@Composable
fun AddToInventoryOverlayView() {
    var selectedItemType by remember { mutableStateOf(StoreItemType.Vegetables) }

    Box(
        modifier = Modifier
            .background(color = Color.Black.copy(alpha = 0.3f))
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(

        ) {
            StoreItemTypeSelectorView(
                selectedItemType,
                { selectedItemType = it }
            )

//            Image(
//                painter = painterResource()
//            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAddToInventoryOverlayView() {
    AddToInventoryOverlayView()
}