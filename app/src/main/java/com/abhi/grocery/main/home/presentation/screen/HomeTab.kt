package com.abhi.grocery.main.home.presentation.screen

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abhi.grocery.R
import com.abhi.grocery.common.utils.sampleItemsList
import com.abhi.grocery.main.HomeTabMode
import com.abhi.grocery.main.MainScreen
import com.abhi.grocery.main.cart.data.repository.CartRepositoryImpl
import com.abhi.grocery.main.cart.domain.CartItem
import com.abhi.grocery.main.cart.domain.repository.CartRepository
import com.abhi.grocery.main.cart.presentation.viewmodel.CartViewModel
import com.abhi.grocery.main.home.data.repository.InventoryRepositoryImpl
import com.abhi.grocery.main.home.data.repository.PreviewInventoryRepository
import com.abhi.grocery.main.home.domain.ProductItem
import com.abhi.grocery.main.home.domain.repository.InventoryRepository
import com.abhi.grocery.main.home.presentation.components.AddItemOverlayView
import com.abhi.grocery.main.home.presentation.components.AddItemToInventoryView
import com.abhi.grocery.main.home.presentation.components.InventoryListCustomerView
import com.abhi.grocery.main.home.presentation.components.InventoryListVendorView
import com.abhi.grocery.main.home.presentation.viewmodel.HomeViewModel
import com.abhi.grocery.main.uicomponents.StoreItemTypeSelectorView
import com.abhi.grocery.ui.theme.Geist
import com.abhi.grocery.ui.theme.GroceryTheme
import kotlinx.coroutines.delay
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun HomeTab(
    inventory: List<ProductItem>,
    mode: HomeTabMode,
    onModeChange: (HomeTabMode) -> Unit,
    onSpeak: (String) -> Unit,
    onAddToCart: (CartItem) -> Unit,
    onAddToInventory: (ProductItem) -> Unit,
    onRemoveFromInventory: (String) -> Unit,
    context: Context
) {
    var vendorSelectedItemType by remember { mutableStateOf(StoreItemType.Vegetables) }
    var customerSelectedItemType by remember { mutableStateOf(StoreItemType.Vegetables) }
    var time by remember { mutableStateOf(LocalTime.now()) }

    val customerSelectedItem = remember { mutableStateOf<ProductItem?>(null) }

    val isAddItemToInventoryViewVisible = remember { mutableStateOf<Boolean>(false) }
    val isRemoveItemFromInventoryActive = remember { mutableStateOf<Boolean>(false) }

    LaunchedEffect(Unit) {
        while (true) {
            val now = LocalTime.now()
            time = now
            delay((60 - now.second) * 1000L)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AnimatedContent(
            targetState = mode,
            transitionSpec = {
                fadeIn(
                    animationSpec = tween(300)
                ) togetherWith fadeOut(
                    animationSpec = tween(300)
                ) using SizeTransform(clip = false)
            },
            label = ""
        ) { targetMode ->
            if(targetMode == HomeTabMode.none) {
                LandingStateView(time, onModeChange)
            } else {
                AddItemsStateView(
                    inventory = inventory,
                    mode = mode,
                    onModeChange = onModeChange,
                    customerSelectedItem = customerSelectedItem,
                    vendorSelectedItemType = vendorSelectedItemType,
                    customerSelectedItemType = customerSelectedItemType,
                    onVendorItemTypeChange = {vendorSelectedItemType = it},
                    onCustomerItemTypeChange = {customerSelectedItemType = it},
                    onSpeak = { onSpeak(it) },
                    onAddItemClick = { isAddItemToInventoryViewVisible.value = true },
                    onRemoveItemClick = { isRemoveItemFromInventoryActive.value = true },
                    context = context
                )

                if(isAddItemToInventoryViewVisible.value) {
                    AddItemToInventoryView(
                        isVisible = isAddItemToInventoryViewVisible,
                        onAdd = { onAddToInventory(it) }
                    )
                }
            }
        }

        customerSelectedItem.value?.let { item ->
            AddItemOverlayView(
                item = item,
                onAddItem = { cartItem->
                    onAddToCart(cartItem)
                },
                onDismiss = {
                    customerSelectedItem.value = null
                }
            )
        }
    }
}

@Composable
private fun AddItemsStateView(
    inventory: List<ProductItem>,
    mode: HomeTabMode,
    onModeChange: (HomeTabMode) -> Unit,
    customerSelectedItem: MutableState<ProductItem?>,
    vendorSelectedItemType: StoreItemType,
    customerSelectedItemType: StoreItemType,
    onVendorItemTypeChange: (StoreItemType) -> Unit,
    onCustomerItemTypeChange: (StoreItemType) -> Unit,
    onSpeak: (String) -> Unit,
    onAddItemClick: () -> Unit,
    onRemoveItemClick: () -> Unit,
    context: Context
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 75.dp)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ModeToggleView(mode, onModeChange)

        AnimatedContent(
            targetState = mode,
            transitionSpec = {
                fadeIn(animationSpec = tween(300)) togetherWith
                        fadeOut(animationSpec = tween(300))
            },
            label = ""
        ) { targetMode ->
            if(targetMode == HomeTabMode.Customer) {
                CustomerAddItemsView(
                    inventory,
                    customerSelectedItem,
                    customerSelectedItemType,
                    onCustomerItemTypeChange,
                    { onSpeak(it) },
                    context
                )
            } else if(targetMode == HomeTabMode.Vendor) {
                VendorAddItemsView(
                    inventory,
                    vendorSelectedItemType,
                    onVendorItemTypeChange,
                    onAddItemClick = onAddItemClick,
                    onRemoveItemClick = onRemoveItemClick,
                    context
                )
            }
        }
    }
}

