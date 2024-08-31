package com.example.presentation.common

import android.os.Build
import android.os.Bundle
import androidx.navigation.NavType
import com.example.domain.models.Image
import com.google.gson.Gson

sealed class Screen(val route: String) {

    data object Main : Screen(ROUTE_MAIN)
    data object ImagePreview : Screen(ROUTE_IMAGE_PREVIEW) {
        const val ARG_IMAGE = "/{image}"
        const val IMAGE = "image"
    }

    companion object {
        private const val ROUTE_MAIN = "main"
        private const val ROUTE_IMAGE_PREVIEW = "image_preview"
    }

}

class ImageNavType : NavType<Image>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): Image? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, Image::class.java)
        } else {
            bundle.getParcelable(key)
        }
    }

    override fun parseValue(value: String): Image {
        return Gson().fromJson(value, Image::class.java)
    }

    override fun put(bundle: Bundle, key: String, value: Image) {
        bundle.putParcelable(key, value)
    }
}