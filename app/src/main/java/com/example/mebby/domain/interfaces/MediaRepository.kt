package com.example.mebby.domain.interfaces

import com.example.mebby.data.Resource
import com.example.mebby.domain.models.ImageModel

interface MediaRepository {
    fun uploadProfilePicture(userId: String, picture: ImageModel): Resource<Boolean>
    fun deleteProfilePicture(userId: String, pictureId: String): Resource<Boolean>
}