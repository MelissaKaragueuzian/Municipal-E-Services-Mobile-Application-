package com.example.municipalityservices.data.local.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "service_requests")
data class ServiceRequest(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val citizenId: Long,
    val municipalityId: Long,
    val serviceTypeId: Long,
    val title: String,
    val description: String,
    val status: String = "Pending", // "Pending", "Approved", "Rejected"
    val createdAt: Long = System.currentTimeMillis(),
    val attachmentPaths: String = "", // Comma-separated list of file paths
    val mayorId: Long? = null // Set when mayor reviews
)


