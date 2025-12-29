package `in`.co.abdev.greenguardian.data.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant

// Database Tables
object Users : UUIDTable("users") {
    val name = varchar("name", 255)
    val email = varchar("email", 255).uniqueIndex()
    val passwordHash = varchar("password_hash", 255)
    val role = varchar("role", 50).default("CITIZEN")
    val createdAt = timestamp("created_at").default(Instant.now())
}

object Issues : UUIDTable("issues") {
    val title = varchar("title", 255)
    val description = text("description")
    val category = varchar("category", 50)
    val latitude = double("latitude")
    val longitude = double("longitude")
    val imageUrl = varchar("image_url", 500).nullable()
    val status = varchar("status", 50).default("SUBMITTED")
    val severity = varchar("severity", 50).default("MEDIUM")
    val reportedBy = varchar("reported_by", 255)
    val reportedAt = timestamp("reported_at").default(Instant.now())
    val verifiedAt = timestamp("verified_at").nullable()
    val resolvedAt = timestamp("resolved_at").nullable()
    val userId = reference("user_id", Users).nullable()
}

// DTOs matching frontend models
@Serializable
data class UserDTO(
    val id: String,
    val name: String,
    val email: String,
    val role: String = "CITIZEN"
)

@Serializable
data class IssueDTO(
    val id: String,
    val title: String,
    val description: String,
    val category: String,
    val latitude: Double,
    val longitude: Double,
    val imageUrl: String? = null,
    val status: String,
    val reportedBy: String,
    val reportedAt: Long,
    val verifiedAt: Long? = null,
    val resolvedAt: Long? = null,
    val severity: String = "MEDIUM"
)

@Serializable
data class CreateIssueRequest(
    val title: String,
    val description: String,
    val category: String,
    val latitude: Double,
    val longitude: Double,
    val imageBase64: String? = null,
    val severity: String = "MEDIUM"
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

@Serializable
data class AuthResponse(
    val success: Boolean,
    val message: String,
    val token: String? = null,
    val user: UserDTO? = null
)

@Serializable
data class IssueResponse(
    val success: Boolean,
    val message: String,
    val issue: IssueDTO? = null
)

@Serializable
data class IssuesListResponse(
    val success: Boolean,
    val issues: List<IssueDTO> = emptyList()
)

@Serializable
data class ErrorResponse(
    val success: Boolean = false,
    val message: String
)
