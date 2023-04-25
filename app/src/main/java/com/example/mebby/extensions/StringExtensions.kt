package com.example.mebby.extensions

import java.security.MessageDigest

fun String.convertToSHA256(): String {
    val bytes = MessageDigest.getInstance("SHA256").digest(this.toByteArray())
    return bytes.toHex()
}

fun String.convertToMD5(): String {
    val bytes = MessageDigest.getInstance("MD5").digest(this.toByteArray())
    return bytes.toHex()
}