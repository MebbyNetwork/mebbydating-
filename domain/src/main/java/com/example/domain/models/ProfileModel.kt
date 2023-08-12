package com.example.domain.models

import com.example.domain.models.city.CityModel
import com.example.domain.sealed.Find
import com.example.domain.sealed.Gender
import com.example.domain.sealed.Show
import java.io.Serializable

data class ProfileModel(
    val isVerification: Boolean = false,
    val isBanned: Boolean = false,
    val userId: String? = null,

    val username: String,
    val about: String,

    val birthday: Long? = null,
    val dateOfRegistration: Long? = null,

    val city: CityModel,
    val interests: List<InterestModel>,
    val pictureProfile: PictureModel,
    val pictures: List<PictureModel>,

    val findType: String,
    val showType: String,
    val genderType: String
) : Serializable {
    fun isValid(): Boolean {
        if (username.isEmpty() || username == "") return false
        if (birthday == null || birthday < 0) return false
        if (interests.isEmpty()) return false
        if (pictures.isEmpty()) return false
        if (findType != Find.Dating.value || findType != Find.Partner.value || findType != Find.Friends.value) return false
        if (genderType != Gender.Male.value || findType != Gender.Female.value) return false
        if (showType != Show.Men.value || findType != Show.Women.value || findType != Show.Everyone.value) return false

        return true
    }
}
