package com.example.mebby.domain.models

import java.io.Serializable

data class ImageModel(
    val uri: String,
    val timestamp: Long? = null,
    val generalImage: Boolean = false,
    val request: Long? = null,
) : Serializable