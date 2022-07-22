package com.example.expirydate.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var productName: String,
    var expiryDate: String,
    var imageUrl: String
)