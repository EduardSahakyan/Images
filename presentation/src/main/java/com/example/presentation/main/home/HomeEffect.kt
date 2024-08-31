package com.example.presentation.main.home

import com.example.domain.models.Image

sealed class HomeEffect {
    data class OpenImagePreview(val image: Image): HomeEffect()
}