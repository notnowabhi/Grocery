package com.abhi.grocery.main.cart.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhi.grocery.main.cart.domain.CartItem
import com.abhi.grocery.main.cart.domain.repository.CartRepository
import com.abhi.grocery.main.home.domain.PricingUnit
import com.abhi.grocery.main.home.domain.ProductItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val repository: CartRepository
) : ViewModel() {

    val cartItems = repository.cartItems

    val totalAmount = repository.cartItems
        .map { items ->
            items.sumOf { item ->
                item.quantity * item.product.price
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 0.0
        )

    fun addItem(item: CartItem) = repository.addItem(item)

    fun updateItem(item: CartItem) = repository.updateItem(item)

    fun removeItem(itemId: String) = repository.removeItem(itemId)

    fun clearCart() = repository.clearCart()

    // total cart price, normalized so kg/gram math doesn't mix units incorrectly
//    val cartTotal: StateFlow<Double> = cartItems
//        .map { items -> items.sumOf { it.lineTotal() } }
//        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)
}

// Put this as an extension or inside ProductItem itself
//fun ProductItem.lineTotal(): Double = when (pricingUnit) {
//    PricingUnit.PER_PIECE -> price * quantity
//    PricingUnit.PER_KG -> price * quantity              // quantity already in kg
//    PricingUnit.PER_GRAM -> (price / 1000.0) * quantity // convert gram-price to per-unit-gram basis if price is stored as per-kg-equivalent — see note below
//}