package com.example.data.utils

import android.util.Log
import com.example.domain.Resource

inline fun <T> safeNetworkCall(action: () -> Resource<T>): Resource<T> {
    return try {
        action()
    } catch (e: Exception) {
        Log.d("safeNetworkCall", "${e.message}")
        Resource.Error(e)
    }
}
