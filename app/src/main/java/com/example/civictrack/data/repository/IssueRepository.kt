package com.example.civictrack.data.repository


import com.example.civictrack.data.model.Issue
import com.example.civictrack.data.remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


// A simple sealed class for representing success or failure
sealed class DataResult<out T> {
    data class Success<out T>(val data: T) : DataResult<T>()
    data class Error(val exception: Exception) : DataResult<Nothing>()
}

class IssuesRepository @Inject constructor(private val apiService: ApiService) {

    // A real implementation would handle errors, caching, etc.
    // We use Flow to emit data asynchronously.
    fun getNearbyIssues(lat: Double, lon: Double, radius: Int): Flow<List<Issue>> = flow {
        try {
            val response = apiService.getIssues(lat, lon, radius)
            if (response.isSuccessful && response.body() != null) {
                emit(response.body()!!)
            } else {
                // In a real app, emit an error state
                emit(emptyList())
            }
        } catch (e: Exception) {
            // Handle exceptions (e.g., no network)
            emit(emptyList())
        }
    }.flowOn(Dispatchers.IO) // Run network calls on the IO thread


    fun getIssueDetails(issueId: String): Flow<DataResult<Issue>> = flow {
        try {
            val response = apiService.getIssueDetails(issueId)
            if (response.isSuccessful && response.body() != null) {
                emit(DataResult.Success(response.body()!!))
            } else {
                emit(DataResult.Error(Exception("Issue not found or error fetching details.")))
            }
        } catch (e: Exception) {
            emit(DataResult.Error(e))
        }
    }.flowOn(Dispatchers.IO)
}