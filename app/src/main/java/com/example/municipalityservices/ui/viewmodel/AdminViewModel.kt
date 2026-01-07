package com.example.municipalityservices.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.municipalityservices.data.repository.LocalRepository
import com.example.municipalityservices.model.MunicipalityModel
import com.example.municipalityservices.model.UserModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class AdminUiState(
    val municipalities: List<MunicipalityModel> = emptyList(),
    val users: List<UserModel> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class AdminViewModel(private val repository: LocalRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(AdminUiState())
    val uiState: StateFlow<AdminUiState> = _uiState
    
    init {
        loadData()
    }
    
    private fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                combine(
                    repository.getAllMunicipalities(),
                    repository.getAllUsers()
                ) { municipalities, users ->
                    AdminUiState(
                        municipalities = municipalities,
                        users = users,
                        isLoading = false,
                        errorMessage = null
                    )
                }.collect { state ->
                    _uiState.value = state
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error loading data: ${e.message}"
                )
            }
        }
    }
    
    fun createMunicipality(name: String) {
        viewModelScope.launch {
            try {
                repository.insertMunicipality(
                    MunicipalityModel(0, name.trim(), isApproved = false)
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error creating municipality: ${e.message}"
                )
            }
        }
    }
    
    fun approveMunicipality(municipality: MunicipalityModel) {
        viewModelScope.launch {
            try {
                repository.updateMunicipality(municipality.copy(isApproved = true))
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error approving municipality: ${e.message}"
                )
            }
        }
    }
    
    fun deleteMunicipality(municipality: MunicipalityModel) {
        viewModelScope.launch {
            try {
                repository.deleteMunicipality(municipality)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error deleting municipality: ${e.message}"
                )
            }
        }
    }
    
    fun createUser(email: String, password: String, role: String, municipalityId: Long? = null, name: String = "") {
        viewModelScope.launch {
            try {
                repository.insertUser(
                    UserModel(0, email.trim(), password, role, municipalityId, name.trim())
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error creating user: ${e.message}"
                )
            }
        }
    }
    
    fun deleteUser(user: UserModel) {
        viewModelScope.launch {
            try {
                repository.deleteUser(user)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error deleting user: ${e.message}"
                )
            }
        }
    }
    
    fun refresh() {
        loadData()
    }
}

