package com.example.civictrack.navigation

import com.example.civictrack.auth.AuthState
import com.example.civictrack.auth.AuthViewModel
import com.example.civictrack.auth.LoginScreen
import com.example.civictrack.auth.SignUpScreen
import com.example.civictrack.screens.HomeScreen
import com.example.civictrack.screens.IssueDetailScreen
import com.example.civictrack.screens.MainScreen
import com.example.civictrack.screens.ReportIssueScreen

//import android.widget.Toast
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//import com.example.civictrack.HomeScreen
//import com.example.civictrack.auth.AuthState
//import com.example.civictrack.auth.AuthViewModel
//import com.example.civictrack.auth.LoginScreen
//import com.example.civictrack.auth.SignUpScreen
//
//
//@Composable
//fun AppNavigation(authViewModel: AuthViewModel = viewModel()) {
//    val authState by authViewModel.authState.collectAsState()
//    val navController = rememberNavController()
//    val context = LocalContext.current
//
//    // Display a loading indicator, error message, or the main content
//    when (val state = authState) {
//        is AuthState.Authenticated -> {
//            // If authenticated, show the HomeScreen
//            NavHost(navController = navController, startDestination = "home") {
//                composable("home") { HomeScreen(navController, authViewModel) }
//            }
//        }
//        is AuthState.NotAuthenticated -> {
//            // If not authenticated, show the Login/SignUp flow
//            NavHost(navController = navController, startDestination = "login") {
//                composable("login") { LoginScreen(navController, authViewModel) }
//                composable("signup") { SignUpScreen(navController, authViewModel) }
//            }
//        }
//        is AuthState.Loading -> {
//            // Show a loading spinner while checking auth state
//            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                CircularProgressIndicator()
//            }
//        }
//        is AuthState.Error -> {
//            // Show an error message. In a real app, you'd want to navigate back
//            // or provide a retry option.
//            Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
//            // For simplicity, we'll just show the Login screen on error
//            NavHost(navController = navController, startDestination = "login") {
//                composable("login") { LoginScreen(navController, authViewModel) }
//                composable("signup") { SignUpScreen(navController, authViewModel) }
//            }
//        }
//    }
//}

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation


@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val authState by authViewModel.authState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        when (authState) {
            is AuthState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is AuthState.Error -> {
                Text(
                    text = "An error occurred. Please restart.",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            else -> {
                NavHost(
                    navController = navController,
                    startDestination = if (authState is AuthState.Authenticated) MAIN_GRAPH_ROUTE else AUTH_GRAPH_ROUTE
                ) {
                    authGraph(navController, authViewModel)
                    mainGraph(navController, authViewModel)
                }
            }
        }
    }

    LaunchedEffect(authState) {
        if (authState is AuthState.Authenticated) {
            navController.navigate(MAIN_GRAPH_ROUTE) {
                popUpTo(AUTH_GRAPH_ROUTE) { inclusive = true }
            }
        }
    }
}

fun NavGraphBuilder.authGraph(navController: NavHostController, authViewModel: AuthViewModel) {
    navigation(startDestination = Screen.Login.route, route = AUTH_GRAPH_ROUTE) {
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToSignUp = { navController.navigate(Screen.SignUp.route) },
                authViewModel = authViewModel
            )
        }
        composable(Screen.SignUp.route) {
            SignUpScreen(
                onNavigateBackToLogin = { navController.popBackStack() },
                authViewModel = authViewModel
            )
        }
    }
}

fun NavGraphBuilder.mainGraph(navController: NavHostController, authViewModel: AuthViewModel) {
    // The main graph now defines a container screen and its sibling destinations
    navigation(startDestination = "main_container", route = MAIN_GRAPH_ROUTE) {
        // This is the primary destination after login. It holds the bottom bar.
        composable("main_container") {
            MainScreen(mainNavController = navController)
        }
        // These screens are navigated to from within MainScreen
        composable(Screen.IssueDetails.route) {
            IssueDetailScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.ReportIssue.route) {
            ReportIssueScreen(
                onNavigateBack = { navController.popBackStack() },
                onReportSubmitted = { navController.popBackStack() }
            )
        }
    }
}