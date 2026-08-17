package com.abhi.grocery.main.home.domain

import com.abhi.grocery.main.home.presentation.screen.StoreItemType

data class ProductItem(
    val id: String,
    val name: String,
    val imageName: String,
    val price: Double,
    val pricingUnit: PricingUnit,
//    val quantity: Double = defaultQuantityFor(pricingUnit), // the item details and its quantity should be different and isolated
    val productType: StoreItemType,
)

enum class PricingUnit(val title: String, val unit: String) {
    PER_KG(title = "/ kg", unit = "kg"),
    PER_GRAM(title = "/ gram", unit = "g"),
    PER_PIECE(title = "/ Piece", unit = "piece"),
    PER_DOZEN("/ dozen", "dozen")
}

fun defaultQuantityFor(unit: PricingUnit): Double =
    if (unit == PricingUnit.PER_PIECE) 1.0 else 0.5 // e.g. default 0.5kg for weight items