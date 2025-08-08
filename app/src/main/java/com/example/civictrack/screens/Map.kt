package com.example.civictrack.screens


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.civictrack.viewmodel.MapViewModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

@Composable
fun MapScreen(
    onIssueClick: (String) -> Unit,
    viewModel: MapViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = viewModel.cameraPositionState,
        ) {
            uiState.issues.forEach { issue ->
                Marker(
                    state = MarkerState(
                        position = LatLng(issue.location.latitude, issue.location.longitude)
                    ),
                    title = issue.title,
                    snippet = "Category: ${issue.category}",
                    onInfoWindowClick = {
                        onIssueClick(issue.id)
                    },
                    // You can customize icons based on category or status
                    icon = BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_RED
                    )
                )
            }
        }
        // TODO: Add FilterBar UI as an overlay here
    }
}