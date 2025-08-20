package com.example.civictrack.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.civictrack.clients.SupabaseManager
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// This sealed class remains the same
sealed class AuthState {
    data object Authenticated : AuthState()
    data object NotAuthenticated : AuthState()
    data object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel : ViewModel() {

    // Get the Supabase client from our singleton object
    private val supabase = SupabaseManager.supabaseClient

    // StateFlow for auth state remains the same
    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState

    init {
        viewModelScope.launch {
            // Check the initial session state
            // ** NEW **: Use currentSessionOrNull()
            val session = supabase.auth.currentSessionOrNull()
            if (session != null) {
                _authState.value = AuthState.Authenticated
            } else {
                _authState.value = AuthState.NotAuthenticated
            }
        }
    }

    fun signUp(email: String, pass: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            try {
                // ** NEW **: The function is now signUpWith
                supabase.auth.signUpWith(Email) {
                    this.email = email
                    this.password = pass
                }
                _authState.value = AuthState.Authenticated
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Sign Up Failed: ${e.message}")
            }
        }
    }

    fun login(email: String, pass: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            try {
                // ** NEW **: The function is now signInWith
                supabase.auth.signInWith(Email) {
                    this.email = email
                    this.password = pass
                }
                _authState.value = AuthState.Authenticated
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Login Failed: ${e.message}")
            }
        }
    }

    fun logout() {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            try {
                // ** NEW **: This function remains the same, but is called on the auth plugin
                supabase.auth.signOut()
                _authState.value = AuthState.NotAuthenticated
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Logout Failed: ${e.message}")
            }
        }
    }
}