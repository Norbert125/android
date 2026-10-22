package com.example.progr3ssapp.network

import com.example.progr3ssapp.network.models.ApiError
import com.example.progr3ssapp.network.models.LoginRequest
import com.example.progr3ssapp.network.models.LoginResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File

/**
 * A nyers Response<T>-ből Result<T>-t csinál (siker vagy olvasható hibaüzenet).
 * A runCatching minden kivételt (hálózati hiba, error(...)) Result.failure-be csomagol,
 * így a hívónak nem kell try/catch.
 */
class AuthRepository(
    private val api: AuthApi = RetrofitClient.authApi
) {

    suspend fun login(email: String, password: String): Result<LoginResponse> = runCatching {
        val response = api.signIn(LoginRequest(email = email, password = password))
        response.requireBody("Sikertelen bejelentkezés")
    }

    suspend fun register(
        username: String,
        email: String,
        password: String,
        profileImage: File? = null
    ): Result<LoginResponse> = runCatching {
        val textType = "text/plain".toMediaType()
        val imagePart = profileImage?.let {
            MultipartBody.Part.createFormData(
                "profileImage",
                it.name,
                it.asRequestBody("image/*".toMediaType())
            )
        }
        val response = api.signUp(
            username = username.toRequestBody(textType),
            email = email.toRequestBody(textType),
            password = password.toRequestBody(textType),
            profileImage = imagePart
        )
        response.requireBody("Sikertelen regisztráció")
    }

    private fun Response<LoginResponse>.requireBody(fallbackMessage: String): LoginResponse {
        if (isSuccessful) {
            return body() ?: error("Üres válasz a szervertől")
        }
        error(parseErrorMessage(this, fallbackMessage))
    }

    /** A backend {"statusCode", "message"} hibatörzséből kiolvassa az üzenetet. */
    private fun parseErrorMessage(response: Response<*>, fallbackMessage: String): String {
        val raw = response.errorBody()?.string()
        if (!raw.isNullOrBlank()) {
            val parsed = try {
                RetrofitClient.json.decodeFromString(ApiError.serializer(), raw)
            } catch (e: Exception) {
                null
            }
            if (parsed?.message != null) {
                return parsed.message
            }
        }
        val message = response.message()
        if (message.isNullOrBlank()) {
            return "$fallbackMessage (${response.code()})"
        }
        return message
    }
}
