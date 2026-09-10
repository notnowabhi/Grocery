package com.abhi.grocery.main

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abhi.grocery.R
import com.abhi.grocery.main.cart.data.repository.CartRepositoryImpl
import com.abhi.grocery.main.cart.domain.repository.CartRepository
import com.abhi.grocery.main.cart.presentation.screen.CartTab
import com.abhi.grocery.main.cart.presentation.viewmodel.CartViewModel
import com.abhi.grocery.main.home.data.local.ProductDao
import com.abhi.grocery.main.home.data.repository.InventoryRepositoryImpl
import com.abhi.grocery.main.home.data.repository.PreviewInventoryRepository
import com.abhi.grocery.main.home.domain.repository.InventoryRepository
import com.abhi.grocery.main.home.presentation.screen.HomeTab
import com.abhi.grocery.main.home.presentation.viewmodel.HomeViewModel
import com.abhi.grocery.main.speaker.presentation.SpeakerTab
import com.abhi.grocery.main.uicomponents.CustomTabBar
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MainScreen(
    cartViewModel: CartViewModel,
    homeViewModel: HomeViewModel,
    onSpeak: (String) -> Unit,
    context: Context
) {
    var selectedTab by remember { mutableStateOf(Tab.Home) } // set here for landing tab
    var mode by remember { mutableStateOf(HomeTabMode.Vendor) }
    val inventory by homeViewModel.inventoryItems.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize()
    ){
        // to load screens via custom navbar
        AnimatedContent(
            targetState = selectedTab,
            transitionSpec = {
                fadeIn(tween(300)) togetherWith fadeOut(tween(300)) using SizeTransform(false)
            }
        ) { tab ->
            when (tab) {
                Tab.Home -> HomeTab(
                    inventory = inventory,
                    mode = mode,
                    onModeChange = { mode = it },
                    onSpeak = { onSpeak(it) },
                    onAddToCart = { homeViewModel.addItemToCart(it) },
                    onAddToInventory = { homeViewModel.addItemToInventory(it) },
                    onRemoveFromInventory = { homeViewModel.removeItemFromInventory(it) },
                    context = context
                )
                Tab.Cart -> CartTab(
                    viewModel = cartViewModel,
                    onSpeak = { onSpeak(it) },
                    context = context
                )
                Tab.Speaker -> SpeakerTab()
            }
        }

        CustomTabBar(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it },
            modifier = Modifier
                .align(Alignment.BottomCenter)  // works only inside a Box layout
                .padding(bottom = 20.dp)
        )
    }
}

enum class Tab(val title: String, val iconId: Int) {
    Home("Home", R.drawable.ic_home),
    Cart("Cart", R.drawable.ic_cart),
    Speaker("Speaker", R.drawable.ic_speaker)
}


enum class HomeTabMode(val title: String) {
    none("none"),
    Customer("Customer"),
    Vendor("Vendor")
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    val context = LocalContext.current

    val cartRepo: CartRepository = CartRepositoryImpl()
    val inventoryRepo: InventoryRepository = PreviewInventoryRepository()

    val cartViewModel: CartViewModel = CartViewModel(cartRepo)
    val homeViewModel: HomeViewModel = HomeViewModel(cartRepo, inventoryRepo)

    MainScreen(cartViewModel, homeViewModel, {}, context)
}