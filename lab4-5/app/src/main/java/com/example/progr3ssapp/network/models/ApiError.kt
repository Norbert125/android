package com.example.progr3ssapp.network.models

import kotlinx.serialization.Serializable

/** Hibás kérésnél (4xx/5xx) ezt adja vissza a backend, pl. {"statusCode":403,"message":"Access denied"}. */
@Serializable
data class ApiError(
    val statusCode: Int? = null,
    val message: String? = null,
    val error: String? = null
)
