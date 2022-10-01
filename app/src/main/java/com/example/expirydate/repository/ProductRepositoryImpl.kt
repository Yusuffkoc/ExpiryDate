package com.example.expirydate.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import com.example.expirydate.database.ProductDAO
import com.example.expirydate.model.Product

class ProductRepositoryImpl(private val productDAO: ProductDAO) : ProductRepository {

    override suspend fun insertProducts(product: Product) {
        productDAO.insertWord(product)
    }

    override fun deleteProduct(product: Product) {
        productDAO.deleteProduct(product)
    }

    override fun getAllProducts(): LiveData<List<Product>> {
        return productDAO.getAllWords()
    }

    override fun insertImageUrl(uri: String, productId: Int) {
        productDAO.insertImageUrl(uri, productId)
    }

    override suspend fun updateProduct(product: Product) {
        productDAO.updateProduct(product)
    }
}