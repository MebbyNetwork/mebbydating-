package com.example.mebby.mapper

import com.example.mebby.domain.models.ImageModel
import com.example.mebby.domain.models.InterestModel
import com.example.mebby.domain.models.city.CityModel

fun MutableMap<String, Any>.cityMapper(): CityModel {
    val city = this["city"] as String
    val country = this["country"] as String
    val countryCode = this["countryCode"] as String
    val id = this["id"] as Long
    val latitude = this["latitude"] as Double
    val longitude = this["longitude"] as Double
    val name = this["name"] as String
    val population = this["population"] as Long
    val region = this["region"] as String
    val regionCode = this["regionCode"] as String
    val type =  this["type"] as String
    val wikiDataId = this["wikiDataId"] as String

    return CityModel(
        city, country, countryCode, id, latitude, longitude, name, population, region, regionCode, type, wikiDataId
    )
}

fun MutableMap<String, Any>.interestMapper(): InterestModel {
    val label = this["label"] as String?
    val key = this["key"] as Long
    val value = this["value"] as String

    return InterestModel(
        label = label, key = key, value = value
    )
}

fun MutableMap<String, Any>.imageMapper(): ImageModel {
    val uri = this[com.example.mebby.const.PictureConstants.URI] as String
    val timestamp = this[com.example.mebby.const.PictureConstants.TIMESTAMP] as Long
    val generalImage = this[com.example.mebby.const.PictureConstants.GENERAL_IMAGE] as Boolean
    val request = this[com.example.mebby.const.PictureConstants.REQUEST_TYPE] as Long

    return ImageModel(
        uri = uri,
        timestamp = timestamp,
        generalImage = generalImage,
        request = request
    )
}