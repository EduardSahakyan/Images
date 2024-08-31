package com.example.presentation.main

import androidx.lifecycle.ViewModel
import com.example.domain.models.Image
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel() {

    private val _effect = Channel<MainEffect>(BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun handleEvent(event: MainEvent) {
        when (event) {
            is MainEvent.OpenImagePreview -> openImagePreview(event.image)
        }
    }

    private fun openImagePreview(image: Image) {
        _effect.trySend(MainEffect.OpenImagePreview(image))
    }
}