package com.example.mebby.domain.models

import android.net.Uri

data class SwipeCardModel(
    val name: String,
    val age: Int,
    val about: String? = null,
    val photo: String? = null,
    val gender: String? = null,
    val show: String? = null,
    val find: String? = null,
    val profile: String? = null,
)