package com.example.domain

sealed class Resource<T>(val data: T? = null, val exception: Exception? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(message: Exception, data: T? = null) : Resource<T>(data, message)
}
