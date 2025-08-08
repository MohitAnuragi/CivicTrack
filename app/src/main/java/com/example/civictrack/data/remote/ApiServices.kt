package com.example.civictrack.data.remote


import com.example.civictrack.data.model.Issue
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // Example: GET /issues?lat=26.91&lon=75.78&radius=5
    @GET("issues")
    suspend fun getIssues(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("radius") radiusInKm: Int
    ): Response<List<Issue>>

    // Example: GET /issues/issue_id_123
    @GET("issues/{id}")
    suspend fun getIssueDetails(
        @Path("id") issueId: String
    ): Response<Issue>

    // Other endpoints like POST for reporting an issue would go here
}