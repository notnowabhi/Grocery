package com.abhi.grocery.common.utils

import com.abhi.grocery.R
import com.abhi.grocery.main.cart.domain.CartItem
import com.abhi.grocery.main.home.domain.PricingUnit
import com.abhi.grocery.main.home.domain.ProductItem
import com.abhi.grocery.main.home.presentation.screen.StoreItemType

val sampleItemsList = mutableListOf(
    ProductItem(
        id = "banana",
        name = "Bananas",
        imageName = "banana",
        productType = StoreItemType.Fruits,
        price = 40.0,
        pricingUnit = PricingUnit.PER_PIECE
    ),

    ProductItem(
        id = "apple",
        name = "Apples",
        imageName = "banana",
        productType = StoreItemType.Fruits,
        price = 40.0,
        pricingUnit = PricingUnit.PER_KG
    ),

    ProductItem(
        id = "egg",
        name = "Eggs",
        imageName = "banana",
        productType = StoreItemType.Others,
        price = 40.0,
        pricingUnit = PricingUnit.PER_PIECE
    ),

    ProductItem(
        id = "tomato",
        name = "Tomatoes",
        imageName = "banana",
        productType = StoreItemType.Vegetables,
        price = 40.0,
        pricingUnit = PricingUnit.PER_PIECE
    ),

    ProductItem(
        id = "egg",
        name = "Eggs",
        imageName = "banana",
        productType = StoreItemType.Others,
        price = 40.0,
        pricingUnit = PricingUnit.PER_PIECE
    ),

    ProductItem(
        id = "tomato",
        name = "Tomatoes",
        imageName = "banana",
        productType = StoreItemType.Vegetables,
        price = 40.0,
        pricingUnit = PricingUnit.PER_PIECE
    ),
)

val sampleCartItemsList = mutableListOf<CartItem>(
    CartItem(
        id = sampleItemsList[0].id,
        product = sampleItemsList[0],
        quantity = 2.0
    ),
    CartItem(
        id = sampleItemsList[1].id,
        product = sampleItemsList[1],
        quantity = 2.0
    ),
//    CartItem(
//        id = sampleItemsList[2].id,
//        product = sampleItemsList[2],
//        quantity = 2.0
//    ),
//    CartItem(
//        id = sampleItemsList[3].id,
//        product = sampleItemsList[3],
//        quantity = 2.0
//    ),
//    CartItem(
//        id = sampleItemsList[4].id,
//        product = sampleItemsList[0],
//        quantity = 2.0
//    ),
//    CartItem(
//        id = sampleItemsList[5].id,
//        product = sampleItemsList[1],
//        quantity = 2.0
//    ),
)