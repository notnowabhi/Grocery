package com.abhi.grocery.main.home.data.repository

import android.util.Log
import com.abhi.grocery.common.utils.sampleItemsList
import com.abhi.grocery.main.assets.ProductAssetDataSource
import com.abhi.grocery.main.home.data.local.ProductDao
import com.abhi.grocery.main.home.data.mapper.toEntity
import com.abhi.grocery.main.home.data.mapper.toProductEntity
import com.abhi.grocery.main.home.data.mapper.toProductEntity
import com.abhi.grocery.main.home.data.mapper.toProductItem
import com.abhi.grocery.main.home.domain.ProductItem
import com.abhi.grocery.main.home.domain.repository.InventoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class InventoryRepositoryImpl @Inject constructor(
    private val productDao: ProductDao,
    private val productAssetDataSource: ProductAssetDataSource
) : InventoryRepository {
    override val inventoryItems: Flow<List<ProductItem>> =
        productDao.getAllProducts()
            .map { products->
                products.map { it.toProductItem() }
            }


    override suspend fun addItem(item: ProductItem) {
        productDao.insertProduct(
            item.toProductEntity()
        )
    }

    override suspend fun removeItem(itemId: String) {
        productDao.deleteProductById(itemId)
    }

    override suspend fun updateItem(item: ProductItem) {
        productDao.updateProduct(
            item.toProductEntity()
        )
    }

    override suspend fun initialiseInventory() {
        Log.d("Abhi", "initialiseInventory: repo impl initialise called 0")
        if(productDao.getProductCount() > 0) return
        Log.d("Abhi", "initialiseInventory: repo impl initialise called 1")

        val productsJson = productAssetDataSource.loadProducts()
        val products = productsJson.map { it.toEntity() }

        productDao.insertProducts(products)
        Log.d("Abhi", "initialiseInventory: repo impl initialise called 2")
    }
}