package com.example.presentation.activity

import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.domain.models.Image
import com.example.presentation.common.ImageNavType
import com.example.presentation.common.Screen
import com.example.presentation.common.theme.WebAntTheme
import com.example.presentation.imagepreview.ImagePreviewScreen
import com.example.presentation.main.MainScreen
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            WebAntTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController, 
                    startDestination = Screen.Main.route
                ) {
                    composable(route = Screen.Main.route) {
                        MainScreen(
                            openImagePreview = {
                                val json = Uri.encode(Gson().toJson(it))
                                navController.navigate("${Screen.ImagePreview.route}/$json")
                            }
                        )
                    }
                    composable(
                        route = Screen.ImagePreview.route + Screen.ImagePreview.ARG_IMAGE,
                        arguments = listOf(
                            navArgument(Screen.ImagePreview.IMAGE) {
                                type = ImageNavType()
                            }
                        )
                    ) {
                        val image = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            it.arguments?.getParcelable(Screen.ImagePreview.IMAGE, Image::class.java)
                        } else {
                            it.arguments?.getParcelable(Screen.ImagePreview.IMAGE)
                        }
                        image?.let {
                            ImagePreviewScreen(
                                navigateUp = {
                                    navController.navigateUp()
                                },
                                image = image
                            )
                        }
                    }
                }
            }
        }
    }
}