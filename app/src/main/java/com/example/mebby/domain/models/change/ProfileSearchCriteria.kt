package com.example.mebby.domain.models.change

import com.example.mebby.domain.models.InterestModel
import com.example.mebby.domain.models.city.CityModel
import com.example.mebby.enums.FindTypes
import com.example.mebby.enums.GenderTypes

data class ProfileSearchCriteria(
    val location: CityModel,
    val genderShow: GenderTypes,
    val findShow: FindTypes,
    val ageRange: IntRange,
    val interests: List<InterestModel>
)