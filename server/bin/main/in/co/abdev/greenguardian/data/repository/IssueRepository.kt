package `in`.co.abdev.greenguardian.data.repository

import `in`.co.abdev.greenguardian.data.model.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import java.util.*
import kotlin.math.*

class IssueRepository {
    
    fun getAllIssues(): List<IssueDTO> = transaction {
        Issues.selectAll().map { it.toIssueDTO() }
    }
    
    fun getIssueById(id: String): IssueDTO? = transaction {
        Issues.selectAll().where { Issues.id eq UUID.fromString(id) }
            .mapNotNull { it.toIssueDTO() }
            .singleOrNull()
    }
    
    fun createIssue(request: CreateIssueRequest, userEmail: String): IssueDTO = transaction {
        val issueId = Issues.insertAndGetId {
            it[title] = request.title
            it[description] = request.description
            it[category] = request.category
            it[latitude] = request.latitude
            it[longitude] = request.longitude
            it[imageUrl] = request.imageBase64 // In production, save to storage and store URL
            it[severity] = request.severity
            it[reportedBy] = userEmail
            it[reportedAt] = Instant.now()
            it[status] = "SUBMITTED"
        }
        
        Issues.selectAll().where { Issues.id eq issueId }
            .map { it.toIssueDTO() }
            .single()
    }
    
    fun updateIssueStatus(id: String, newStatus: String): Boolean = transaction {
        val updated = Issues.update({ Issues.id eq UUID.fromString(id) }) {
            it[status] = newStatus
            
            when (newStatus) {
                "VERIFIED" -> it[verifiedAt] = Instant.now()
                "RESOLVED" -> it[resolvedAt] = Instant.now()
            }
        }
        updated > 0
    }
    
    fun getNearbyIssues(latitude: Double, longitude: Double, radiusKm: Double): List<IssueDTO> = transaction {
        Issues.selectAll()
            .map { it.toIssueDTO() }
            .filter { issue ->
                val distance = calculateDistance(latitude, longitude, issue.latitude, issue.longitude)
                distance <= radiusKm
            }
    }
    
    fun getIssuesByStatus(status: String): List<IssueDTO> = transaction {
        Issues.selectAll().where { Issues.status eq status }
            .map { it.toIssueDTO() }
    }
    
    fun deleteIssue(id: String): Boolean = transaction {
        Issues.deleteWhere { Issues.id eq UUID.fromString(id) } > 0
    }
    
    private fun ResultRow.toIssueDTO() = IssueDTO(
        id = this[Issues.id].toString(),
        title = this[Issues.title],
        description = this[Issues.description],
        category = this[Issues.category],
        latitude = this[Issues.latitude],
        longitude = this[Issues.longitude],
        imageUrl = this[Issues.imageUrl],
        status = this[Issues.status],
        reportedBy = this[Issues.reportedBy],
        reportedAt = this[Issues.reportedAt].toEpochMilli(),
        verifiedAt = this[Issues.verifiedAt]?.toEpochMilli(),
        resolvedAt = this[Issues.resolvedAt]?.toEpochMilli(),
        severity = this[Issues.severity]
    )
    
    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val R = 6371.0 // Earth radius in kilometers
        
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        
        val a = sin(dLat / 2).pow(2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2).pow(2)
        
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        
        return R * c
    }
}
