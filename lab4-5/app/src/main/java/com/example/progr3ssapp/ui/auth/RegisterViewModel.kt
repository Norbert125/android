package com.example.progr3ssapp.ui.auth

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.progr3ssapp.network.AuthRepository
import com.example.progr3ssapp.network.models.LoginResponse
import com.example.progr3ssapp.utils.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegisterUiState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoggedIn: Boolean = false
)

/** Ugyanaz a felépítés, mint a LoginViewModel-nél, csak felhasználónévvel és multipart hívással. */
class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AuthRepository()
    private val sessionManager = SessionManager.getInstance(application)

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onUsernameChange(value: String) {
        _uiState.update { it.copy(username = value, error = null) }
    }

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value, error = null) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value, error = null) }
    }

    fun onErrorShown() {
        _uiState.update { it.copy(error = null) }
    }

    fun register() {
        val state = _uiState.value
        val username = state.username.trim()
        val email = state.email.trim()

        validate(username, email, state.password)?.let { message ->
            _uiState.update { it.copy(error = message) }
            return
        }
        if (state.isLoading) return

        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            repository.register(
                username = username,
                email = email,
                password = state.password
            )
                .onSuccess { response ->
                    // A signup is session-t ad vissza, így regisztráció után már be is vagyunk lépve
                    persistSession(response)
                    _uiState.update { it.copy(isLoading = false, isLoggedIn = true) }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = throwable.message ?: "Sikertelen regisztráció. Próbáld újra!"
                        )
                    }
                }
        }
    }

    private fun persistSession(response: LoginResponse) {
        sessionManager.saveSession(
            accessToken = response.accessToken,
            refreshToken = response.refreshToken,
            userId = response.user?.id,
            email = response.user?.email,
            username = response.user?.profile?.username
        )
    }

    private fun validate(username: String, email: String, password: String): String? = when {
        username.isEmpty() -> "A felhasználónév megadása kötelező"
        email.isEmpty() -> "Az email megadása kötelező"
        !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Adj meg egy érvényes email címet"
        password.length < 6 -> "A jelszónak legalább 6 karakterből kell állnia"
        else -> null
    }
}
