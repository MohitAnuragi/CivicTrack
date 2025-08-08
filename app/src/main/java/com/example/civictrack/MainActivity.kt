package com.example.civictrack


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.civictrack.navigation.AppNavigation
import com.example.civictrack.ui.theme.CivicTrackTheme
import dagger.hilt.android.AndroidEntryPoint


// Define routes for navigation
//object Routes {
//    const val LOGIN = "login"
//    const val SIGNUP = "signup"
//    const val HOME = "home"
//}


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CivicTrackTheme {
                    AppNavigation()
                }
            }
        }
    }


