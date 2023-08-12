package com.example.data.utils

import com.google.firebase.auth.FirebaseAuth

fun FirebaseAuth.isUserInvalid(): Boolean {
    return this.currentUser == null
}
