package `in`.co.abdev.greenguardian.ui.viewmodel

import `in`.co.abdev.greenguardian.data.model.CreateIssueRequest
import `in`.co.abdev.greenguardian.data.model.Issue
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
    val latitude: Double? = null,
    val longitude: Double? = null,
    val imageBase64: String? = null,
    val isSubmitting: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val createdIssue: Issue? = null
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
    
    fun updateLocation(latitude: Double, longitude: Double) {
        _uiState.value = _uiState.value.copy(latitude = latitude, longitude = longitude)
    }
    
    fun updateImage(imageBase64: String?) {
        _uiState.value = _uiState.value.copy(imageBase64 = imageBase64)
    }
    
    fun submitIssue() {
        val state = _uiState.value
        
        if (state.title.isBlank() || state.description.isBlank()) {
            _uiState.value = state.copy(error = "Please fill in all required fields")
            return
        }
        
        if (state.latitude == null || state.longitude == null) {
            _uiState.value = state.copy(error = "Location is required")
            return
        }
        
        viewModelScope.launch {
            _uiState.value = state.copy(isSubmitting = true, error = null)
            
            val request = CreateIssueRequest(
                title = state.title,
                description = state.description,
                category = state.category,
                latitude = state.latitude,
                longitude = state.longitude,
                imageBase64 = state.imageBase64,
                severity = state.severity
            )
            
            repository.createIssue(request)
                .onSuccess { issue ->
                    _uiState.value = _uiState.value.copy(
                        isSubmitting = false,
                        isSuccess = true,
                        createdIssue = issue
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isSubmitting = false,
                        error = error.message
                    )
                }
        }
    }
    
    fun resetForm() {
        _uiState.value = ReportIssueUiState()
    }
}
