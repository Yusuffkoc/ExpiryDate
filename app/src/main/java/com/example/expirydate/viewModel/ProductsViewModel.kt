package com.example.expirydate.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.expirydate.model.Product
import com.example.expirydate.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(private val wordsRepository: ProductRepository) :
    ViewModel() {
    fun saveWord(text: String) = liveData {
        try {
            wordsRepository.insertWords(Product(0, text,"12.12.2022"))
            emit(true)
        } catch (e: Exception) {
            emit(false)
        }
    }

    fun getAllWords() = wordsRepository.getAllWords()
}