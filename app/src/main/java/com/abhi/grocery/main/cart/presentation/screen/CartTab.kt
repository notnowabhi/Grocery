package com.abhi.grocery.main.cart.presentation.screen

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.abhi.grocery.R
import com.abhi.grocery.common.utils.topFadingEdge
import com.abhi.grocery.common.utils.verticalFadingEdge
import com.abhi.grocery.main.cart.data.repository.CartRepositoryImpl
import com.abhi.grocery.main.cart.domain.CartItem
import com.abhi.grocery.main.cart.domain.repository.CartRepository
import com.abhi.grocery.main.cart.presentation.components.AddWeightOverlayView
import com.abhi.grocery.main.cart.presentation.components.CartItemView
import com.abhi.grocery.main.cart.presentation.viewmodel.CartViewModel
import com.abhi.grocery.main.home.domain.ProductItem
import com.abhi.grocery.main.home.presentation.components.AddItemOverlayView
import com.abhi.grocery.ui.theme.Geist

@Composable
fun CartTab(
    viewModel: CartViewModel,
    onSpeak: (String) -> Unit
) {
    val cartItems by viewModel.cartItems.collectAsState()
    val totalAmount = cartItems.sumOf {
        it.quantity * it.product.price
    }
    val itemToEdit = remember { mutableStateOf<CartItem?>(null) }
    val ttsString = totalAmount.toString() + "₹"

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(top = 75.dp)
                .padding(horizontal = 20.dp)
        ) {

            CartItemsView(
                modifier = Modifier.weight(1f),
                cartItems = cartItems,
                onUpdateItem = { viewModel.updateItem(it) },
                itemToEdit = itemToEdit
            )

            TotalAmountView(
                totalAmount = totalAmount,
                onSpeak = { onSpeak(ttsString) }
            )


            Spacer(Modifier.height(40.dp))

            RecordPurchaseButton { viewModel.clearCart() }

            Spacer(Modifier.height(110.dp))
        }

        itemToEdit.value?.let { item ->
            AddWeightOverlayView(
                item = item,
                onDone = { cartItem->
                    viewModel.updateItem(cartItem)
                    Log.d("Abhi", "HomeTab: cart:" + viewModel.cartItems.value.toString())
                },
                onDismiss = {
                    itemToEdit.value = null
                }
            )
        }
    }
}

@Composable
private fun CartItemsView(
    modifier: Modifier,
    cartItems: List<CartItem>,
    onUpdateItem: (CartItem) -> Unit,
    itemToEdit: MutableState<CartItem?>
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = "Cart",
            fontFamily = Geist,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            color = Color.Black,
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .verticalFadingEdge(fadeHeight = 60.dp),
            verticalArrangement = Arrangement.spacedBy(17.dp)
        ) {
            item { Spacer(Modifier.height(22.dp)) }

            items(
                items = cartItems,
                key = { it.id }
            ) { item ->
                CartItemView(
                    item = item,
                    modifier = Modifier.animateItem(),
                    onUpdateItem = { onUpdateItem(it) },
                    itemToEdit = itemToEdit
                )
            }

            item { Spacer(Modifier.height(22.dp)) }
        }
    }
}

@Composable
private fun TotalAmountView(
    totalAmount: Double,
    onSpeak: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(22.dp)
    ) {
        Text(
            text = "Total",
            fontFamily = Geist,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            color = Color.Black,
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier
                .clip(RoundedCornerShape(21.dp))
                .clickable { onSpeak() }
                .border(
                    width = 1.dp,
                    color = Color(0xFFD9D9D9),
                    shape = RoundedCornerShape(21.dp)
                )
                .padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_speaker),
                    contentDescription = null,
                    alpha = 0.46f,
                    modifier = Modifier
                        .size(18.dp)
                )
            }

            Text(
                text = "₹$totalAmount",
                fontFamily = Geist,
                fontWeight = FontWeight.SemiBold,
                fontSize = 38.sp,
                color = Color(0xFFDA585B),
                modifier = Modifier
                    .padding(bottom = 23.dp)
            )
        }
    }
}

@Composable
fun RecordPurchaseButton(
    onClick: () -> Unit
) {
    Text(
        text = "Done",
        fontFamily = Geist,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        color = Color.White,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .background(
                color = Color(0xFF58C447),
                shape = RoundedCornerShape(50)
            )
            .clip(RoundedCornerShape(50))
            .clickable { onClick() }
            .padding(vertical = 16.dp)
            .fillMaxWidth(),
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewCartTab() {
    val cartRepo: CartRepository = CartRepositoryImpl()
    val viewModel: CartViewModel = CartViewModel(cartRepo)
    CartTab(viewModel, { })
}