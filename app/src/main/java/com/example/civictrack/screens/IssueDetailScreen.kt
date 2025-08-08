package com.example.civictrack.screens



import com.example.civictrack.data.model.GeoLocation
import com.example.civictrack.data.model.Issue
import com.example.civictrack.data.model.IssueCategory
import com.example.civictrack.data.model.IssueStatus
import com.example.civictrack.ui.theme.CivicTrackTheme
import com.example.civictrack.viewmodel.IssueDetailViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IssueDetailScreen(
    onNavigateBack: () -> Unit,
    viewModel: IssueDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Issue Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                uiState.error != null -> {
                    Text(
                        text = "Error: ${uiState.error}",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState.issue != null -> {
                    IssueDetailContent(issue = uiState.issue!!)
                }
            }
        }
    }
}

@Composable
private fun IssueDetailContent(issue: Issue) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- Image ---
        item {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(issue.imageUrls.firstOrNull() ?: "https://picsum.photos/seed/${issue.id}/1280/720") // Placeholder
                    .crossfade(true)
                    .build(),
                contentDescription = "Image of ${issue.title}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
        }

        // --- Title and Status ---
        item {
            Text(issue.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            StatusBadge(status = issue.status) // Reusing the badge from HomeScreen
        }

        // --- Information Section ---
        item {
            InfoCard(issue)
        }

        // --- Description ---
        item {
            Text("Description", style = MaterialTheme.typography.titleMedium)
            Text(issue.description, style = MaterialTheme.typography.bodyLarge)
        }

        // --- Status History ---
        item {
            Text("History", style = MaterialTheme.typography.titleMedium)
            Card {
                Column(Modifier.padding(16.dp)) {
                    Text("Reported on: ${issue.reportedAt.toFormattedString()}")
                    // In a real app, you would have a list of status changes
                }
            }
        }
    }
}

@Composable
private fun InfoCard(issue: Issue) {
    Card {
        Column(Modifier.padding(16.dp)) {
            InfoRow("Category:", issue.category.name.replace('_', ' '))
            Divider(Modifier.padding(vertical = 8.dp))
            InfoRow("Reported on:", issue.reportedAt.toFormattedString())
            Divider(Modifier.padding(vertical = 8.dp))
            InfoRow("Location:", "Lat: ${issue.location.latitude}, Lon: ${issue.location.longitude}")
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(label, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
        Text(value, modifier = Modifier.weight(2f))
    }
}

// Re-add the StatusBadge here or move it to a common file to share with HomeScreen
@Composable
private fun StatusBadge(status: IssueStatus) {
    // ... same implementation as in HomeScreen ...
}

fun Date.toFormattedString(): String {
    val formatter = SimpleDateFormat("dd MMM, yyyy 'at' hh:mm a", Locale.getDefault())
    return formatter.format(this)
}

// --- PREVIEW ---
@Preview(showBackground = true)
@Composable
private fun IssueDetailContentPreview() {
    val previewIssue = Issue(
        id = "1", title = "Large pothole on main street",
        description = "A very large and dangerous pothole has formed on the main road near the city park. It has caused issues for multiple vehicles.",
        category = IssueCategory.ROADS, status = IssueStatus.IN_PROGRESS,
        location = GeoLocation(24.8, 74.6), imageUrls = emptyList(),
        reportedAt = Date(), reporterId = "user123"
    )
    CivicTrackTheme {
        Surface {
            IssueDetailContent(issue = previewIssue)
        }
    }
}