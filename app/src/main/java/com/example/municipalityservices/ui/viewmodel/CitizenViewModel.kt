package com.example.municipalityservices.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.municipalityservices.data.repository.LocalRepository
import com.example.municipalityservices.model.MunicipalityModel
import com.example.municipalityservices.model.ServiceRequestModel
import com.example.municipalityservices.model.ServiceTypeModel
import com.example.municipalityservices.util.SessionManager
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class CitizenUiState(
    val requests: List<ServiceRequestModel> = emptyList(),
    val municipalities: List<MunicipalityModel> = emptyList(),
    val serviceTypes: List<ServiceTypeModel> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class CitizenViewModel(private val repository: LocalRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(CitizenUiState())
    val uiState: StateFlow<CitizenUiState> = _uiState
    
    init {
        loadData()
    }
    
    private fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                val user = SessionManager.getCurrentUser()
                if (user != null) {
                    combine(
                        repository.getRequestsByCitizen(user.id),
                        repository.getApprovedMunicipalities(),
                        repository.getAllServiceTypes()
                    ) { requests, municipalities, serviceTypes ->
                        CitizenUiState(
                            requests = requests,
                            municipalities = municipalities,
                            serviceTypes = serviceTypes,
                            isLoading = false,
                            errorMessage = null
                        )
                    }.collect { state ->
                        _uiState.value = state
                    }
                } else {
                    combine(
                        repository.getApprovedMunicipalities(),
                        repository.getAllServiceTypes()
                    ) { municipalities, serviceTypes ->
                        CitizenUiState(
                            requests = emptyList(),
                            municipalities = municipalities,
                            serviceTypes = serviceTypes,
                            isLoading = false,
                            errorMessage = null
                        )
                    }.collect { state ->
                        _uiState.value = state
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error loading data: ${e.message}"
                )
            }
        }
    }
    
    fun refresh() {
        loadData()
    }
}

