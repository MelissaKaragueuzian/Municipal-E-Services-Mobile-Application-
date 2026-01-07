package com.example.municipalityservices.data.local.DAO

import androidx.room.*
import com.example.municipalityservices.data.local.Entities.ServiceType
import kotlinx.coroutines.flow.Flow

@Dao
interface ServiceTypeDao {
    @Query("SELECT * FROM service_types")
    fun getAllServiceTypes(): Flow<List<ServiceType>>

    @Query("SELECT * FROM service_types WHERE id = :id")
    suspend fun getServiceTypeById(id: Long): ServiceType?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServiceType(serviceType: ServiceType): Long

    @Delete
    suspend fun deleteServiceType(serviceType: ServiceType)
}


