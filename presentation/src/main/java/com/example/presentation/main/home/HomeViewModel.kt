package com.example.presentation.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.dispatchers.AppDispatchers
import com.example.domain.models.Image
import com.example.domain.models.ImageOrderType
import com.example.domain.resource.Resource
import com.example.domain.usecases.GetImagesUseCase
import com.example.domain.usecases.SearchImagesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getImagesUseCase: GetImagesUseCase,
    private val searchImagesUseCase: SearchImagesUseCase,
    private val appDispatchers: AppDispatchers
): ViewModel() {

    private val _state = MutableStateFlow(State())
    val state = _state.asStateFlow()

    private val _effect = Channel<HomeEffect>(BUFFERED)
    val effect = _effect.receiveAsFlow()

    private var imageLoadingJob: Job? = null

    fun handleEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.ChangeOrderType -> changeOrderType(event.orderType)
            HomeEvent.LoadNextPage -> loadNextPage()
            is HomeEvent.ImageClicked -> openImagePreview(event.image)
            is HomeEvent.SearchQueryChanged -> changeSearchQuery(event.query)
            HomeEvent.Refresh -> refresh()
            HomeEvent.Search -> search()
            HomeEvent.DisableSearchMode -> disableSearchMode()
        }
    }

    private fun disableSearchMode() {
        imageLoadingJob?.cancel()
        imageLoadingJob = null
        _state.update { State() }
        loadNextPage()
    }

    private fun search() {
        imageLoadingJob?.cancel()
        imageLoadingJob = null
        _state.update { State(searchQuery = state.value.searchQuery, isSearchMode = true) }
        loadNextPage()
    }

    private fun refresh() {
        _state.update { it.copy(page = 0, isRefreshing = true) }
        loadNextPage()
    }

    private fun changeSearchQuery(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    private fun openImagePreview(image: Image) {
        _effect.trySend(HomeEffect.OpenImagePreview(image))
    }

    private fun changeOrderType(orderType: ImageOrderType) {
        if (imageLoadingJob != null) return
        _state.update { State(orderType = orderType) }
        loadNextPage()
    }

    private fun loadNextPage() {
        if (imageLoadingJob != null) return
        val page = state.value.page + 1
        val perPage = state.value.pageSize
        val orderType = state.value.orderType
        val searchQuery = state.value.searchQuery
        val dataFlow = if (state.value.isSearchMode) {
            searchImagesUseCase.invoke(searchQuery, page, perPage)
        } else {
            getImagesUseCase.invoke(page, perPage, orderType)
        }
        imageLoadingJob = dataFlow
            .flowOn(appDispatchers.io)
            .onEach { resource ->
                when (resource) {
                    is Resource.Error -> {
                        _state.update { it.copy(isError = true) }
                    }
                    Resource.Loading -> {
                        _state.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        if (resource.model.isNotEmpty()) {
                            val current = state.value.images.toMutableList()
                            current.addAll(resource.model)
                            _state.update {
                                it.copy(page = page, images = current.toList(), isError = false)
                            }
                        }
                    }
                }
            }
            .onCompletion {
                _state.update { it.copy(isLoading = false, isRefreshing = false) }
                imageLoadingJob = null
            }
            .flowOn(appDispatchers.main)
            .launchIn(viewModelScope)
    }

    data class State(
        val images: List<Image> = emptyList(),
        val page: Int = 0,
        val pageSize: Int = 10,
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val isRefreshing: Boolean = false,
        val searchQuery: String = "",
        val isSearchMode: Boolean = false,
        val orderType: ImageOrderType = ImageOrderType.LATEST
    )

}