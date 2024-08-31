package com.example.presentation.main

import androidx.annotation.DrawableRes
import com.example.presentation.R

sealed class Tab(val route: String, @DrawableRes val drawable: Int) {
    data object Home: Tab(ROUTE_HOME, R.drawable.ic_home)
    data object AddImage: Tab(ROUTE_ADD_IMAGE, R.drawable.ic_add_image)
    data object Profile: Tab(ROUTE_PROFILE, R.drawable.ic_profile)

    companion object {
        private const val ROUTE_HOME = "home"
        private const val ROUTE_ADD_IMAGE = "add_image"
        private const val ROUTE_PROFILE = "profile"
    }

}