package com.example.municipalityservices.data.local.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val email: String,
    val password: String,
    val role: String, // "Citizen", "Mayor", "Admin"
    val municipalityId: Long? = null, // null for Admin, set for Citizen and Mayor
    val name: String = ""
)


