package com.example.mebby.domain.models.change

import com.example.mebby.domain.models.city.CityModel

data class ProfileModel(
    val isVerification: Boolean = false,

    val username: String,
    val about: String,

    val birthday: Long,
    val dateOfRegistration: Long,

    val city: CityModel,
    val interests: List<InterestModel>,
    val pictureProfile: PictureModel,
    val pictures: List<PictureModel>,

    val findType: String,
    val showType: String,
    val genderType: String,
)