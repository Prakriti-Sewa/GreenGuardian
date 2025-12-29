package `in`.co.abdev.greenguardian.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Issue(
    val id: String,
    val title: String,
    val description: String,
    val category: IssueCategory,
    val latitude: Double,
    val longitude: Double,
    val imageUrl: String? = null,
    val status: IssueStatus,
    val reportedBy: String,
    val reportedAt: Long,
    val verifiedAt: Long? = null,
    val resolvedAt: Long? = null,
    val severity: IssueSeverity = IssueSeverity.MEDIUM
)

@Serializable
enum class IssueCategory {
    ILLEGAL_DUMPING,
    PLASTIC_POLLUTION,
    FOREST_DAMAGE,
    WATER_CONTAMINATION,
    WASTE_DISPOSAL,
    AIR_POLLUTION,
    OTHER
}

@Serializable
enum class IssueStatus {
    SUBMITTED,
    VERIFIED,
    IN_PROGRESS,
    RESOLVED,
    REJECTED
}

@Serializable
enum class IssueSeverity {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL
}

@Serializable
data class CreateIssueRequest(
    val title: String,
    val description: String,
    val category: IssueCategory,
    val latitude: Double,
    val longitude: Double,
    val imageBase64: String? = null,
    val severity: IssueSeverity = IssueSeverity.MEDIUM
)

@Serializable
data class IssueResponse(
    val success: Boolean,
    val message: String,
    val issue: Issue? = null
)

@Serializable
data class IssuesListResponse(
    val success: Boolean,
    val issues: List<Issue> = emptyList()
)
