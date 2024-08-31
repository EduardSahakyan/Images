package com.example.domain.resource

sealed class Resource<out T> {
    data class Success<T>(val model: T): Resource<T>()
    data class Error(val throwable: Throwable): Resource<Nothing>()
    data object Loading: Resource<Nothing>()
}