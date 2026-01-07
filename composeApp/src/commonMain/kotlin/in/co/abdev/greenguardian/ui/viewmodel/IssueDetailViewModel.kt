package `in`.co.abdev.greenguardian.ui.viewmodel

import `in`.co.abdev.greenguardian.data.model.Issue
import `in`.co.abdev.greenguardian.data.repository.IssueRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class IssueDetailUiState(
    val issue: Issue? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

class IssueDetailViewModel(
    private val issueId: String
) : ViewModel() {
    
    private val repository = IssueRepository()
    
    private val _uiState = MutableStateFlow(IssueDetailUiState())
    val uiState: StateFlow<IssueDetailUiState> = _uiState.asStateFlow()
    
    init {
        loadIssue()
    }
    
    fun loadIssue() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            repository.getIssueById(issueId)
                .onSuccess { issue ->
                    _uiState.value = _uiState.value.copy(
                        issue = issue,
                        isLoading = false
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Failed to load issue"
                    )
                }
        }
    }
}
