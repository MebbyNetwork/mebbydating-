package com.example.mebby.domain.models.city

import java.io.Serializable

data class CityModel(
    val city: String,
    val country: String,
    val countryCode: String,
    val id: Long,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val population: Long,
    val region: String,
    val regionCode: String,
    val type: String,
    val wikiDataId: String
): Serializable