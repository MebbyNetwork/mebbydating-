package com.example.mebby.mapper

import com.example.domain.models.InterestModel
import com.example.domain.models.PictureModel
import com.example.domain.models.city.CityModel

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

fun MutableMap<String, Any>.interestsMapper(): InterestModel {
    val interestId = this["interestId"] as Long
    val interestValue = this["interestValue"] as String
    val interestsDescription = this["interestsDescription"] as String?
    val interestImage = this["interestImage"] as String?

    return InterestModel(
        interestId = interestId,
        interestValue = interestValue,
        interestsDescription = interestsDescription,
        interestImage = interestImage
    )
}

fun MutableMap<String, Any>.pictureMapper(): PictureModel {
    val pictureId = this["pictureId"] as String
    val userId = this["userId"] as String
    val imageUrl = this["imageUrl"] as String
    val uploadDate = this["uploadDate"] as Long
    val isProfilePicture = this["isProfilePicture"] as Boolean

    return PictureModel(
        pictureId = pictureId,
        userId = userId,
        imageUrl = imageUrl,
        uploadDate = uploadDate,
        isProfilePicture = isProfilePicture
    )
}