package com.example.mqtt_subscriber.model

sealed class UiState<out T>  {
    data class Success<out T>(val data: T? = null) : UiState<T>()
    data class Loading(val nothing: Nothing?=null) : UiState<Nothing>()
    data class Error(val msg: String?) : UiState<Nothing>()
}