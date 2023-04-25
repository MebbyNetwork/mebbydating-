package com.example.mebby.const

import android.app.Activity
import com.example.mebby.domain.models.ImageModel
import com.example.mebby.domain.models.InterestModel
import com.example.mebby.domain.models.change.PictureModel
import com.example.mebby.domain.models.city.CityModel
import com.example.mebby.enums.FindTypes
import com.example.mebby.enums.GenderTypes
import com.example.mebby.enums.ShowTypes
import java.sql.Timestamp

lateinit var ACTIVITY: Activity

const val DATABASE_REFERENCE = "https://mebbydating-2eb1d-default-rtdb.europe-west1.firebasedatabase.app/"
const val STORAGE_REFERENCE = "gs://mebbydating-2eb1d.appspot.com"

const val INTERESTS_PATH = "interests"
const val LANGUAGES_PATH = "languages"
const val EU_PATH = "eu"

const val USERS_CHILD = "users"
const val IMAGES_CHILD = "images"

const val REQUEST_TAKE_PHOTO = 5401
const val REQUEST_PERMISSION_TAKE_PHOTO = 5411
const val REQUEST_SELECT_PHOTO_IN_GALLERY = 5402
const val REQUEST_PERMISSION_SELECT_PHOTO_IN_GALLERY = 5412

const val CITY_VALUE = "CITY"

const val NAME = "username"
const val BIRTHDAY = "birthday"
const val GENDER = "gender"
const val FIND = "find"
const val SHOW = "show"
const val PHOTOS = "photos"
const val PROFILE_IMAGES = "profileImages"
const val GENERAL_IMAGE = "generalImage"
const val INTEREST = "interests"
const val ABOUT = "about"
const val CITY = "city"
const val DATE_OF_REGISTRATION = "dateOfRegistration"
const val PHONE_NUMBER = "phoneNumber"
const val VERIFICATION = "KYC"

const val EDIT_PROFILE = "editProfile"
const val USER = "user"

const val NONE = "none"

const val SWW = "Something went wrong"

object PictureConstants {
    const val URI = "uri"
    const val TIMESTAMP = "timestamp"
    const val GENERAL_IMAGE = "generalImage"
    const val REQUEST_TYPE = "request"
}

object ProfileConstants {
    const val USERNAME = "username"
    const val ABOUT = "about"
    const val BIRTHDAY = "dateOfBirthday"
    const val DATE_OF_REGISTRATION = "dateOfRegistration"
    const val CITY = "city"
    const val INTERESTS = "interests"
    const val PICTURE_PROFILE = "pictureProfile"
    const val PICTURES = "pictures"
    const val FIND_TYPE = "findType"
    const val SHOW_TYPE = "showType"
    const val GENDER_TYPE = "genderType"
    const val IS_VERIFICATION = "isVerification"
    const val PHONE_NUMBER_HASH = "PhoneNumberHash"
}

const val DATE_PATTERN = "dd/MM/yyyy"