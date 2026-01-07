package com.example.municipalityservices.data.local.DAO

import androidx.room.*
import com.example.municipalityservices.data.local.Entities.Municipality
import kotlinx.coroutines.flow.Flow

@Dao
interface MunicipalityDao {
    @Query("SELECT * FROM municipalities")
    fun getAllMunicipalities(): Flow<List<Municipality>>

    @Query("SELECT * FROM municipalities WHERE isApproved = 1")
    fun getApprovedMunicipalities(): Flow<List<Municipality>>

    @Query("SELECT * FROM municipalities WHERE id = :id")
    suspend fun getMunicipalityById(id: Long): Municipality?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMunicipality(municipality: Municipality): Long

    @Update
    suspend fun updateMunicipality(municipality: Municipality)

    @Delete
    suspend fun deleteMunicipality(municipality: Municipality)

    @Query("DELETE FROM municipalities WHERE id = :id")
    suspend fun deleteMunicipalityById(id: Long)
}


