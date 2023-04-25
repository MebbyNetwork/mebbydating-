package com.example.mebby.domain.models.change

data class InterestModel(
    val interestId: Long,
    val interestName: String,
    val interestValue: String,
    val interestsDescription: String? = null,
    val interestImage: String? = null
)