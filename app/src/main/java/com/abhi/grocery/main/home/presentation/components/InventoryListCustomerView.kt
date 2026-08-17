package com.abhi.grocery.main.home.presentation.components

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.abhi.grocery.R
import com.abhi.grocery.common.utils.sampleItemsList
import com.abhi.grocery.common.utils.topFadingEdge
import com.abhi.grocery.main.cart.data.repository.CartRepositoryImpl
import com.abhi.grocery.main.cart.domain.CartItem
import com.abhi.grocery.main.cart.domain.repository.CartRepository
import com.abhi.grocery.main.home.data.repository.InventoryRepositoryImpl
import com.abhi.grocery.main.home.data.repository.PreviewInventoryRepository
import com.abhi.grocery.main.home.domain.PricingUnit
import com.abhi.grocery.main.home.domain.ProductItem
import com.abhi.grocery.main.home.domain.repository.InventoryRepository
import com.abhi.grocery.main.home.presentation.screen.StoreItemType
import com.abhi.grocery.main.home.presentation.viewmodel.HomeViewModel

@Composable
fun InventoryListCustomerView(
    customerSelectedItem: MutableState<ProductItem?>,
    products: List<ProductItem>,
    onSpeak: (String) -> Unit,
    context: Context
) {
    val density = LocalDensity.current.density

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(7.dp),
        horizontalArrangement = Arrangement.spacedBy(7.dp),
        modifier = Modifier
            .topFadingEdge(fadeHeight = 30.dp, density = density)
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Spacer(modifier = Modifier.height(30.dp))
        }

        items(products) { product ->
            ProductItemCustomerView(
                product,
                { customerSelectedItem.value = product },
                { onSpeak(it) },
                context
            )
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview (showBackground = true)
@Composable
fun PreviewInventoryListCustomerView() {
    val customerSelectedItem = remember { mutableStateOf<ProductItem?>(null) }

    val cartRepo: CartRepository = CartRepositoryImpl()
    val inventoryRepo: InventoryRepository = PreviewInventoryRepository()
    val viewModel: HomeViewModel = HomeViewModel(cartRepo, inventoryRepo)

    InventoryListCustomerView(
        products = sampleItemsList,
        context = LocalContext.current,
        customerSelectedItem = customerSelectedItem,
        onSpeak = { }
    )
}