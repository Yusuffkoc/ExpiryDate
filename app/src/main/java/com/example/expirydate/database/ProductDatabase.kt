package com.example.expirydate.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.expirydate.model.Product

@Database(entities = [Product::class], exportSchema = false, version = 1)
abstract class ProductDatabase : RoomDatabase() {

    abstract fun wordDao(): ProductDAO
}