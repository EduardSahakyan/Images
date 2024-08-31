package com.example.presentation.main.home

import com.example.domain.models.Image
import com.example.domain.models.ImageOrderType

sealed class HomeEvent {
    data object LoadNextPage: HomeEvent()
    data class ChangeOrderType(val orderType: ImageOrderType): HomeEvent()
    data class ImageClicked(val image: Image): HomeEvent()
    data class SearchQueryChanged(val query: String): HomeEvent()
    data object Refresh : HomeEvent()
    data object Search : HomeEvent()
    data object DisableSearchMode : HomeEvent()
}