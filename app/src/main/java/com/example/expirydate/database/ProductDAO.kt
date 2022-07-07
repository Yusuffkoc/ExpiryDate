package com.example.expirydate.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.expirydate.model.Product

@Dao
interface ProductDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(product: Product)

    @Query("SELECT * FROM words")
    fun getAllWords(): LiveData<List<Product>>

}