package com.example.mebby.extensions

fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
    if (index1 < 0 || index1 >= size || index2 < 0 || index2 >= size) {
        throw IndexOutOfBoundsException("Invalid indices provided")
    }

    val tmp = this[index1]
    this[index1] = this[index2]
    this[index2] = tmp
}