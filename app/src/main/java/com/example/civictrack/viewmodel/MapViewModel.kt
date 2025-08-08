package com.example.civictrack.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.civictrack.data.model.Issue
import com.example.civictrack.data.repository.IssuesRepository
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

data class MapUiState(
    val issues: List<Issue> = emptyList(),
    val isLoading: Boolean = true,
    // Add filter states later
)

@HiltViewModel
class MapViewModel @Inject constructor(
    private val repository: IssuesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(MapUiState())
    val uiState = _uiState.asStateFlow()

    // Control the map's camera position
    val cameraPositionState = CameraPositionState(
        position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(
            LatLng(25.2744, 76.1025), // Default to Ranpur, Rajasthan area
            12f
        )
    )

    init {
        // Fetch issues using the user's location (hardcoded for now)
        repository.getNearbyIssues(25.2744, 76.1025, 5)
            .onEach { issues ->
                _uiState.value = MapUiState(issues = issues, isLoading = false)
            }
            .launchIn(viewModelScope)
    }
}