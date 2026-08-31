package com.abhi.grocery.main.uicomponents

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abhi.grocery.main.MainScreen
import com.abhi.grocery.main.cart.data.repository.CartRepositoryImpl
import com.abhi.grocery.main.cart.presentation.viewmodel.CartViewModel
import com.abhi.grocery.main.home.data.repository.PreviewInventoryRepository
import com.abhi.grocery.main.home.presentation.screen.StoreItemType
import com.abhi.grocery.main.home.presentation.viewmodel.HomeViewModel
import com.abhi.grocery.ui.theme.Geist
import com.abhi.grocery.ui.theme.GroceryTheme

@Composable
fun StoreItemTypeSelectorView(
    selectedItem: StoreItemType,
    onTypeChange: (StoreItemType) -> Unit
) {
    Row(
        modifier = Modifier
            .background(
                color = Color(0xffd9d9d9),
                shape = RoundedCornerShape(50)
            )
            .padding(6.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        StoreItemType.entries.forEach { tab ->
            ItemView(
                iconId = tab.iconId,
                title = tab.title,
                isSelected = selectedItem == tab,
                onClick = {onTypeChange(tab)},
                modifier = Modifier
                    .padding(start = if(tab == StoreItemType.entries.first()) { if(selectedItem == tab) { 0.dp } else { 20.dp } } else { 0.dp })
                    .padding(end = if(tab == StoreItemType.entries.last()) { if(selectedItem == tab) { 0.dp } else { 20.dp } } else { 0.dp })
            )
        }
    }
}

@Composable
private fun ItemView(
    iconId: Int,
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier
) {
    // animated the color change to not flicker
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else Color.Transparent,
        animationSpec = tween(300)
    )

    Row(
        modifier = modifier
            .height(36.dp)
            .background(
                shape = RoundedCornerShape(50),
                color = bgColor
            )
            .animateContentSize()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ){onClick()}
            .padding(horizontal = if(isSelected) 20.dp else 0.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(9.dp)
    ) {
        Icon(
            painterResource(iconId),
            contentDescription = null,
            modifier = Modifier
                .size(14.dp)
        )

        if(isSelected) {
            Text(
                text = title,
                fontFamily = Geist,
                fontWeight = FontWeight.Light,
                fontSize = 14.sp,
                lineHeight = 14.sp
            )
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun PreviewStoreItemTypeSelectorView() {
    val context = LocalContext.current

    val cartRepo = CartRepositoryImpl()
    val inventoryRepo = PreviewInventoryRepository()

    val cartViewModel = CartViewModel(cartRepo)
    val homeViewModel = HomeViewModel(cartRepo, inventoryRepo)

    GroceryTheme {
        MainScreen(
            cartViewModel,
            homeViewModel,
            { },
            context
        )
    }
}