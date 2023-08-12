package com.example.domain.models

data class BlockedProfileModel(
    val userId: String,
    val blockedUUID: String,
    val blockedUserId: String,
    val blockTime: Long
)
