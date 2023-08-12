package com.example.domain.models.messenger

class ChatModel(
    val userId: String,
    val username: String,
    val UserVerify: Boolean,
    val lastMessage: MessageModule
)