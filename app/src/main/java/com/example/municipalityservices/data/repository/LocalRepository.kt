package com.example.municipalityservices.data.repository

import com.example.municipalityservices.data.local.AppDatabase
import com.example.municipalityservices.data.local.Entities.*
import com.example.municipalityservices.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalRepository(private val database: AppDatabase) {
    
    // User operations
    suspend fun login(email: String, password: String): UserModel? {
        val user = database.userDao().login(email, password)
        return user?.toModel()
    }
    
    suspend fun getUserById(id: Long): UserModel? {
        val user = database.userDao().getUserById(id)
        return user?.toModel()
    }
    
    fun getAllUsers(): Flow<List<UserModel>> {
        return database.userDao().getAllUsers().map { users -> users.map { it.toModel() } }
    }
    
    fun getUsersByRole(role: String): Flow<List<UserModel>> {
        return database.userDao().getUsersByRole(role).map { users -> users.map { it.toModel() } }
    }
    
    suspend fun insertUser(user: UserModel): Long {
        return database.userDao().insertUser(user.toEntity())
    }
    
    suspend fun deleteUser(user: UserModel) {
        database.userDao().deleteUser(user.toEntity())
    }
    
    suspend fun deleteUserById(id: Long) {
        database.userDao().deleteUserById(id)
    }
    
    // Municipality operations
    fun getAllMunicipalities(): Flow<List<MunicipalityModel>> {
        return database.municipalityDao().getAllMunicipalities().map { municipalities -> 
            municipalities.map { it.toModel() }
        }
    }
    
    fun getApprovedMunicipalities(): Flow<List<MunicipalityModel>> {
        return database.municipalityDao().getApprovedMunicipalities().map { municipalities -> 
            municipalities.map { it.toModel() }
        }
    }
    
    suspend fun getMunicipalityById(id: Long): MunicipalityModel? {
        val municipality = database.municipalityDao().getMunicipalityById(id)
        return municipality?.toModel()
    }
    
    suspend fun insertMunicipality(municipality: MunicipalityModel): Long {
        return database.municipalityDao().insertMunicipality(municipality.toEntity())
    }
    
    suspend fun updateMunicipality(municipality: MunicipalityModel) {
        database.municipalityDao().updateMunicipality(municipality.toEntity())
    }
    
    suspend fun deleteMunicipality(municipality: MunicipalityModel) {
        database.municipalityDao().deleteMunicipality(municipality.toEntity())
    }
    
    suspend fun deleteMunicipalityById(id: Long) {
        database.municipalityDao().deleteMunicipalityById(id)
    }
    
    // Service Type operations
    fun getAllServiceTypes(): Flow<List<ServiceTypeModel>> {
        return database.serviceTypeDao().getAllServiceTypes().map { serviceTypes -> 
            serviceTypes.map { it.toModel() }
        }
    }
    
    suspend fun getServiceTypeById(id: Long): ServiceTypeModel? {
        val serviceType = database.serviceTypeDao().getServiceTypeById(id)
        return serviceType?.toModel()
    }
    
    suspend fun insertServiceType(serviceType: ServiceTypeModel): Long {
        return database.serviceTypeDao().insertServiceType(serviceType.toEntity())
    }
    
    suspend fun deleteServiceType(serviceType: ServiceTypeModel) {
        database.serviceTypeDao().deleteServiceType(serviceType.toEntity())
    }
    
    // Service Request operations
    fun getRequestsByCitizen(citizenId: Long): Flow<List<ServiceRequestModel>> {
        return database.serviceRequestDao().getRequestsByCitizen(citizenId).map { requests -> 
            requests.map { it.toModel() }
        }
    }
    
    fun getRequestsByMunicipality(municipalityId: Long): Flow<List<ServiceRequestModel>> {
        return database.serviceRequestDao().getRequestsByMunicipality(municipalityId).map { requests -> 
            requests.map { it.toModel() }
        }
    }
    
    suspend fun getRequestById(id: Long): ServiceRequestModel? {
        val request = database.serviceRequestDao().getRequestById(id)
        return request?.toModel()
    }
    
    fun getAllRequests(): Flow<List<ServiceRequestModel>> {
        return database.serviceRequestDao().getAllRequests().map { requests -> 
            requests.map { it.toModel() }
        }
    }
    
    suspend fun insertRequest(request: ServiceRequestModel): Long {
        return database.serviceRequestDao().insertRequest(request.toEntity())
    }
    
    suspend fun updateRequest(request: ServiceRequestModel) {
        database.serviceRequestDao().updateRequest(request.toEntity())
    }
    
    suspend fun deleteRequest(request: ServiceRequestModel) {
        database.serviceRequestDao().deleteRequest(request.toEntity())
    }
    
    // Extension functions for Entity <-> Model conversion
    private fun User.toModel() = UserModel(id, email, password, role, municipalityId, name)
    private fun UserModel.toEntity() = User(id, email, password, role, municipalityId, name)
    
    private fun Municipality.toModel() = MunicipalityModel(id, name, isApproved)
    private fun MunicipalityModel.toEntity() = Municipality(id, name, isApproved)
    
    private fun ServiceType.toModel() = ServiceTypeModel(id, name, description)
    private fun ServiceTypeModel.toEntity() = ServiceType(id, name, description)
    
    private fun ServiceRequest.toModel() = ServiceRequestModel(
        id, citizenId, municipalityId, serviceTypeId, title, description, 
        status, createdAt, attachmentPaths, mayorId
    )
    private fun ServiceRequestModel.toEntity() = ServiceRequest(
        id, citizenId, municipalityId, serviceTypeId, title, description, 
        status, createdAt, attachmentPaths, mayorId
    )
}


