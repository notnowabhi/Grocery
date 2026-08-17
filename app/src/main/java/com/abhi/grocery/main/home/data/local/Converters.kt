package com.abhi.grocery.main.home.data.local

import androidx.room.TypeConverter
import com.abhi.grocery.main.home.domain.PricingUnit
import com.abhi.grocery.main.home.presentation.screen.StoreItemType

class Converters {

    @TypeConverter
    fun fromPricingUnit(value: PricingUnit): String {
        return value.name
    }

    @TypeConverter
    fun toPricingUnit(value: String): PricingUnit {
        return PricingUnit.valueOf(value)
    }

    @TypeConverter
    fun fromStoreItemType(value: StoreItemType): String {
        return value.name
    }

    @TypeConverter
    fun toStoreItemType(value: String): StoreItemType {
        return StoreItemType.valueOf(value)
    }
}