package com.example.tp_projet.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.tp_projet.model.Role

@Entity(tableName = "users")
data class UserEntity
(
    @PrimaryKey val id:String,
    val name:String,
    val municipalityid:String,
    val role:Role,
)

