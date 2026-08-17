package com.abhi.grocery.main.home.domain.repository

import com.abhi.grocery.main.home.domain.ProductItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface InventoryRepository {
    val inventoryItems: Flow<List<ProductItem>>
    suspend fun initialiseInventory()

    suspend fun addItem(item: ProductItem)
    suspend fun removeItem(itemId: String)
    suspend fun updateItem(item: ProductItem)
}