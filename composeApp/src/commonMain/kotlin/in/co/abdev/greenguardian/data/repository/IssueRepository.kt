package `in`.co.abdev.greenguardian.data.repository

import `in`.co.abdev.greenguardian.data.model.*
import `in`.co.abdev.greenguardian.data.network.NetworkClient
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class IssueRepository {
    
    private val client = NetworkClient.httpClient
    private val baseUrl = NetworkClient.BASE_URL
    
    suspend fun getIssues(): Result<List<Issue>> {
        return try {
            val response = client.get("$baseUrl/issues")
            if (response.status.isSuccess()) {
                val issuesResponse = response.body<IssuesListResponse>()
                Result.success(issuesResponse.issues)
            } else {
                Result.failure(Exception("Failed to fetch issues"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getIssueById(id: String): Result<Issue> {
        return try {
            val response = client.get("$baseUrl/issues/$id")
            if (response.status.isSuccess()) {
                val issueResponse = response.body<IssueResponse>()
                issueResponse.issue?.let { 
                    Result.success(it) 
                } ?: Result.failure(Exception("Issue not found"))
            } else {
                Result.failure(Exception("Failed to fetch issue"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun createIssue(request: CreateIssueRequest): Result<Issue> {
        return try {
            val response = client.post("$baseUrl/issues") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            if (response.status.isSuccess()) {
                val issueResponse = response.body<IssueResponse>()
                issueResponse.issue?.let { 
                    Result.success(it) 
                } ?: Result.failure(Exception("Failed to create issue"))
            } else {
                Result.failure(Exception("Failed to create issue"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getNearbyIssues(latitude: Double, longitude: Double, radius: Double = 10.0): Result<List<Issue>> {
        return try {
            val response = client.get("$baseUrl/issues/nearby") {
                parameter("lat", latitude)
                parameter("lng", longitude)
                parameter("radius", radius)
            }
            if (response.status.isSuccess()) {
                val issuesResponse = response.body<IssuesListResponse>()
                Result.success(issuesResponse.issues)
            } else {
                Result.failure(Exception("Failed to fetch nearby issues"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
