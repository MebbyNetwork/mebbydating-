package com.example.domain.models

import java.io.Serializable
import java.lang.Exception

data class PictureModel(
    val pictureId: String? = null,
    val userId: String? = null,
    val imageUrl: String,
    val uploadDate: Long? = null,
    val isProfilePicture: Boolean = false
) : Serializable {
    companion object {
        fun fromHashMap(map: HashMap<String, Any>): PictureModel {
            try {
                return PictureModel(
                    pictureId = map["pictureId"] as String,
                    userId = map["userId"] as String,
                    imageUrl = map["imageUrl"] as String,
                    uploadDate = map["uploadDate"] as Long,
                    isProfilePicture = map["profilePicture"] as Boolean
                )
            } catch (e: Exception) {
                throw e
            }
        }
    }
}
