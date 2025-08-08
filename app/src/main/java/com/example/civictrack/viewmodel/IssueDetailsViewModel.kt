package com.example.civictrack.viewmodel


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.civictrack.data.model.Issue
import com.example.civictrack.data.repository.DataResult
import com.example.civictrack.data.repository.IssuesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

// This data class will hold the entire state for our detail screen UI
data class IssueDetailUiState(
    val isLoading: Boolean = true,
    val issue: Issue? = null,
    val error: String? = null
)

@HiltViewModel
class IssueDetailViewModel @Inject constructor(
    private val repository: IssuesRepository,
    savedStateHandle: SavedStateHandle // Used to get navigation arguments
) : ViewModel() {

    private val _uiState = MutableStateFlow(IssueDetailUiState())
    val uiState: StateFlow<IssueDetailUiState> = _uiState.asStateFlow()

    init {
        // Retrieve the issueId passed from the NavGraph
        val issueId: String? = savedStateHandle.get("issueId")
        if (issueId != null) {
            fetchIssue(issueId)
        } else {
            _uiState.update { it.copy(isLoading = false, error = "Issue ID not found.") }
        }
    }

    private fun fetchIssue(issueId: String) {
        repository.getIssueDetails(issueId).onEach { result ->
            val newState = when (result) {
                is DataResult.Success -> IssueDetailUiState(issue = result.data, isLoading = false)
                is DataResult.Error -> IssueDetailUiState(error = result.exception.message, isLoading = false)
            }
            _uiState.value = newState
        }.launchIn(viewModelScope)
    }
}