package com.example.civictrack.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Map
import androidx.compose.ui.graphics.vector.ImageVector


// A sealed class makes route definitions type-safe and organized.
sealed class Screen(val route: String) {
    // Auth Routes
    data object Login : Screen("login")
    data object SignUp : Screen("signup")

    // Main App Routes
    data object Home : Screen("home")
    data object Map : Screen("map")
    data object ReportIssue : Screen("report_issue")
    data object IssueDetails : Screen("issue_details/{issueId}") {
        fun createRoute(issueId: String) = "issue_details/$issueId"
    }
}

// These represent the nested graphs
const val AUTH_GRAPH_ROUTE = "auth"
const val MAIN_GRAPH_ROUTE = "main"

data class BottomNavItem(val route: String, val icon: ImageVector, val label: String)

// List of items for the bottom bar
val bottomNavItems = listOf(
    BottomNavItem(Screen.Home.route, Icons.Default.List, "List"),
    BottomNavItem(Screen.Map.route, Icons.Default.Map, "Map")
)