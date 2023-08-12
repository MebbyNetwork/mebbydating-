package com.example.domain.models.swipes

import com.example.domain.interfaces.SwipesRepository
import com.example.domain.models.InterestModel
import com.example.domain.models.city.CityModel

data class SwipesFiltersModel(
    val location: CityModel? = null,
    val show: String? = null,
    val find: String? = null,
    val ageRange: AgeRange? = null,
    val interest: List<InterestModel>? = null
) {
    fun toHashMap(): HashMap<String, Any?> {
        return hashMapOf<String, Any?>(
            "location" to this.location,
            "show" to this.show,
            "find" to this.find,
            "ageRange" to this.ageRange,
            "interest" to this.interest
        )
    }

    companion object {
        fun fromHashMap(map: HashMap<String, Any?>): SwipesFiltersModel {
            return SwipesFiltersModel(
                location = CityModel.fromHashMap(map["location"] as HashMap<String, Any?>),
                ageRange = AgeRange.fromHashMap(map["ageRange"] as HashMap<String, Any?>),
                interest = (map["interest"] as List<HashMap<String, Any?>>?)?.map { InterestModel.fromHashMap(it) },
                find = map["find"].toString(),
                show = map["show"].toString()
            )
        }
    }
}
