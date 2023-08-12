package com.example.domain.sealed

sealed class Birthday {
    object Day : Birthday()
    object Month : Birthday()
    object Year : Birthday()
}
