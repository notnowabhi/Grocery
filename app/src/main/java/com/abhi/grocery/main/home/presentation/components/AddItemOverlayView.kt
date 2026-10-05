package com.abhi.grocery.main.home.presentation.components

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableDoubleState
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.abhi.grocery.R
import com.abhi.grocery.common.utils.rememberScreenSize
import com.abhi.grocery.common.utils.sampleItemsList
import com.abhi.grocery.main.cart.domain.CartItem
import com.abhi.grocery.main.home.domain.PricingUnit
import com.abhi.grocery.main.home.domain.ProductItem
import com.abhi.grocery.main.home.domain.WeightUnit
import com.abhi.grocery.ui.theme.Geist

@Composable
fun AddItemOverlayView(
    item: ProductItem,
    onAddItem: (CartItem) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    // calculated the same way the image size was calculated in Product item cards
    val screenSize = rememberScreenSize()
    val imageSize: Dp = ((screenSize.width - (40.dp + 7.dp)) / 2) - 70.dp

    val pieceQuantity = remember { mutableIntStateOf(0) }
    val gramsQuantity = remember { mutableDoubleStateOf(0.0) } // stored in grams then converted to kg for UI
    val unit = remember { mutableStateOf(WeightUnit.G) }

    val imageFile = remember(item.imageName) { productImageFile(context, item.imageName) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black.copy(alpha = 0.3f))
            .clickable{ onDismiss() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .width(screenSize.width - 90.dp)
                .imePadding()
                .clip(RoundedCornerShape(30.dp))
                .background(Color.White)
                .clickable{ }
                .padding(horizontal = 30.dp)
                .padding(
                    top = 20.dp,
                    bottom = 15.dp
                ),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if(imageFile != null && imageFile.exists()) {
                    AsyncImage(
                        model = imageFile,
                        contentDescription = null,
                        modifier = Modifier
                            .size(imageSize)
                            .clip(RoundedCornerShape(20.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.ic_vendor_mode),
                        contentDescription = null,
                        modifier = Modifier
                            .size(imageSize)
                    )
                }

                Column(
//                    modifier = Modifier.padding(top = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = item.name,
                        fontFamily = Geist,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        lineHeight = 16.sp
                    )

                    if(item.pricingUnit == PricingUnit.PER_PIECE) {
                        EnterPieceQuantityView(count = pieceQuantity)
                    } else {
                        EnterWeightQuantityView(
                            weight = gramsQuantity,
                            unit = unit
                        )
                    }

                }
            }

            Text(
                text = "Add Item",
                fontFamily = Geist,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                lineHeight = 14.4.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFFDA585B))
                    .clickable {
                        addItem(
                            item = item,
                            quantity = if (item.pricingUnit == PricingUnit.PER_PIECE) pieceQuantity.intValue.toDouble() else gramsQuantity.value,
                            unit = unit,
                            onAddItem = onAddItem,
                            onDismiss = onDismiss
                        )
                    }
                    .padding(vertical = 18.dp)
                    .fillMaxWidth(),
            )
        }
    }
}

private fun addItem(
    item: ProductItem,
    quantity: Double,
    unit: MutableState<WeightUnit>,
    onAddItem: (CartItem) -> Unit,
    onDismiss: () -> Unit
) {
    val quantity =
        if( unit.value == WeightUnit.G && item.pricingUnit == PricingUnit.PER_KG ) {
            quantity / 1000
        } else if( unit.value == WeightUnit.KG && item.pricingUnit == PricingUnit.PER_GRAM ) {
            quantity * 1000
        } else {
//            unit.value == WeightUnit.G && item.pricingUnit == PricingUnit.PER_GRAM ||
//            unit.value == WeightUnit.KG && item.pricingUnit == PricingUnit.PER_KG ||
//            item.pricingUnit == PricingUnit.PER_PIECE
            quantity
        }

    val cartItem = CartItem(
        id = item.id,
        product = item,
        quantity = quantity,
    )

    onAddItem(cartItem)
    onDismiss()
}

@Composable
private fun EnterPieceQuantityView(count: MutableIntState) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(21.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .clickable {
                        if (count.intValue > 0) {
                            count.intValue--
                        }
                    }
                    .background(Color(0xFFD9D9D9))
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_minus),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Color(0xFFDA585B)),
                    modifier = Modifier.padding(4.dp)
                )
            }

            Text(
                text = count.intValue.toString() + " Piece",
                fontFamily = Geist,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp
            )

            Spacer(Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .size(21.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .clickable { count.intValue++ }
                    .background(Color(0xFFD9D9D9))
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_plus),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Color(0xFFDA585B)),
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
        HorizontalDivider(color = Color(0xFFD9D9D9))
    }
}

@Composable
private fun EnterWeightQuantityView(
    weight: MutableDoubleState,
    unit: MutableState<WeightUnit>
) {
    val state = rememberTextFieldState()

    LaunchedEffect(state.text) {
        weight.doubleValue = state.text.toString().toDoubleOrNull() ?: 0.0
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Row() {
            BasicTextField(
                state = state,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                lineLimits = TextFieldLineLimits.SingleLine,
                textStyle = LocalTextStyle.current,
                modifier = Modifier
                    .weight(1f)
                    .background(Color.Transparent)
            )

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color(0xFFD9D9D9))
                    .clickable{ if(unit.value == WeightUnit.G) { unit.value = WeightUnit.KG } else { unit.value = WeightUnit.G } }
                    .padding(top = 4.dp, bottom = 2.dp, start = 4.dp, end = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = unit.value.toString().lowercase(),
                    fontFamily = Geist,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp
                )

                Image(
                    painter = painterResource(R.drawable.ic_recycle),
                    contentDescription = null,
                    modifier = Modifier
                        .size(14.dp)
                )
            }

        }

        HorizontalDivider(color = Color(0xFFD9D9D9))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAddItemOverlayView() {
    AddItemOverlayView(
        item = sampleItemsList[1],
        onAddItem = { },
        onDismiss = { }
    )
}