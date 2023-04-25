package com.example.mebby.domain.models.change

data class SwipeCardModel(
    val userId: String,
    val profileImageUrl: String,
    val name: String,
    val age: Long,
    val location: String,
    val interests: List<InterestModel>? = null,
    val pictures: List<PictureModel>? = null
)