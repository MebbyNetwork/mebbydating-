package com.example.mebby.extensions

fun ByteArray.toHex(): String {
    return joinToString("") { "%02x".format(it) }
}