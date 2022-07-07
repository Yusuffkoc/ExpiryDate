package com.example.expirydate.repository

import androidx.lifecycle.LiveData
import com.example.expirydate.model.Product

interface ProductRepository {

    suspend fun insertWords(product: Product)
    fun getAllWords(): LiveData<List<Product>>
}