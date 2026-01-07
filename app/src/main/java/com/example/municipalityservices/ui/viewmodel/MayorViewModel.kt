package com.example.municipalityservices.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.municipalityservices.data.repository.LocalRepository
import com.example.municipalityservices.model.ServiceRequestModel
import com.example.municipalityservices.util.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class MayorUiState(
    val requests: List<ServiceRequestModel> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class MayorViewModel(private val repository: LocalRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(MayorUiState())
    val uiState: StateFlow<MayorUiState> = _uiState
    
    init {
        loadRequests()
    }
    
    private fun loadRequests() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                val user = SessionManager.getCurrentUser()
                if (user?.municipalityId != null) {
                    repository.getRequestsByMunicipality(user.municipalityId).collect { requests ->
                        _uiState.value = _uiState.value.copy(
                            requests = requests,
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error loading requests: ${e.message}"
                )
            }
        }
    }
    
    fun approveRequest(request: ServiceRequestModel) {
        viewModelScope.launch {
            try {
                val user = SessionManager.getCurrentUser()
                val updatedRequest = request.copy(
                    status = "Approved",
                    mayorId = user?.id
                )
                repository.updateRequest(updatedRequest)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error approving request: ${e.message}"
                )
            }
        }
    }
    
    fun rejectRequest(request: ServiceRequestModel) {
        viewModelScope.launch {
            try {
                val user = SessionManager.getCurrentUser()
                val updatedRequest = request.copy(
                    status = "Rejected",
                    mayorId = user?.id
                )
                repository.updateRequest(updatedRequest)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error rejecting request: ${e.message}"
                )
            }
        }
    }
    
    fun refresh() {
        loadRequests()
    }
}


