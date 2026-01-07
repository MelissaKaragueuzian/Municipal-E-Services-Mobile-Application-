package com.example.municipalityservices.util

import com.example.municipalityservices.model.UserModel

object SessionManager {
    private var currentUser: UserModel? = null
    
    fun login(user: UserModel) {
        currentUser = user
    }
    
    fun logout() {
        currentUser = null
    }
    
    fun getCurrentUser(): UserModel? = currentUser
    
    fun isLoggedIn(): Boolean = currentUser != null
    
    fun isCitizen(): Boolean = currentUser?.role == "Citizen"
    fun isMayor(): Boolean = currentUser?.role == "Mayor"
    fun isAdmin(): Boolean = currentUser?.role == "Admin"
}


