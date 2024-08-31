package com.example.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Image(
    val id: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    val thumbnailUrl: String,
    val ownerName: String,
    val updatedAt: String
) : Parcelable
