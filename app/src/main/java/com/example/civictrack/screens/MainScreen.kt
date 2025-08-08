package com.example.civictrack.screens



import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.civictrack.navigation.Screen

// Define the bottom bar items
sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    data object Home : BottomNavItem(Screen.Home.route, Icons.Default.List, "List")
    data object Map : BottomNavItem(Screen.Map.route, Icons.Default.Map, "Map")
}

@Composable
fun MainScreen(
    mainNavController: NavHostController // The app's main NavController
) {
    val bottomNavItems = listOf(BottomNavItem.Home, BottomNavItem.Map)
    val tabNavController = rememberNavController() // A new NavController for the tabs

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by tabNavController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                bottomNavItems.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            tabNavController.navigate(screen.route) {
                                popUpTo(tabNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = tabNavController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Home.route) {
                HomeScreen(
                    onLogout = { /* This needs to be handled via ViewModel or passed up */ },
                    onIssueClick = { issueId ->
                        mainNavController.navigate(Screen.IssueDetails.createRoute(issueId))
                    },
                    onNavigateToReport = {
                        mainNavController.navigate(Screen.ReportIssue.route)
                    }
                )
            }
            composable(BottomNavItem.Map.route) {
                // We will create this screen next
                MapScreen(
                    onIssueClick = { issueId ->
                        mainNavController.navigate(Screen.IssueDetails.createRoute(issueId))
                    }
                )
            }
        }
    }
}