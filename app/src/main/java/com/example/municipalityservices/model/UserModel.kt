package com.example.municipalityservices.model

data class UserModel(
    val id: Long,
    val email: String,
    val password: String,
    val role: String,
    val municipalityId: Long?,
    val name: String
)


