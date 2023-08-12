package com.example.domain.models.city

import java.io.Serializable
import java.lang.Exception

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
) : Serializable {
    companion object {
        fun fromHashMap(map: HashMap<String, Any?>): CityModel {
            try {
                val city = map["city"] as String
                val country = map["country"] as String
                val countryCode = map["countryCode"] as String
                val id = map["id"] as Long
                val latitude = map["latitude"] as Double
                val longitude = map["longitude"] as Double
                val name = map["name"] as String
                val population = map["population"] as Long
                val region = map["region"] as String
                val regionCode = map["regionCode"] as String
                val type = map["type"] as String
                val wikiDataId = map["wikiDataId"] as String

                return CityModel(city, country, countryCode, id, latitude, longitude, name, population, region, regionCode, type, wikiDataId)
            } catch (e: Exception) {
                throw e
            }
        }
    }
}
