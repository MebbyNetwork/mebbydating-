package com.example.domain.sealed

sealed class Gender(val value: String) {
    object Male : Gender("male")
    object Female : Gender("female")
}
