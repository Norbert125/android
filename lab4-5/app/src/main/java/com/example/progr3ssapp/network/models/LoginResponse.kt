package com.example.progr3ssapp.network.models

import kotlinx.serialization.Serializable

/** A backend válasza bejelentkezésre és regisztrációra. */
@Serializable
data class LoginResponse(
    val message: String? = null,
    val user: UserDto? = null,
    val tokens: TokensDto? = null
) {
    val accessToken: String? get() = tokens?.accessToken
    val refreshToken: String? get() = tokens?.refreshToken
}
