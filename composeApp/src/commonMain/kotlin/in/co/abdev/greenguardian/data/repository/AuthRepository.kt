package `in`.co.abdev.greenguardian.data.repository

import `in`.co.abdev.greenguardian.data.model.*
import `in`.co.abdev.greenguardian.data.network.NetworkClient
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class AuthRepository {
    
    private val client = NetworkClient.httpClient
    private val baseUrl = NetworkClient.BASE_URL
    
    suspend fun login(email: String, password: String): Result<AuthResponse> {
        return try {
            val response = client.post("$baseUrl/auth/login") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(email, password))
            }
            if (response.status.isSuccess()) {
                val authResponse = response.body<AuthResponse>()
                Result.success(authResponse)
            } else {
                Result.failure(Exception("Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun register(name: String, email: String, password: String): Result<AuthResponse> {
        return try {
            val response = client.post("$baseUrl/auth/register") {
                contentType(ContentType.Application.Json)
                setBody(RegisterRequest(name, email, password))
            }
            if (response.status.isSuccess()) {
                val authResponse = response.body<AuthResponse>()
                Result.success(authResponse)
            } else {
                Result.failure(Exception("Registration failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
