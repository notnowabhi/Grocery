package com.abhi.grocery

import android.app.Application
import android.util.Log
import com.abhi.grocery.initialiser.InventoryInitialiser
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class GroceryApp: Application() {
    @Inject
    lateinit var inventoryInitialiser: InventoryInitialiser

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("Abhi", "onCreate: initialise oncreate called")
            inventoryInitialiser.initialiseInventory()
        }
    }
}