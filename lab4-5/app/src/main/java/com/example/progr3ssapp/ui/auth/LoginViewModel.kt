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

/** A Login képernyő teljes állapota egyetlen megváltoztathatatlan objektumban. */
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoggedIn: Boolean = false
)

// AndroidViewModel: így Context nélkül is elérjük a SessionManager-t
class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AuthRepository()
    private val sessionManager = SessionManager.getInstance(application)

    // Kívülről csak olvasható StateFlow látszik
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value, error = null) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value, error = null) }
    }

    /** A képernyő jelzi, hogy a hibát már megmutatta (így forgatás után nem jelenik meg újra). */
    fun onErrorShown() {
        _uiState.update { it.copy(error = null) }
    }

    fun login() {
        val state = _uiState.value
        val email = state.email.trim()

        validate(email, state.password)?.let { message ->
            _uiState.update { it.copy(error = message) }
            return
        }
        if (state.isLoading) return

        _uiState.update { it.copy(isLoading = true, error = null) }
        // A viewModelScope túléli a forgatást, a kérés nem szakad meg
        viewModelScope.launch {
            repository.login(email, state.password)
                .onSuccess { response ->
                    persistSession(response)
                    _uiState.update { it.copy(isLoading = false, isLoggedIn = true) }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = throwable.message ?: "Sikertelen bejelentkezés. Próbáld újra!"
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

    private fun validate(email: String, password: String): String? = when {
        email.isEmpty() -> "Az email megadása kötelező"
        !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Adj meg egy érvényes email címet"
        password.length < 6 -> "A jelszónak legalább 6 karakterből kell állnia"
        else -> null
    }
}
