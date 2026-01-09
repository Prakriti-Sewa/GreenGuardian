package `in`.co.abdev.greenguardian.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `in`.co.abdev.greenguardian.data.model.User
import `in`.co.abdev.greenguardian.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
        val isLoading: Boolean = false,
        val isLoggedIn: Boolean = false,
        val user: User? = null,
        val token: String? = null,
        val error: String? = null,
        val registrationSuccess: Boolean = false
)

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Email and password are required")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            repository
                    .login(email, password)
                    .onSuccess { response ->
                        _uiState.value =
                                _uiState.value.copy(
                                        isLoading = false,
                                        isLoggedIn = true,
                                        user = response.user,
                                        token = response.token
                                )
                    }
                    .onFailure { error ->
                        _uiState.value =
                                _uiState.value.copy(
                                        isLoading = false,
                                        error = error.message ?: "Login failed"
                                )
                    }
        }
    }

    fun register(name: String, email: String, password: String) {
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "All fields are required")
            return
        }

        if (password.length < 6) {
            _uiState.value = _uiState.value.copy(error = "Password must be at least 6 characters")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            repository
                    .register(name, email, password)
                    .onSuccess { response ->
                        _uiState.value =
                                _uiState.value.copy(
                                        isLoading = false,
                                        isLoggedIn = true,
                                        user = response.user,
                                        token = response.token,
                                        registrationSuccess = true
                                )
                    }
                    .onFailure { error ->
                        _uiState.value =
                                _uiState.value.copy(
                                        isLoading = false,
                                        error = error.message ?: "Registration failed"
                                )
                    }
        }
    }

    fun logout() {
        _uiState.value = AuthUiState()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun clearRegistrationSuccess() {
        _uiState.value = _uiState.value.copy(registrationSuccess = false)
    }
}
