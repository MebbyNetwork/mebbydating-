package com.example.mebby.util

import com.example.mebby.data.Resource

inline fun <T> safeNetworkCall(action: () -> Resource<T>): Resource<T> {
    return try {
        action()
    } catch (e: Exception) {
        Resource.Error(e.message ?: "An unknown Error")
    }
}