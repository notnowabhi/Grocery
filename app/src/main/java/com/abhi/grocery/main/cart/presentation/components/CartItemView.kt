package com.abhi.grocery.main.cart.presentation.components

import android.content.Context
import android.graphics.Paint
import android.text.Layout
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import com.abhi.grocery.R
import com.abhi.grocery.common.utils.sampleCartItemsList
import com.abhi.grocery.common.utils.sampleItemsList
import com.abhi.grocery.main.cart.domain.CartItem
import com.abhi.grocery.main.home.domain.PricingUnit
import com.abhi.grocery.main.home.domain.ProductItem
import com.abhi.grocery.main.home.presentation.components.productImageFile
import com.abhi.grocery.ui.theme.Geist

@Composable
fun CartItemView(
    item: CartItem,
    modifier: Modifier = Modifier,
    onUpdateItem: (CartItem) -> Unit,
    itemToEdit: MutableState<CartItem?>,
    context: Context
) {
    val quantity =
        if(item.product.pricingUnit == PricingUnit.PER_PIECE) {
            item.quantity.toInt().toString() + if(item.quantity > 1.0) " pieces" else " piece"
        } else {
            val tempQuantity =
                if(item.quantity % 1.0 == 0.0) { item.quantity.toInt().toString() } else { item.quantity.toString() }

            tempQuantity + " " + item.product.pricingUnit.unit.lowercase()
        }

    // (price per piece) * (no. of pieces)
    // (price per gram) * (weight in grams)
    // (price per kg) * (weight in kg)
    val amount = item.product.price * item.quantity

    val imageFile = remember(item.product.imageName) { productImageFile(context, item.product.imageName) }

    Row(
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .border(
                width = 1.dp,
                color = Color(0xFFD9D9D9),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(18.dp)
    ) {
        if(imageFile != null && imageFile.exists()) {
            AsyncImage(
                model = imageFile,
                contentDescription = null,
                modifier = Modifier
                    .size(45.dp)
                    .clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                painter = painterResource(R.drawable.ic_vendor_mode),
                contentDescription = null,
                modifier = Modifier
                    .size(45.dp)
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = item.product.name,
                fontFamily = Geist,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                lineHeight = 14.4.sp,
                color = Color.Black,
            )

            Text(
                text = quantity,
                fontFamily = Geist,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 14.4.sp,
                color = Color.Black,
            )
        }

        Spacer(Modifier.weight(1f))

        Column() {
            if(item.product.pricingUnit == PricingUnit.PER_PIECE) {
                PieceQuantityView(
                    amount = amount,
                    quantity = item.quantity,
                    onUpdateItem = { onUpdateItem(it) },
                    item = item
                )
            } else {
                WeightQuantityView(
                    amount,
                    quantity,
                    itemToEdit,
                    item
                )
            }
        }
    }
}

@Composable
private fun PieceQuantityView(
    amount: Double,
    quantity: Double,
    item: CartItem,
    onUpdateItem: (CartItem) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(7.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .dropShadow(
                    shape = RoundedCornerShape(5.dp),
                    shadow = Shadow(
                        radius = 7.dp,
                        spread = 1.dp,
                        color = Color.Black.copy(alpha = 0.2f),
                    )
                )
                .background(Color.White, RoundedCornerShape(5.dp))
        ) {
            Image(
                painter = painterResource(R.drawable.ic_minus_ext),
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .clickable {
                        onUpdateItem(
                            CartItem(
                                id = item.id,
                                product = item.product,
                                quantity = item.quantity - 1
                            )
                        )
                    }
            )

            Text(
                text = quantity.toInt().toString(),
                fontFamily = Geist,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 14.4.sp,
                color = Color.Black,
            )

            Image(
                painter = painterResource(R.drawable.ic_plus_ext),
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .clickable {
                        onUpdateItem(
                            CartItem(
                                id = item.id,
                                product = item.product,
                                quantity = item.quantity + 1
                            )
                        )
                    }

            )
        }

        Text(
            text = "₹$amount",
            fontFamily = Geist,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            lineHeight = 14.4.sp,
            color = Color(0xFFDA585B),
        )
    }
}

@Composable
private fun WeightQuantityView(
    amount: Double,
    quantity: String,
    itemToEdit: MutableState<CartItem?>,
    cartItem: CartItem
) {
    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .dropShadow(
                    shape = RoundedCornerShape(5.dp),
                    shadow = Shadow(
                        radius = 7.dp,
                        spread = 1.dp,
                        color = Color.Black.copy(alpha = 0.2f),
                    )
                )
                .clickable{
                    itemToEdit.value = cartItem
                }
                .background(Color.White, RoundedCornerShape(5.dp))
                .padding(6.dp)
                .padding(start = 3.dp)
        ) {
            Text(
                text = quantity,
                fontFamily = Geist,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 14.4.sp,
                color = Color.Black,
            )

            Image(
                painter = painterResource(R.drawable.ic_pencil),
                contentDescription = null,
                modifier = Modifier
                    .size(14.dp)
            )
        }

        Text(
            text = "₹$amount",
            fontFamily = Geist,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            lineHeight = 14.4.sp,
            color = Color(0xFFDA585B),
        )
    }
}

@Preview (showBackground = true)
@Composable
fun PreviewCartItemView() {
    val itemToEdit = remember { mutableStateOf<CartItem?>(null) }

    val cartItem =
        CartItem(
            id = sampleItemsList[1].id,
            product = sampleItemsList[1],
            quantity = 2.0
        )
    CartItemView(
        cartItem,
        onUpdateItem = {},
        itemToEdit = itemToEdit,
        context = LocalContext.current
    )
}