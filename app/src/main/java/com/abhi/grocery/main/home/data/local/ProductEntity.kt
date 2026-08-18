package com.abhi.grocery.main.home.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.abhi.grocery.main.home.domain.PricingUnit
import com.abhi.grocery.main.home.presentation.screen.StoreItemType

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val hindiName: String,
    val imageName: String,
    val price: Double,
    val pricingUnit: PricingUnit,
    val productType: StoreItemType
)