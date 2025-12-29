package `in`.co.abdev.greenguardian

import `in`.co.abdev.greenguardian.data.DatabaseFactory
import `in`.co.abdev.greenguardian.routes.authRoutes
import `in`.co.abdev.greenguardian.routes.issueRoutes
import `in`.co.abdev.greenguardian.security.JwtConfig
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import org.slf4j.event.Level

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    // Initialize database
    DatabaseFactory.init()
    
    // Configure JWT authentication
    JwtConfig.configureJwtAuth(this)
    
    // Install plugins
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }
    
    install(CORS) {
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowMethod(HttpMethod.Options)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.AccessControlAllowOrigin)
        anyHost() // Allow all hosts for development - restrict in production
        allowCredentials = true
        allowNonSimpleContentTypes = true
    }
    
    install(CallLogging) {
        level = Level.INFO
    }
    
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respond(
                HttpStatusCode.InternalServerError,
                mapOf("success" to false, "message" to (cause.message ?: "Internal server error"))
            )
        }
    }
    
    // Configure routing
    routing {
        get("/") {
            call.respondText(
                """
                🌿 GreenGuardian API Server
                
                Available Endpoints:
                - GET  /api/issues                    - Get all issues
                - GET  /api/issues/{id}               - Get issue by ID
                - GET  /api/issues/nearby             - Get nearby issues (?lat=X&lng=Y&radius=10)
                - GET  /api/issues/status/{status}    - Get issues by status
                - POST /api/issues                    - Create new issue
                - PATCH /api/issues/{id}/status       - Update issue status
                - DELETE /api/issues/{id}             - Delete issue
                
                - POST /api/auth/register             - Register new user
                - POST /api/auth/login                - Login user
                
                Server is running! ✅
                """.trimIndent(),
                ContentType.Text.Plain
            )
        }
        
        route("/api") {
            issueRoutes()
            authRoutes()
        }
    }
}
