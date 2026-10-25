package com.abhi.grocery.initialiser

import android.content.Context
import com.abhi.grocery.main.home.domain.repository.InventoryRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class InventoryInitialiser @Inject constructor(
    val inventoryRepository: InventoryRepository,
    @param:ApplicationContext private val context: Context
) {
    suspend fun initialiseInventory() {
        copyBundledProductImages()
        inventoryRepository.initialiseInventory()
    }

    // catalog photos ship in assets; the cards read them from filesDir
    private fun copyBundledProductImages() {
        val names = context.assets.list("product_images") ?: return
        val destination = File(context.filesDir, "product_images")
        if (!destination.exists()) destination.mkdirs()

        for (name in names) {
            if (!name.endsWith(".jpg")) continue
            val file = File(destination, name)
            context.assets.open("product_images/$name").use { input ->
                file.outputStream().use { output -> input.copyTo(output) }
            }
        }
    }
}