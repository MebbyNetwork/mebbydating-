package com.example.mebby.domain.interfaces

import com.example.mebby.data.Resource
import com.example.mebby.domain.models.UserProfileModel
import com.example.mebby.domain.models.change.ProfileModel
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun createProfile(userProfile: ProfileModel): Flow<Resource<Boolean>>

    fun editProfile(userId: String, userProfile: ProfileModel): Flow<Resource<Boolean>>

    fun verifyProfile(userId: String, verificationType: String, verificationData: String): Flow<Resource<Boolean>>

    fun deactivateProfile(userId: String): Flow<Resource<Boolean>>

    fun removeProfile(userId: String): Flow<Resource<Boolean>>

    fun viewProfile(userId: String, viewedUserId: String): Flow<Resource<ProfileModel>>

    fun blockProfile(userId: String, blockedUserId: String): Flow<Resource<Boolean>>

    fun unblockProfile(userId: String, blockedUserId: String): Flow<Resource<Boolean>>

    fun reportProfile(userId: String, reportedUserId: String, reportReason: String): Flow<Resource<Boolean>>
}