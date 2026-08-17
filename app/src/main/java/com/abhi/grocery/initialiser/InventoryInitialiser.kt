package com.abhi.grocery.initialiser

import com.abhi.grocery.main.home.domain.repository.InventoryRepository
import javax.inject.Inject

class InventoryInitialiser @Inject constructor(
    val inventoryRepository: InventoryRepository
) {
    suspend fun initialiseInventory() {
        inventoryRepository.initialiseInventory()
    }
}