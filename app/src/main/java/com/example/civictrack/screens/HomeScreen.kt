package com.example.civictrack.screens

import com.example.civictrack.data.model.IssueStatus
import com.example.civictrack.viewmodel.HomeViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.civictrack.data.model.GeoLocation
import com.example.civictrack.data.model.Issue
import com.example.civictrack.data.model.IssueCategory
import com.example.civictrack.ui.theme.CivicTrackTheme
import java.util.Date
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.outlined.Inbox
import com.example.civictrack.components.FilterBar

@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    onIssueClick: (String) -> Unit,
    onNavigateToReport: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val filteredIssues by viewModel.filteredIssues.collectAsState()

    Scaffold(
        topBar = {
            HomeTopAppBar(onLogout = onLogout)
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToReport) {
                Icon(Icons.Default.Add, contentDescription = "Report New Issue")
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            // Add the FilterBar at the top of the content area
            FilterBar(
                selectedStatus = uiState.selectedStatus,
                onStatusSelected = viewModel::onStatusFilterChange,
                selectedCategory = uiState.selectedCategory,
                onCategorySelected = viewModel::onCategoryFilterChange
            )
            Divider()

            Box(modifier = Modifier.fillMaxSize()) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else if (filteredIssues.isEmpty()) {
                    val message = if (uiState.allIssues.isEmpty()) {
                        "No issues found nearby!"
                    } else {
                        "No issues match the current filters."
                    }
                    EmptyStateContent(message = message)
                } else {
                    IssueList(issues = filteredIssues, onIssueClick = onIssueClick)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopAppBar(onLogout: () -> Unit) {
    TopAppBar(
        title = { Text("Nearby Issues") },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        actions = {
            IconButton(onClick = onLogout) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = "Logout"
                )
            }
        }
    )
}

@Composable
private fun IssueList(issues: List<Issue>, onIssueClick: (String) -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(issues, key = { it.id }) { issue ->
            IssueCard(
                issue = issue,
                onClick = { onIssueClick(issue.id) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IssueCard(issue: Issue, onClick: () -> Unit) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = issue.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Category: ${issue.category}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))
            StatusBadge(status = issue.status)
        }
    }
}

@Composable
private fun StatusBadge(status: IssueStatus) {
    val backgroundColor = when (status) {
        IssueStatus.REPORTED -> MaterialTheme.colorScheme.errorContainer
        IssueStatus.IN_PROGRESS -> MaterialTheme.colorScheme.tertiaryContainer
        IssueStatus.RESOLVED -> MaterialTheme.colorScheme.primaryContainer
    }
    val contentColor = when (status) {
        IssueStatus.REPORTED -> MaterialTheme.colorScheme.onErrorContainer
        IssueStatus.IN_PROGRESS -> MaterialTheme.colorScheme.onTertiaryContainer
        IssueStatus.RESOLVED -> MaterialTheme.colorScheme.onPrimaryContainer
    }

    Badge(
        containerColor = backgroundColor,
        contentColor = contentColor,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Text(
            text = status.name,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun EmptyStateContent(message: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.Inbox,
            contentDescription = "No Issues",
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    val previewIssue = Issue(
        id = "1", title = "Large pothole on main street", description = "",
        category = IssueCategory.ROADS, status = IssueStatus.REPORTED,
        location = GeoLocation(0.0, 0.0), imageUrls = emptyList(), reportedAt = Date(), reporterId = "user123"
    )
    CivicTrackTheme {
        IssueList(issues = listOf(previewIssue, previewIssue.copy(id = "2", status = IssueStatus.RESOLVED))) {}
    }
}