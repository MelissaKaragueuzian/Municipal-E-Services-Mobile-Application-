package com.example.tp_projet.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index
import androidx.room.ForeignKey

data class AttachmentEntity
(
        @PrimaryKey val id: String,
        val requestId: String,
        val filename: String ,
        val localUri: String?
)