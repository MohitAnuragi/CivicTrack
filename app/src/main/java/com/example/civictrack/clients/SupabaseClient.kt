package com.example.civictrack.clients

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient

object SupabaseManager {

    var supabaseClient = createSupabaseClient(
        supabaseUrl = "https://rvmbnjbuzpfgpiyxufdw.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InJ2bWJuamJ1enBmZ3BpeXh1ZmR3Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTQ0NzAyNzUsImV4cCI6MjA3MDA0NjI3NX0.37wK43D-kctT8YyEIhaI7TChux90hTp1BK9zMuxWIas"
    ) {
        install(Auth)
    }
}