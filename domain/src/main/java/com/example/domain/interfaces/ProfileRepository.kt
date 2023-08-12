package com.example.domain.interfaces

import com.example.domain.Resource
import com.example.domain.models.ProfileModel
import com.example.domain.models.ReportModel
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    suspend fun createProfile(profile: ProfileModel): Flow<Resource<Boolean>>

    suspend fun editProfile(profile: ProfileModel): Flow<Resource<Boolean>>

    suspend fun deleteProfile(): Flow<Resource<Boolean>>

    suspend fun viewProfile(userId: String?): Flow<Resource<ProfileModel>>

    suspend fun blockProfile(blockedUserId: String): Flow<Resource<Boolean>>

    suspend fun pardonProfile(blockedUserId: String): Flow<Resource<Boolean>>

    suspend fun reportProfile(report: ReportModel): Flow<Resource<Boolean>>
}
