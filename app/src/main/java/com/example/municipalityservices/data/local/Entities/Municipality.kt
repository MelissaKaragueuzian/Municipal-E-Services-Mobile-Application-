package com.example.municipalityservices.data.local.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "municipalities")
data class Municipality(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val isApproved: Boolean = false // Only approved municipalities are visible to citizens
)