@Composable
private fun CustomerAddItemsView(
    products: List<ProductItem>,
    customerSelectedItem: MutableState<ProductItem?>,
    selectedItemType: StoreItemType,
    onCustomerItemTypeChange: (StoreItemType) -> Unit,
    onSpeak: (String) -> Unit,
    context: Context
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .padding(top = 25.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "Your Cart",
                fontFamily = Geist,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp
            )
        }

        Spacer(Modifier.size(25.dp))

        StoreItemTypeSelectorView(
            selectedItem = selectedItemType,
            onTypeChange = onCustomerItemTypeChange,
        )

        InventoryListCustomerView(
            products = products,
            customerSelectedItem = customerSelectedItem,
            onSpeak = { onSpeak(it) },
            context = context,
        )
    }
}

@Composable
private fun VendorAddItemsView(
    products: List<ProductItem>,
    selectedItemType: StoreItemType,
    onVendorItemTypeChange: (StoreItemType) -> Unit,
    onAddItemClick: () -> Unit,
    onRemoveItemClick: () -> Unit,
    context: Context
) {
    Box() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .padding(top = 25.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Your Inventory",
                    fontFamily = Geist,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp
                )
            }

            Spacer(Modifier.size(25.dp))

            StoreItemTypeSelectorView(
                selectedItem = selectedItemType,
                onTypeChange = onVendorItemTypeChange,
            )

            InventoryListVendorView(
                products = products,
                context = context
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 25.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))

            AddItemsButtons(onAddItemClick, onRemoveItemClick)
        }

    }
}

@Composable
private fun AddItemsButtons(
    onAddItemClick: () -> Unit,
    onRemoveItemClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_minus),
            contentDescription = null,
            modifier = Modifier
                .shadow(
                    elevation = 10.dp,
                    shape = CircleShape
                )
                .clip(CircleShape)
                .background(Color.White)
                .clickable { onRemoveItemClick() }
                .padding(12.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            painter = painterResource(R.drawable.ic_plus),
            contentDescription = null,
            modifier = Modifier
                .shadow(
                    elevation = 10.dp,
                    shape = CircleShape
                )
                .clip(CircleShape)
                .background(Color.White)
                .clickable { onAddItemClick() }
                .padding(12.dp)
        )
    }
}

@Composable
private fun ModeToggleView(
    mode: HomeTabMode,
    onModeChange: (HomeTabMode) -> Unit
) {
    Row(
        modifier = Modifier
            .background(
                color = if (mode == HomeTabMode.Vendor) Color(0xFF749CF8) else Color(0xFFDA585B),
                shape = RoundedCornerShape(50)
            )
            .padding(6.dp)
    ) {
        ModeToggleItemView(
            iconId = R.drawable.ic_food_stall,
            title = "Vendor",
            isSelected = mode == HomeTabMode.Vendor,
            onClick = {onModeChange(HomeTabMode.Vendor)}
        )

        ModeToggleItemView(
            iconId = R.drawable.ic_bag,
            title = "Customer",
            isSelected = mode == HomeTabMode.Customer,
            onClick = {onModeChange(HomeTabMode.Customer)}
        )
    }
}

