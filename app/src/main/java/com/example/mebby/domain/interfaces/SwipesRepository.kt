package com.example.mebby.domain.interfaces

import com.example.mebby.data.Resource
import com.example.mebby.domain.models.change.ProfileSearchCriteria
import com.example.mebby.domain.models.UserProfileModel
import kotlinx.coroutines.flow.Flow

interface SwipesRepository {
    fun getMatches(userId: String): Flow<Resource<List<UserProfileModel>>>
    fun getProfiles(userId: String): Flow<Resource<List<UserProfileModel>>>

    fun likeProfile(userId: String, likedUserId: String): Resource<Boolean>
    fun passProfile(userId: String, passedUserId: String): Resource<Boolean>

    fun searchProfiles(userId: String, criteria: ProfileSearchCriteria): Flow<Resource<List<UserProfileModel>>>
}
