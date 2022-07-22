package com.example.expirydate.viewModel

import android.net.Uri
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
class ProductsViewModel @Inject constructor(private val wordsRepository: ProductRepository) :
    ViewModel() {
    fun saveProductToDatabase(text: String) = liveData {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d("", "Delete Product succeeded")
                wordsRepository.insertProducts(Product(0, text, "12.12.2022", ""))
                emit(true)
            } catch (e: Exception) {
                Log.d("", "Delete Product failed")
                emit(false)
            }
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                wordsRepository.deleteProduct(product)
                Log.d("", "Delete Product succeeded.")
            } catch (e: Exception) {
                Log.d("", "Delete Product Failed.")
            }
        }
    }

    fun saveImageUrl(uri: String, productId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                wordsRepository.insertImageUrl(uri, productId)
                Log.d("", "Save Image Url succeeded.")
            } catch (e: Exception) {
                Log.d("", "Save Image Url Failed.")
            }
        }
    }

    fun getAllWords() = wordsRepository.getAllWords()
}