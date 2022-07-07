package com.example.expirydate.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words")
data class Product(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var productName: String,
    var expiryDate: String
)