package com.abhi.grocery.main.cart.domain.repository

import com.abhi.grocery.main.cart.domain.CartItem
import com.abhi.grocery.main.home.domain.ProductItem
import kotlinx.coroutines.flow.StateFlow

interface CartRepository {
    val cartItems: StateFlow<List<CartItem>>

    fun addItem(item: CartItem)
    fun updateItem(item: CartItem)
    fun removeItem(id: String)
    fun clearCart()
}