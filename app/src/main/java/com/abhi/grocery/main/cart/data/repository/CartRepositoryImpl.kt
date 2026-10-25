package com.abhi.grocery.main.cart.data.repository

import com.abhi.grocery.main.cart.domain.CartItem
import com.abhi.grocery.main.cart.domain.repository.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor() : CartRepository {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    override val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    override fun addItem(item: CartItem) {
        if(item.quantity == 0.0) return
        _cartItems.update { current ->
            val existing = current.find { it.id == item.id }
            if (existing != null) {
                current.map {
                    if (it.id == item.id) it.copy(quantity = it.quantity + item.quantity) else it
                }
            } else {
                current + item
            }
        }
    }

    override fun updateItem(item: CartItem) {
        if (item.quantity == 0.0) {
            removeItem(item.id)
            return
        }

        _cartItems.update { current ->
            current.map {
                if (it.id == item.id) item else it
            }
        }
    }

    override fun removeItem(id: String) {
        _cartItems.update { current ->
            current.filter { it.id != id }
        }
    }

    override fun clearCart() {
        _cartItems.value = emptyList()
    }
}