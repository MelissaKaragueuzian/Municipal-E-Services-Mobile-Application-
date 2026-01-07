package com.example.municipalityservices.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.municipalityservices.data.local.DAO.MunicipalityDao
import com.example.municipalityservices.data.local.DAO.ServiceRequestDao
import com.example.municipalityservices.data.local.DAO.ServiceTypeDao
import com.example.municipalityservices.data.local.DAO.UserDao
import com.example.municipalityservices.data.local.Entities.Municipality
import com.example.municipalityservices.data.local.Entities.ServiceRequest
import com.example.municipalityservices.data.local.Entities.ServiceType
import com.example.municipalityservices.data.local.Entities.User

@Database(
    entities = [User::class, Municipality::class, ServiceType::class, ServiceRequest::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun municipalityDao(): MunicipalityDao
    abstract fun serviceTypeDao(): ServiceTypeDao
    abstract fun serviceRequestDao(): ServiceRequestDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "municipality_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}


