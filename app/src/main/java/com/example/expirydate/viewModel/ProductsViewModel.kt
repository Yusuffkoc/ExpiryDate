package com.example.expirydate.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.expirydate.model.Product
import com.example.expirydate.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(private val productRepository: ProductRepository) :
    ViewModel() {

    companion object {
        private const val TAG = "ProductsViewModel"
    }

    fun saveProductToDatabase(text: String) = liveData {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d(TAG, "saveProductToDatabase succeeded")
                productRepository.insertProducts(Product(0, text, "", "12.12.2022", ""))
                emit(true)
            } catch (e: Exception) {
                Log.d(TAG, "saveProductToDatabase failed" + e.message)
                emit(false)
            }
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                productRepository.deleteProduct(product)
                Log.d(TAG, "Delete Product succeeded.")
            } catch (e: Exception) {
                Log.d(TAG, "Delete Product Failed." + e.message)
            }
        }
    }

    fun saveImageUrl(uri: String, productId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                productRepository.insertImageUrl(uri, productId)
                Log.d(TAG, "Save Image Url succeeded.")
            } catch (e: Exception) {
                Log.d(TAG, "Save Image Url Failed." + e.message)
            }
        }
    }

    fun getProducts() = productRepository.getAllProducts()
}