package com.example.mebby.domain.models.change

data class PictureModel(
    val pictureId: String,
    val userId: String,
    val imageUrl: String,
    val uploadDate: String,
    val isProfilePicture: Boolean
)
