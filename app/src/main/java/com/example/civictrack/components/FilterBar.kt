package com.example.civictrack.components


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.civictrack.data.model.IssueCategory
import com.example.civictrack.data.model.IssueStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBar(
    selectedStatus: IssueStatus?,
    onStatusSelected: (IssueStatus?) -> Unit,
    selectedCategory: IssueCategory?,
    onCategorySelected: (IssueCategory?) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        // --- Status Filters ---
        Text("Status", style = MaterialTheme.typography.titleSmall)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(IssueStatus.values()) { status ->
                FilterChip(
                    selected = status == selectedStatus,
                    onClick = {
                        // If the same chip is clicked again, deselect it. Otherwise, select the new one.
                        val newSelection = if (status == selectedStatus) null else status
                        onStatusSelected(newSelection)
                    },
                    label = { Text(status.name) }
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        // --- Category Filters ---
        Text("Category", style = MaterialTheme.typography.titleSmall)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(IssueCategory.values()) { category ->
                FilterChip(
                    selected = category == selectedCategory,
                    onClick = {
                        val newSelection = if (category == selectedCategory) null else category
                        onCategorySelected(newSelection)
                    },
                    label = { Text(category.name.replace('_', ' ')) }
                )
            }
        }
    }
}