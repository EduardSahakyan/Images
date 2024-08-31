package com.example.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.domain.models.Image
import com.example.presentation.common.theme.ColorSelected
import com.example.presentation.common.theme.ColorUnselected
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MainScreen(
    openImagePreview: (Image) -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {

    val navController = rememberNavController()
    val tabs = remember {
        listOf(Tab.Home, Tab.AddImage, Tab.Profile)
    }

    LaunchedEffect(key1 = "main_screen") {
        viewModel.effect.collectLatest {
            when (it) {
                is MainEffect.OpenImagePreview -> openImagePreview(it.image)
            }
        }
    }

    Scaffold(
        content = {
            MainContainer(
                modifier = Modifier
                    .background(Color.White)
                    .padding(it),
                navController = navController,
                viewModel = viewModel
            )
        },
        bottomBar = {
            Column(
                Modifier
                    .navigationBarsPadding()
                    .height(56.dp)
            ) {
                HorizontalDivider(
                    color = Color.Gray
                )
                BottomAppBar(
                    containerColor = Color.White
                ) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    tabs.forEach { tab ->
                        NavigationBarItem(
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = Color.Transparent,
                                selectedIconColor = ColorSelected,
                                unselectedIconColor = ColorUnselected
                            ),
                            icon = {
                                Icon(
                                    painter = painterResource(id = tab.drawable),
                                    contentDescription = null
                                )
                            },
                            selected = currentDestination?.hierarchy?.any { it.route == tab.route } == true,
                            onClick = {
                                navController.navigate(tab.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    )
}
