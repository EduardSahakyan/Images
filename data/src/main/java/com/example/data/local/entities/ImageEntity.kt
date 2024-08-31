package com.example.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("images")
data class ImageEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    val thumbnailUrl: String,
    val ownerName: String,
    val updatedAt: String,
    val orderType: String
)
