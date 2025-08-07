package com.example.civictrack.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.civictrack.HomeScreen
import com.example.civictrack.auth.AuthState
import com.example.civictrack.auth.AuthViewModel
import com.example.civictrack.auth.LoginScreen
import com.example.civictrack.auth.SignUpScreen


@Composable
fun AppNavigation(authViewModel: AuthViewModel = viewModel()) {
    val authState by authViewModel.authState.collectAsState()
    val navController = rememberNavController()
    val context = LocalContext.current

    // Display a loading indicator, error message, or the main content
    when (val state = authState) {
        is AuthState.Authenticated -> {
            // If authenticated, show the HomeScreen
            NavHost(navController = navController, startDestination = "home") {
                composable("home") { HomeScreen(navController, authViewModel) }
            }
        }
        is AuthState.NotAuthenticated -> {
            // If not authenticated, show the Login/SignUp flow
            NavHost(navController = navController, startDestination = "login") {
                composable("login") { LoginScreen(navController, authViewModel) }
                composable("signup") { SignUpScreen(navController, authViewModel) }
            }
        }
        is AuthState.Loading -> {
            // Show a loading spinner while checking auth state
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is AuthState.Error -> {
            // Show an error message. In a real app, you'd want to navigate back
            // or provide a retry option.
            Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
            // For simplicity, we'll just show the Login screen on error
            NavHost(navController = navController, startDestination = "login") {
                composable("login") { LoginScreen(navController, authViewModel) }
                composable("signup") { SignUpScreen(navController, authViewModel) }
            }
        }
    }
}
