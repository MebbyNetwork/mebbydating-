package com.example.domain.models

import java.io.Serializable

data class InterestModel(
    val interestId: Long,
    val interestValue: String,
    val interestsDescription: String? = null,
    val interestImage: String? = null
) : Serializable {
    companion object {
        fun fromHashMap(map: HashMap<String, Any?>): InterestModel {
            val interestId = map["interestId"] as Long
            val interestValue = map["interestValue"] as String
            val interestsDescription = map["interestsDescription"] as String?
            val interestImage = map["interestImage"] as String?

            return InterestModel(interestId, interestValue, interestsDescription, interestImage)
        }
    }
}
