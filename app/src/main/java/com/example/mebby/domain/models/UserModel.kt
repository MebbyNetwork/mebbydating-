package com.example.mebby.domain.models

import com.example.mebby.enums.FindTypes
import com.example.mebby.enums.GenderTypes
import com.example.mebby.enums.ShowTypes
import com.example.mebby.domain.models.city.CityModel
import java.sql.Timestamp

data class UserModel(
    val name: String,
    val birthday: Timestamp,
    val gender: GenderTypes,
    val find: FindTypes,
    val show: ShowTypes,
    val images: List<ImageModel>,
    val interest: List<InterestModel>,
    val about: String,
    val city: CityModel
)
