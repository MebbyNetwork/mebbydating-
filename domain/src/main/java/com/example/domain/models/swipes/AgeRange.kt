package com.example.domain.models.swipes

import com.example.domain.Resource

data class AgeRange(
    val startAge: Int,
    val endAge: Int,
) {
    companion object {
        fun fromHashMap(map: HashMap<String, Any?>): AgeRange {
            return AgeRange(
                startAge = (map["startAge"] as Long).toInt(),
                endAge = (map["endAge"] as Long).toInt(),
            )
        }
    }
}
