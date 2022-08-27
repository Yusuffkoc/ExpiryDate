package com.example.expirydate.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expirydate.model.Product
import com.example.expirydate.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(private val productRepository: ProductRepository) :
    ViewModel() {

    companion object {
        private const val TAG = "AddProductViewModel"
    }

    fun addProductToDatabase(productName: String, productDetail: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d(TAG, "insert product succeeded")
                productRepository.insertProducts(
                    Product(
                        0,
                        productName,
                        productDetail,
                        "12.12.2022",
                        ""
                    )
                )
            } catch (e: Exception) {
                Log.d(TAG, "insert product failed " + e.message)
            }
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                productRepository.updateProduct(product)
                Log.d(TAG, "Update Product succeed.")
            } catch (e: Exception) {
                Log.d(TAG, "Update Project failed." + e.message)
            }

        }
    }
}