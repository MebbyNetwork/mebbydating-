package com.example.domain.sealed

sealed class AuthStates {
    object IsRegistered : AuthStates()
    object IsNotRegistered: AuthStates()
    object IsNotLogged : AuthStates()
}