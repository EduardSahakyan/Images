package com.example.presentation.imagepreview

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class ImagePreviewViewModel @Inject constructor(): ViewModel() {

    private val _effect = Channel<ImagePreviewEffect>(BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun handleEvent(event: ImagePreviewEvent) {
        when (event) {
            ImagePreviewEvent.OnBackClicked -> navigateUp()
        }
    }

    private fun navigateUp() {
        _effect.trySend(ImagePreviewEffect.NavigateUp)
    }

}