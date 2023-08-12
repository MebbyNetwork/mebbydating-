package com.example.domain.sealed

sealed class Show(val value: String) {
    object Men : Show("men")
    object Women : Show("women")
    object Everyone : Show("everyone")
}

