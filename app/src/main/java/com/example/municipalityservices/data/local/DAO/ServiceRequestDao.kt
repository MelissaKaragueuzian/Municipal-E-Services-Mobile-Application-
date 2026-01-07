package com.example.municipalityservices.data.local.DAO

import androidx.room.*
import com.example.municipalityservices.data.local.Entities.ServiceRequest
import kotlinx.coroutines.flow.Flow

@Dao
interface ServiceRequestDao {
    @Query("SELECT * FROM service_requests WHERE citizenId = :citizenId ORDER BY createdAt DESC")
    fun getRequestsByCitizen(citizenId: Long): Flow<List<ServiceRequest>>

    @Query("SELECT * FROM service_requests WHERE municipalityId = :municipalityId ORDER BY createdAt DESC")
    fun getRequestsByMunicipality(municipalityId: Long): Flow<List<ServiceRequest>>

    @Query("SELECT * FROM service_requests WHERE id = :id")
    suspend fun getRequestById(id: Long): ServiceRequest?

    @Query("SELECT * FROM service_requests ORDER BY createdAt DESC")
    fun getAllRequests(): Flow<List<ServiceRequest>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRequest(request: ServiceRequest): Long

    @Update
    suspend fun updateRequest(request: ServiceRequest)

    @Delete
    suspend fun deleteRequest(request: ServiceRequest)
}


