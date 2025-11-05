package com.example.tp_projet.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "municipalities")

data class MunicipalityEntity
(
    @PrimaryKey val id:String,
    val name:String,
    val approved:Boolean = false
)