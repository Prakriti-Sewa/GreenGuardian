package `in`.co.abdev.greenguardian.ui.viewmodel

import `in`.co.abdev.greenguardian.data.model.Issue
import `in`.co.abdev.greenguardian.data.repository.IssueRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class IssuesUiState(
    val issues: List<Issue> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class IssuesViewModel : ViewModel() {
    
    private val repository = IssueRepository()
    
    private val _uiState = MutableStateFlow(IssuesUiState())
    val uiState: StateFlow<IssuesUiState> = _uiState.asStateFlow()
    
    init {
        loadIssues()
    }
    
    fun loadIssues() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            repository.getIssues()
                .onSuccess { issues ->
                    _uiState.value = _uiState.value.copy(
                        issues = issues,
                        isLoading = false
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
        }
    }
    
    fun loadNearbyIssues(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            repository.getNearbyIssues(latitude, longitude)
                .onSuccess { issues ->
                    _uiState.value = _uiState.value.copy(
                        issues = issues,
                        isLoading = false
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
        }
    }
}
