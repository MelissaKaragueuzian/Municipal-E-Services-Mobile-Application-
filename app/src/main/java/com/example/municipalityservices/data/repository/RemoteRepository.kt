package com.example.municipalityservices.data.repository

import com.example.municipalityservices.model.*
import kotlinx.coroutines.flow.Flow

/**
 * Interface for remote data operations (Future Firebase implementation)
 */
interface RemoteRepository {
    suspend fun syncUsers(users: List<UserModel>)
    suspend fun syncMunicipalities(municipalities: List<MunicipalityModel>)
    suspend fun syncServiceRequests(requests: List<ServiceRequestModel>)
    suspend fun uploadAttachment(filePath: String): String
    suspend fun downloadAttachment(remotePath: String): String
}


