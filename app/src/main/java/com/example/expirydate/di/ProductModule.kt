package com.example.expirydate.di

import com.example.expirydate.database.ProductDAO
import com.example.expirydate.repository.ProductRepository
import com.example.expirydate.repository.ProductRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ProductModule {

    @Provides
    fun provideWordRepository(productDAO: ProductDAO): ProductRepository {
        return ProductRepositoryImpl(productDAO)
    }
}