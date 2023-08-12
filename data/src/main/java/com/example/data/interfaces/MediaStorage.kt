package com.example.data.interfaces

import com.example.domain.Resource
import com.example.domain.models.PictureModel

interface MediaStorage {
    suspend fun uploadPicture(picture: PictureModel): Resource<PictureModel>
    suspend fun deletePicture(pictureId: String): Resource<Boolean>
    suspend fun getProfilePictures(userId: String): Resource<List<PictureModel>>
    suspend fun deleteAllMediaDate(userId: String): Resource<Boolean>
}
