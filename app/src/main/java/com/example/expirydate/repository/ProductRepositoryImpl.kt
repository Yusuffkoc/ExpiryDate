package com.example.expirydate.repository

import androidx.lifecycle.LiveData
import com.example.expirydate.database.ProductDAO
import com.example.expirydate.model.Product

class ProductRepositoryImpl(private val productDAO: ProductDAO) : ProductRepository {

    override suspend fun insertWords(product: Product) {
        productDAO.insertWord(product)
    }

    override  fun getAllWords(): LiveData<List<Product>> {
        return productDAO.getAllWords()
    }
}