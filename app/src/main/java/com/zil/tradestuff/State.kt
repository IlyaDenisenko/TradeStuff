package com.zil.tradestuff

sealed class State<T> {
    class Loading<T> : State<T>()
    data class Success<T>(val data: T): State<T>()
    data class Failed<T>(val message: T): State<T>()

    companion object{
        fun <T> loading() = Loading<T>()
        fun <T> success(data: T) = Success(data)
        fun <T> failed(message: T) = Failed(message)
    }
}