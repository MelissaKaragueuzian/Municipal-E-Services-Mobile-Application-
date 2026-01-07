package com.example.municipalityservices.data

import android.content.Context
import com.example.municipalityservices.data.local.AppDatabase
import com.example.municipalityservices.data.repository.LocalRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

object DatabaseInitializer {
    private var initialized = false
    
    fun initializeIfNeeded(context: Context) {
        if (initialized) return
        
        val database = AppDatabase.getDatabase(context)
        val repository = LocalRepository(database)
        
        CoroutineScope(Dispatchers.IO).launch {
            // Check if data already exists
            val users = repository.getAllUsers().first()
            
            // Initialize with sample data only if database is empty
            if (users.isEmpty()) {
                initializeSampleData(repository)
            }
            initialized = true
        }
    }
    
    private suspend fun initializeSampleData(repository: LocalRepository) {
        // Create sample municipality
        val sampleMunicipalityId = repository.insertMunicipality(
            com.example.municipalityservices.model.MunicipalityModel(
                id = 0,
                name = "Sample Municipality",
                isApproved = true
            )
        )
        
        // Create sample service types
        repository.insertServiceType(
            com.example.municipalityservices.model.ServiceTypeModel(
                id = 0,
                name = "Waste Management",
                description = "Request for waste collection or recycling services"
            )
        )
        repository.insertServiceType(
            com.example.municipalityservices.model.ServiceTypeModel(
                id = 0,
                name = "Infrastructure Repair",
                description = "Report infrastructure issues like potholes, broken streetlights, etc."
            )
        )
        repository.insertServiceType(
            com.example.municipalityservices.model.ServiceTypeModel(
                id = 0,
                name = "Water Supply",
                description = "Issues related to water supply and quality"
            )
        )
        repository.insertServiceType(
            com.example.municipalityservices.model.ServiceTypeModel(
                id = 0,
                name = "Parks & Recreation",
                description = "Requests related to parks, playgrounds, and recreational facilities"
            )
        )
        
        // Create Citizen account
        repository.insertUser(
            com.example.municipalityservices.model.UserModel(
                id = 0,
                email = "citizen@example.com",
                password = "citizen123",
                role = "Citizen",
                municipalityId = sampleMunicipalityId,
                name = "John Citizen"
            )
        )
        
        // Create Mayor account
        repository.insertUser(
            com.example.municipalityservices.model.UserModel(
                id = 0,
                email = "mayor@example.com",
                password = "mayor123",
                role = "Mayor",
                municipalityId = sampleMunicipalityId,
                name = "Mayor Smith"
            )
        )
        
        // Create Admin account
        repository.insertUser(
            com.example.municipalityservices.model.UserModel(
                id = 0,
                email = "admin@example.com",
                password = "admin123",
                role = "Admin",
                municipalityId = null,
                name = "Admin User"
            )
        )
    }
}

