package com.example.presentation.main

import com.example.domain.models.Image

sealed class MainEffect {
    data class OpenImagePreview(val image: Image): MainEffect()
}