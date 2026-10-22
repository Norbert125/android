package com.example.progr3ssapp.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.json.JSONObject

@Serializable
data class ProfileDto(
    val id: Long? = null,
    val email: String? = null,
    val username: String? = null,
    val description: String? = null,
    val profileImageUrl: String? = null,
    val profileImageBase64: String? = null,
    val coverImageUrl: String? = null,
    val fcmToken: String? = null,
    val preferences: JSONObject? = null,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("updated_at") val updatedAt: String? = null
)
