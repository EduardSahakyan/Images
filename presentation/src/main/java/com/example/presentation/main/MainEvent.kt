package com.example.presentation.main

import com.example.domain.models.Image

sealed class MainEvent {
    data class OpenImagePreview(val image: Image): MainEvent()
}