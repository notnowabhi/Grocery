package com.abhi.grocery.main.home.data.mapper

import com.abhi.grocery.main.assets.ProductJson
import com.abhi.grocery.main.home.data.local.ProductEntity
import com.abhi.grocery.main.home.domain.PricingUnit
import com.abhi.grocery.main.home.domain.ProductItem
import com.abhi.grocery.main.home.presentation.screen.StoreItemType

fun ProductEntity.toProductItem(): ProductItem {
    return ProductItem(
        id = id,
        name = name,
        imageName = imageName,
        price = price,
        pricingUnit = pricingUnit,
        productType = productType
    )
}

fun ProductItem.toProductEntity(): ProductEntity {
    return ProductEntity(
        id = id,
        name = name,
        imageName = imageName,
        price = price,
        pricingUnit = pricingUnit,
        productType = productType
    )
}

fun ProductJson.toEntity(): ProductEntity {
    return ProductEntity(
        id = id,
        name = name,
        imageName = imageName,
        price = price,
        pricingUnit = PricingUnit.valueOf(pricingUnit),
        productType = StoreItemType.valueOf(productType)
    )
}