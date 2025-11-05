package com.example.tp_projet.data.local
import androidx.room.*
import com.example.tp_projet.model.Role
import kotlinx.coroutines.flow.flow

@Dao
interface UserDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user:UserEntity)
    
}


