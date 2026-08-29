package com.abhi.grocery.main.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.abhi.grocery.main.home.domain.ProductItem

@Composable
fun AddItemToInventoryView(
    isVisible: MutableState<Boolean>,
    onAdd: (ProductItem) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black.copy(alpha = 0.3f))
            .clickable{ isVisible.value = false }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewAddItemToInventoryView() {
    val isAddItemToInventoryViewVisible = remember { mutableStateOf<Boolean>(false) }
    AddItemToInventoryView(
        isAddItemToInventoryViewVisible,
        {}
    )
}