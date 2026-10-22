package com.example.progr3ssapp.network.models

import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/** A modellek a backend valódi válaszaival (docker, POST /auth/local/signin) működnek-e. */
class NetworkModelsTest {

    // Alapbeállítású (szigorú) Json: elbukik, ha egy mezőnév nem egyezik a backendével
    private val json = Json

    @Test
    fun loginRequest_serializesToBackendFormat() {
        val body = json.encodeToString(LoginRequest("a@b.hu", "Teszt12345"))

        assertEquals("""{"email":"a@b.hu","password":"Teszt12345"}""", body)
    }

    @Test
    fun loginResponse_parsesRealSignInResponse() {
        val response = json.decodeFromString<LoginResponse>(SIGN_IN_RESPONSE)

        assertEquals("Login successful", response.message)
        assertEquals("access-123", response.accessToken)
        assertEquals("refresh-456", response.refreshToken)

        val user = response.user!!
        assertEquals(1L, user.id)
        assertEquals("local", user.authProvider)

        val profile = user.profile!!
        assertEquals("claude_test", profile.username)
        assertEquals("2026-10-22T13:50:11.200Z", profile.createdAt)
        assertEquals("2026-10-22T13:50:11.200Z", profile.updatedAt)
        assertNull(profile.description)
        assertNull(profile.preferences)
        assertNull(profile.profileImageBase64) // a válaszban nincs benne -> alapérték
    }

    @Test
    fun loginResponse_missingFieldsFallBackToNull() {
        val response = json.decodeFromString<LoginResponse>("{}")

        assertNull(response.user)
        assertNull(response.accessToken)
    }

    @Test
    fun apiError_parsesRealErrorResponse() {
        val error = json.decodeFromString<ApiError>("""{"statusCode":403,"message":"Access denied"}""")

        assertEquals(403, error.statusCode)
        assertEquals("Access denied", error.message)
        assertNull(error.error)
    }

    private companion object {
        val SIGN_IN_RESPONSE = """
            {
              "message": "Login successful",
              "user": {
                "id": 1,
                "email": "claude.test@example.com",
                "auth_provider": "local",
                "profile": {
                  "id": 1,
                  "email": "claude.test@example.com",
                  "username": "claude_test",
                  "description": null,
                  "profileImageUrl": null,
                  "coverImageUrl": null,
                  "fcmToken": "",
                  "preferences": null,
                  "created_at": "2026-10-22T13:50:11.200Z",
                  "updated_at": "2026-10-22T13:50:11.200Z"
                }
              },
              "tokens": {
                "accessToken": "access-123",
                "refreshToken": "refresh-456"
              }
            }
        """.trimIndent()
    }
}
