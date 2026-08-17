package com.abhi.grocery.main.cart.domain

import com.abhi.grocery.main.home.domain.ProductItem
import com.abhi.grocery.main.home.domain.defaultQuantityFor

data class CartItem(
    val id: String,
    val product: ProductItem,
    val quantity: Double = defaultQuantityFor(product.pricingUnit)
)

