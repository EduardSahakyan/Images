package com.example.presentation.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.presentation.main.addimage.AddImageTab
import com.example.presentation.main.home.HomeTab
import com.example.presentation.main.profile.ProfileTab

@Composable
fun MainContainer(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: MainViewModel
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Tab.Home.route
    ) {
        composable(route = Tab.Home.route) {
            HomeTab(
                openImagePreview = { viewModel.handleEvent(MainEvent.OpenImagePreview(it)) }
            )
        }
        composable(route = Tab.AddImage.route) {
            AddImageTab()
        }
        composable(route = Tab.Profile.route) {
            ProfileTab()
        }
    }
}