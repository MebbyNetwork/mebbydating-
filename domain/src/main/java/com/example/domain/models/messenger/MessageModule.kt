package com.example.domain.models.messenger

import com.google.firebase.Timestamp

data class MessageModule(
    val userId: String,
    val username: String,
    val message: String,
    val time: Timestamp
)