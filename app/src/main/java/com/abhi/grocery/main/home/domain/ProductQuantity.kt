package com.abhi.grocery.main.home.domain

sealed class ProductQuantity {

    data object ByPieces : ProductQuantity()

    data class ByWeight(
        val unit: WeightUnit
    ) : ProductQuantity()
}

enum class WeightUnit {
    KG,
    G
}