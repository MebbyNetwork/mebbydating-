package com.example.domain.sealed

sealed class Find(val value: String) {
    object Dating : Find("dating")
    object Friends : Find("friends")
    object Partner : Find("partner")
}
