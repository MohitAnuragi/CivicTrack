package com.example.civictrack.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
data class Issue(
    val id: String,
    val title: String,
    val description: String,
    val category: IssueCategory,
    val status: IssueStatus,
    val location: GeoLocation,
    @Json(name = "image_urls")
    val imageUrls: List<String>,
    @Json(name = "reported_at")
    val reportedAt: Date,
    @Json(name = "reporter_id")
    val reporterId: String? // Nullable for anonymous reports
)

@JsonClass(generateAdapter = true)
data class GeoLocation(
    val latitude: Double,
    val longitude: Double
)

enum class IssueCategory {
    ROADS,
    LIGHTING,
    WATER_SUPPLY,
    CLEANLINESS,
    PUBLIC_SAFETY,
    OBSTRUCTIONS
}

enum class IssueStatus {
    REPORTED,
    IN_PROGRESS,
    RESOLVED
}