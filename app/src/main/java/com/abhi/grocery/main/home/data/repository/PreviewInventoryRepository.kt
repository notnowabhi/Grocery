package com.abhi.grocery.main.home.data.repository

import com.abhi.grocery.common.utils.sampleItemsList
import com.abhi.grocery.main.home.domain.ProductItem
import com.abhi.grocery.main.home.domain.repository.InventoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PreviewInventoryRepository : InventoryRepository {

    private val _inventoryItems =
        MutableStateFlow<List<ProductItem>>(sampleItemsList)

    override val inventoryItems =
        _inventoryItems.asStateFlow()

    override suspend fun addItem(item: ProductItem) {
        _inventoryItems.update { it + item }
    }

    override suspend fun removeItem(itemId: String) {
        _inventoryItems.update {
            it.filterNot { item -> item.id == itemId }
        }
    }

    override suspend fun updateItem(item: ProductItem) {
        _inventoryItems.update { current ->
            current.map {
                if (it.id == item.id) item else it
            }
        }
    }

    override suspend fun initialiseInventory() { }
}