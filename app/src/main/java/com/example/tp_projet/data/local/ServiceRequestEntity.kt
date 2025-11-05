package com.example.tp_projet.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index
import androidx.room.ForeignKey


@Entity(tableName = "requests", foreignKeys = [
    ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["id"],
        childColumns = ["citizenId"],
        onDelete = ForeignKey.NO_ACTION
    ),

    ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["id"],
        childColumns = ["municipalitiesId"],
        onDelete = ForeignKey.NO_ACTION
    )
],

    indices = [Index("citizenId"), Index("municipalityId")]
)

data class ServiceRequestEntity (
    @PrimaryKey val id:String,
    val citizenId: UserEntity,
    val municipalityId: String,
    val serviceType:String,
    val description:String,
    val status:String,
    val createAt: Long,
    val updateAt:Long,
)
