package `in`.co.abdev.greenguardian.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import java.util.*

object JwtConfig {
    private const val SECRET = "greenguardian-secret-key-change-in-production"
    private const val ISSUER = "greenguardian"
    private const val AUDIENCE = "greenguardian-users"
    const val REALM = "GreenGuardian API"
    
    private val algorithm = Algorithm.HMAC256(SECRET)
    
    fun generateToken(userId: String, email: String): String {
        return JWT.create()
            .withAudience(AUDIENCE)
            .withIssuer(ISSUER)
            .withClaim("userId", userId)
            .withClaim("email", email)
            .withExpiresAt(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) // 7 days
            .sign(algorithm)
    }
    
    fun configureJwtAuth(application: Application) {
        application.install(Authentication) {
            jwt("jwt") {
                realm = REALM
                verifier(
                    JWT.require(algorithm)
                        .withAudience(AUDIENCE)
                        .withIssuer(ISSUER)
                        .build()
                )
                validate { credential ->
                    if (credential.payload.audience.contains(AUDIENCE)) {
                        JWTPrincipal(credential.payload)
                    } else {
                        null
                    }
                }
            }
        }
    }
}

val ApplicationCall.userId: String?
    get() = principal<JWTPrincipal>()?.payload?.getClaim("userId")?.asString()

val ApplicationCall.userEmail: String?
    get() = principal<JWTPrincipal>()?.payload?.getClaim("email")?.asString()
