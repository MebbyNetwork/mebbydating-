package com.example.mebby.domain.interfaces

import com.example.mebby.data.Resource
import com.example.mebby.domain.models.InterestModel

interface RealtimeDatabaseRepository {
    suspend fun getInterestsList(): Resource<List<InterestModel>>
}