package com.example.domain.interfaces

import com.example.domain.Resource
import kotlinx.coroutines.flow.Flow

interface MessengerRepository {
    fun getChats(): Flow<Resource<>>
}