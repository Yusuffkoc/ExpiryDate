package com.example.expirydate.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import com.example.expirydate.model.Product

interface ProductRepository {

    suspend fun insertProducts(product: Product)
    fun deleteProduct(product: Product)
    fun getAllWords(): LiveData<List<Product>>
    fun insertImageUrl(uri: String,productId: Int)
}