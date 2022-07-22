package com.example.expirydate.database

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.expirydate.model.Product

@Dao
interface ProductDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(product: Product)

    @Query("UPDATE products SET imageUrl = :uri WHERE id LIKE :productId ")
    fun insertImageUrl(uri: String, productId: Int)

    @Query("SELECT * FROM products ORDER BY id ASC")
    fun getAllWords(): LiveData<List<Product>>

    @Delete
    fun deleteProduct(product: Product)

}