package `in`.co.abdev.greenguardian.data.model

import kotlinx.serialization.Serializable

@Serializable
data class User(val id: String, val name: String, val email: String, val role: String = "CITIZEN")

@Serializable
enum class UserRole {
    CITIZEN,
    ADMIN,
    NGO,
    GOVERNMENT
}

@Serializable data class LoginRequest(val email: String, val password: String)

@Serializable data class RegisterRequest(val name: String, val email: String, val password: String)

@Serializable
data class AuthResponse(
        val success: Boolean,
        val message: String,
        val token: String? = null,
        val user: User? = null
)
