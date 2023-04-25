package com.example.mebby.domain.interfaces

import com.example.mebby.data.Resource

interface CommonRepository {
    fun sendNotification(userId: String, notificationType: String, message: String): Resource<Boolean>
}