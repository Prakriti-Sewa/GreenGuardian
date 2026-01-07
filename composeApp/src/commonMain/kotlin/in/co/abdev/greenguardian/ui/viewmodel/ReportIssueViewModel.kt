package `in`.co.abdev.greenguardian.ui.viewmodel

import `in`.co.abdev.greenguardian.data.model.CreateIssueRequest
import `in`.co.abdev.greenguardian.data.model.IssueCategory
import `in`.co.abdev.greenguardian.data.model.IssueSeverity
import `in`.co.abdev.greenguardian.data.repository.IssueRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ReportIssueUiState(
    val title: String = "",
    val description: String = "",
    val category: IssueCategory = IssueCategory.OTHER,
    val severity: IssueSeverity = IssueSeverity.MEDIUM,
    val latitude: String = "",
    val longitude: String = "",
    val isSubmitting: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

class ReportIssueViewModel : ViewModel() {
    
    private val repository = IssueRepository()
    
    private val _uiState = MutableStateFlow(ReportIssueUiState())
    val uiState: StateFlow<ReportIssueUiState> = _uiState.asStateFlow()
    
    fun updateTitle(title: String) {
        _uiState.value = _uiState.value.copy(title = title)
    }
    
    fun updateDescription(description: String) {
        _uiState.value = _uiState.value.copy(description = description)
    }
    
    fun updateCategory(category: IssueCategory) {
        _uiState.value = _uiState.value.copy(category = category)
    }
    
    fun updateSeverity(severity: IssueSeverity) {
        _uiState.value = _uiState.value.copy(severity = severity)
    }
    
    fun updateLatitude(latitude: String) {
        _uiState.value = _uiState.value.copy(latitude = latitude)
    }
    
    fun updateLongitude(longitude: String) {
        _uiState.value = _uiState.value.copy(longitude = longitude)
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    fun resetState() {
        _uiState.value = ReportIssueUiState()
    }
    
    fun submitIssue() {
        val currentState = _uiState.value
        
        // Validate inputs
        if (currentState.title.isBlank()) {
            _uiState.value = currentState.copy(error = "Title is required")
            return
        }
        if (currentState.description.isBlank()) {
            _uiState.value = currentState.copy(error = "Description is required")
            return
        }
        
        val lat = currentState.latitude.toDoubleOrNull()
        val lng = currentState.longitude.toDoubleOrNull()
        
        if (lat == null || lng == null) {
            _uiState.value = currentState.copy(error = "Valid latitude and longitude are required")
            return
        }
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSubmitting = true, error = null)
            
            val request = CreateIssueRequest(
                title = currentState.title,
                description = currentState.description,
                category = currentState.category,
                latitude = lat,
                longitude = lng,
                severity = currentState.severity
            )
            
            repository.createIssue(request)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isSubmitting = false,
                        isSuccess = true
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isSubmitting = false,
                        error = error.message ?: "Failed to submit issue"
                    )
                }
        }
    }
}
