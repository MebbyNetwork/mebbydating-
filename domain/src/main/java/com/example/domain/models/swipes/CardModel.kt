package com.example.domain.models.swipes

import com.example.domain.models.PictureModel
import com.example.domain.models.city.CityModel

data class CardModel(
    val username: String,
    val isVerification: Boolean,
    val picture: PictureModel,
    val city: CityModel,
    val about: String,
    val age: Long,
    val userId: String
)