@Composable
private fun ModeToggleItemView(
    iconId: Int,
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    // animated the color change to not flicker
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else Color.Transparent,
        animationSpec = tween(300)
    )

    Row(
        modifier = Modifier
            .background(
                shape = RoundedCornerShape(50),
                color = bgColor
            )
            .animateContentSize()
            .clickable { onClick() }
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(9.dp)
    ) {
        Icon(
            painterResource(iconId),
            contentDescription = null,
            modifier = Modifier
                .padding(vertical = 11.dp)
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

@Composable
private fun LandingStateView(time: LocalTime, onModeChange: (HomeTabMode) -> Unit) {
    Box{
        Image(
            painter = painterResource(R.drawable.bg_home),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 110.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MinuteClock(time)

            Spacer(modifier = Modifier.size(64.dp))

            GreetingView(time)

            Spacer(modifier = Modifier.size(60.dp))

            Box{
                ModeCardView(
                    HomeTabMode.Vendor,
                    modifier = Modifier
                        .padding(top = 30.dp)
                        .padding(start = 155.dp)
                        .rotate(6f),
                    onClick = {onModeChange(HomeTabMode.Vendor)}
                )

                ModeCardView(
                    HomeTabMode.Customer,
                    modifier = Modifier
                        .padding(bottom = 30.dp)
                        .padding(end = 155.dp)
                        .rotate(-6f),
                    onClick = {onModeChange(HomeTabMode.Customer)}
                )
            }
        }
    }

}

@Composable
private fun ModeCardView(
    mode: HomeTabMode,
    modifier: Modifier,
    onClick: () -> Unit,
) {
    if(mode != HomeTabMode.none) {
        val imageRes = if(mode == HomeTabMode.Customer) R.drawable.ic_customer_mode else if(mode == HomeTabMode.Vendor) R.drawable.ic_vendor_mode else R.drawable.ic_launcher_background

        Column(
            modifier = modifier
                .shadow(
                    elevation = 30.dp,
                    shape = RoundedCornerShape(20.dp)
                )
                .width(162.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(20.dp)
                )
                .clickable { onClick() },
            horizontalAlignment = Alignment.CenterHorizontally)
        {
            Spacer(modifier = Modifier.size(53.dp))
            Image(
                painterResource(imageRes),
                contentDescription = null,
                modifier = Modifier.size(width = 58.dp, height = 61.dp)
            )

            Spacer(modifier = Modifier.size(44.dp))

            Column{
                Text(
                    text = mode.title,
                    fontFamily = Geist,
                    fontWeight = FontWeight.Medium,
                    fontSize = 24.sp,
                    color = Color(0xFF212121),
                )

                Text(
                    text = "Mode",
                    fontFamily = Geist,
                    fontWeight = FontWeight.Medium,
                    fontSize = 24.sp,
                    color = Color(if(mode == HomeTabMode.Customer) 0x66FE421D else if(mode == HomeTabMode.Vendor) 0x661C6BEA else 0x000000)
                )


            }

            Spacer(modifier = Modifier.size(13.dp))
        }
    }
}

@Composable
private fun GreetingView(time: LocalTime) {
    val hour = time.hour

    val greeting = when (hour) {
        in 5..11 -> "Good\nMorning"     // 5 AM → 11:59 AM
        in 12..15 -> "Good\nAfternoon" // 12 PM → 3:59 PM
        else -> "Good\nEvening"        // 4 PM → 4:59 AM
    }

    Text(
        text = greeting,
        fontFamily = Geist,
        fontWeight = FontWeight.Normal,
        fontSize = 64.sp,
        lineHeight = 55.sp,
        textAlign = TextAlign.Center,
        color = Color.White.copy(alpha = 0.5f)
    )
}

@Composable
private fun MinuteClock(time: LocalTime) {
    val formatter = DateTimeFormatter.ofPattern("hh:mm a")

    Text(
        text = time.format(formatter),
        fontFamily = Geist,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp
    )
}

enum class StoreItemType(val title: String, val iconId: Int) {
    Vegetables("Vegetables", R.drawable.ic_vegetables),
    Fruits("Fruits", R.drawable.ic_fruits),
    Others("Others", R.drawable.ic_others)
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun PreviewHomeTab() {
    val context = LocalContext.current
    val cartRepo: CartRepository = CartRepositoryImpl()
    val inventoryRepo: InventoryRepository = PreviewInventoryRepository()
    val cartViewModel: CartViewModel = CartViewModel(cartRepo)
    val homeViewModel: HomeViewModel = HomeViewModel(cartRepo, inventoryRepo)
    GroceryTheme {
        MainScreen(cartViewModel, homeViewModel, { }, context)
    }
}