package com.example.data.network.dtos.image

import com.google.gson.annotations.SerializedName

class ImageDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("slug")
    val name: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("urls")
    val urls: ImageUrlDto,
    @SerializedName("user")
    val owner: ImageOwnerDto,
    @SerializedName("updated_at")
    val updatedAt: String
)