package com.example.civictrack

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.civictrack.auth.AuthViewModel
import com.example.civictrack.clients.SupabaseManager
import io.github.jan.supabase.auth.auth


@Composable
fun HomeScreen(navController: NavController, authViewModel: AuthViewModel) {
    val userEmail = SupabaseManager.supabaseClient.auth.currentUserOrNull()?.email ?: "user"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome!", style = MaterialTheme.typography.headlineMedium)
        Text("You are logged in as: $userEmail", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = { navController.navigate("profile") }) {
            Text("Go to Profile")
        }

    }
}