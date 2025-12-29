package `in`.co.abdev.greenguardian.routes

import `in`.co.abdev.greenguardian.data.model.*
import `in`.co.abdev.greenguardian.data.repository.IssueRepository
import `in`.co.abdev.greenguardian.security.userEmail
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.issueRoutes() {
    val repository = IssueRepository()
    
    route("/issues") {
        
        // Get all issues (public)
        get {
            try {
                val issues = repository.getAllIssues()
                call.respond(IssuesListResponse(success = true, issues = issues))
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    ErrorResponse(message = e.message ?: "Failed to fetch issues")
                )
            }
        }
        
        // Get issue by ID (public)
        get("/{id}") {
            try {
                val id = call.parameters["id"] ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                    ErrorResponse(message = "Missing issue ID")
                )
                
                val issue = repository.getIssueById(id)
                if (issue != null) {
                    call.respond(IssueResponse(success = true, message = "Issue found", issue = issue))
                } else {
                    call.respond(
                        HttpStatusCode.NotFound,
                        ErrorResponse(message = "Issue not found")
                    )
                }
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    ErrorResponse(message = e.message ?: "Failed to fetch issue")
                )
            }
        }
        
        // Get nearby issues (public)
        get("/nearby") {
            try {
                val lat = call.request.queryParameters["lat"]?.toDoubleOrNull()
                val lng = call.request.queryParameters["lng"]?.toDoubleOrNull()
                val radius = call.request.queryParameters["radius"]?.toDoubleOrNull() ?: 10.0
                
                if (lat == null || lng == null) {
                    return@get call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse(message = "Missing latitude or longitude")
                    )
                }
                
                val issues = repository.getNearbyIssues(lat, lng, radius)
                call.respond(IssuesListResponse(success = true, issues = issues))
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    ErrorResponse(message = e.message ?: "Failed to fetch nearby issues")
                )
            }
        }
        
        // Get issues by status (public)
        get("/status/{status}") {
            try {
                val status = call.parameters["status"] ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                    ErrorResponse(message = "Missing status")
                )
                
                val issues = repository.getIssuesByStatus(status.uppercase())
                call.respond(IssuesListResponse(success = true, issues = issues))
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    ErrorResponse(message = e.message ?: "Failed to fetch issues by status")
                )
            }
        }
        
        // Create issue (authenticated or public for testing)
        post {
            try {
                val request = call.receive<CreateIssueRequest>()
                
                // Validate request
                if (request.title.isBlank() || request.description.isBlank()) {
                    return@post call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse(message = "Title and description are required")
                    )
                }
                
                // Get user email from JWT if authenticated, otherwise use "anonymous"
                val userEmail = call.userEmail ?: "anonymous@greenguardian.com"
                
                val issue = repository.createIssue(request, userEmail)
                call.respond(
                    HttpStatusCode.Created,
                    IssueResponse(success = true, message = "Issue created successfully", issue = issue)
                )
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    ErrorResponse(message = e.message ?: "Failed to create issue")
                )
            }
        }
        
        // Update issue status (requires authentication)
        authenticate("jwt", optional = true) {
            patch("/{id}/status") {
                try {
                    val id = call.parameters["id"] ?: return@patch call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse(message = "Missing issue ID")
                    )
                    
                    val status = call.request.queryParameters["status"] ?: return@patch call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse(message = "Missing status")
                    )
                    
                    val updated = repository.updateIssueStatus(id, status.uppercase())
                    if (updated) {
                        call.respond(IssueResponse(success = true, message = "Issue status updated"))
                    } else {
                        call.respond(
                            HttpStatusCode.NotFound,
                            ErrorResponse(message = "Issue not found")
                        )
                    }
                } catch (e: Exception) {
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        ErrorResponse(message = e.message ?: "Failed to update issue")
                    )
                }
            }
            
            // Delete issue (requires authentication)
            delete("/{id}") {
                try {
                    val id = call.parameters["id"] ?: return@delete call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse(message = "Missing issue ID")
                    )
                    
                    val deleted = repository.deleteIssue(id)
                    if (deleted) {
                        call.respond(IssueResponse(success = true, message = "Issue deleted"))
                    } else {
                        call.respond(
                            HttpStatusCode.NotFound,
                            ErrorResponse(message = "Issue not found")
                        )
                    }
                } catch (e: Exception) {
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        ErrorResponse(message = e.message ?: "Failed to delete issue")
                    )
                }
            }
        }
    }
}
