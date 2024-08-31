package com.example.presentation.imagepreview

sealed class ImagePreviewEvent {

    data object OnBackClicked : ImagePreviewEvent()

}