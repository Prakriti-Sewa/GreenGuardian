package `in`.co.abdev.greenguardian.routes

import `in`.co.abdev.greenguardian.data.model.AuthResponse
import `in`.co.abdev.greenguardian.data.model.ErrorResponse
import `in`.co.abdev.greenguardian.data.model.LoginRequest
import `in`.co.abdev.greenguardian.data.model.RegisterRequest
import `in`.co.abdev.greenguardian.data.repository.UserRepository
import `in`.co.abdev.greenguardian.security.JwtConfig
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authRoutes() {
    val repository = UserRepository()
    
    route("/auth") {
        
        // Register new user
        post("/register") {
            try {
                val request = call.receive<RegisterRequest>()
                
                // Validate request
                if (request.name.isBlank() || request.email.isBlank() || request.password.isBlank()) {
                    return@post call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse(message = "Name, email, and password are required")
                    )
                }
                
                if (request.password.length < 6) {
                    return@post call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse(message = "Password must be at least 6 characters")
                    )
                }
                
                val user = repository.createUser(request.name, request.email, request.password)
                
                if (user != null) {
                    val token = JwtConfig.generateToken(user.id, user.email)
                    call.respond(
                        HttpStatusCode.Created,
                        AuthResponse(
                            success = true,
                            message = "Registration successful",
                            token = token,
                            user = user
                        )
                    )
                } else {
                    call.respond(
                        HttpStatusCode.Conflict,
                        ErrorResponse(message = "User with this email already exists")
                    )
                }
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    ErrorResponse(message = e.message ?: "Registration failed")
                )
            }
        }
        
        // Login
        post("/login") {
            try {
                val request = call.receive<LoginRequest>()
                
                // Validate request
                if (request.email.isBlank() || request.password.isBlank()) {
                    return@post call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse(message = "Email and password are required")
                    )
                }
                
                val isValid = repository.verifyPassword(request.email, request.password)
                
                if (isValid) {
                    val user = repository.findByEmail(request.email)
                    if (user != null) {
                        val token = JwtConfig.generateToken(user.id, user.email)
                        call.respond(
                            AuthResponse(
                                success = true,
                                message = "Login successful",
                                token = token,
                                user = user
                            )
                        )
                    } else {
                        call.respond(
                            HttpStatusCode.Unauthorized,
                            ErrorResponse(message = "Invalid credentials")
                        )
                    }
                } else {
                    call.respond(
                        HttpStatusCode.Unauthorized,
                        ErrorResponse(message = "Invalid credentials")
                    )
                }
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    ErrorResponse(message = e.message ?: "Login failed")
                )
            }
        }
    }
}
