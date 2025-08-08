package com.example.civictrack.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.civictrack.data.model.Issue
import com.example.civictrack.data.model.IssueCategory
import com.example.civictrack.data.model.IssueStatus
import com.example.civictrack.data.repository.IssuesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.*


/**
 * UI State for the Home screen, now including filter selections.
 */
data class HomeUiState(
    val isLoading: Boolean = true,
    val allIssues: List<Issue> = emptyList(),
    val selectedStatus: IssueStatus? = null,
    val selectedCategory: IssueCategory? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: IssuesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    /**
     * A derived StateFlow that automatically computes the filtered list
     * whenever the source data or filter selections change.
     */
    val filteredIssues: StateFlow<List<Issue>> = _uiState
        .map { state ->
            // Start with the full list of issues
            state.allIssues.filter { issue ->
                // Check if the issue's status matches the filter (or if the filter is null)
                val statusMatch = state.selectedStatus == null || issue.status == state.selectedStatus
                // Check if the issue's category matches the filter (or if the filter is null)
                val categoryMatch = state.selectedCategory == null || issue.category == state.selectedCategory
                // The issue is included only if it matches both conditions
                statusMatch && categoryMatch
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Start sharing after 5s of no subscribers
            initialValue = emptyList()
        )

    init {
        // Use a default location for Ranpur, Rajasthan
        loadIssues(25.2744, 76.1025, 5)
    }

    /**
     * Updates the selected status filter.
     */
    fun onStatusFilterChange(status: IssueStatus?) {
        _uiState.update { it.copy(selectedStatus = status) }
    }

    /**
     * Updates the selected category filter.
     */
    fun onCategoryFilterChange(category: IssueCategory?) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    /**
     * Fetches the initial list of issues from the repository.
     */
    private fun loadIssues(lat: Double, lon: Double, radius: Int) {
        repository.getNearbyIssues(lat, lon, radius)
            .onEach { issueList ->
                // Update the state with the full list of issues and set loading to false
                _uiState.update { it.copy(allIssues = issueList, isLoading = false) }
            }
            .launchIn(viewModelScope)
    }
}