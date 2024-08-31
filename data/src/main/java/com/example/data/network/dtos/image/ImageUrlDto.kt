package com.example.data.network.dtos.image

import com.google.gson.annotations.SerializedName

class ImageUrlDto(
    @SerializedName("raw")
    val raw: String,
    @SerializedName("thumb")
    val thumbnail: String
)