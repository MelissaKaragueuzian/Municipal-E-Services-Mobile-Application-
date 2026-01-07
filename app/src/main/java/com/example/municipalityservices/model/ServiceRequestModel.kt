package com.example.municipalityservices.model

data class ServiceRequestModel(
    val id: Long,
    val citizenId: Long,
    val municipalityId: Long,
    val serviceTypeId: Long,
    val title: String,
    val description: String,
    val status: String,
    val createdAt: Long,
    val attachmentPaths: String,
    val mayorId: Long?
)


