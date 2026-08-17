package com.abhi.grocery.main.assets

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ProductAssetDataSource @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private val gson = Gson()

    fun loadProducts(): List<ProductJson> {
        val json = context.assets
            .open("products.json")
            .bufferedReader()
            .use { it.readText() }

        val type = object : TypeToken<List<ProductJson>>() {}.type

        return gson.fromJson(json, type)
    }
}