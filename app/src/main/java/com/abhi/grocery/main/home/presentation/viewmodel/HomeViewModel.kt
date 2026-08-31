package com.abhi.grocery.main.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhi.grocery.main.cart.domain.CartItem
import com.abhi.grocery.main.cart.domain.repository.CartRepository
import com.abhi.grocery.main.home.domain.ProductItem
import com.abhi.grocery.main.home.domain.repository.InventoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val inventoryRepository: InventoryRepository
): ViewModel() {
    val cartItems = cartRepository.cartItems
    val inventoryItems: StateFlow<List<ProductItem>> =
        inventoryRepository.inventoryItems
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    fun addItemToCart(item: CartItem) = cartRepository.addItem(item)
    fun removeItemFromCart(itemId: String) = cartRepository.removeItem(itemId)
    fun clearCart() = cartRepository.clearCart()

    fun addItemToInventory(item: ProductItem) =
        viewModelScope.launch {
            inventoryRepository.addItem(item)
        }

    fun removeItemFromInventory(itemId: String) =
        viewModelScope.launch {
            inventoryRepository.removeItem(itemId)
        }

    fun updateItemInInventory(item: ProductItem) =
        viewModelScope.launch {
            inventoryRepository.updateItem(item)
        }
}