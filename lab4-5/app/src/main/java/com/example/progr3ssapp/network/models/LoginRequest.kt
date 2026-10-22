package com.example.progr3ssapp.network.models

import kotlinx.serialization.Serializable

/** Bejelentkezéskor ezt küldjük (POST /auth/local/signin). */
@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)
