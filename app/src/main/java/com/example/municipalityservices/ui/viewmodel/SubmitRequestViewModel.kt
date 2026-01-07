package com.example.municipalityservices.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.municipalityservices.data.repository.LocalRepository
import com.example.municipalityservices.model.ServiceRequestModel
import com.example.municipalityservices.util.FileStorageHelper
import com.example.municipalityservices.util.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class SubmitRequestUiState(
    val title: String = "",
    val description: String = "",
    val selectedMunicipalityId: Long? = null,
    val selectedServiceTypeId: Long? = null,
    val attachmentUris: List<Uri> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val submitSuccess: Boolean = false
)

class SubmitRequestViewModel(
    private val repository: LocalRepository,
    private val fileStorageHelper: FileStorageHelper
) : ViewModel() {
    private val _uiState = MutableStateFlow(SubmitRequestUiState())
    val uiState: StateFlow<SubmitRequestUiState> = _uiState
    
    fun updateTitle(title: String) {
        _uiState.value = _uiState.value.copy(title = title, errorMessage = null)
    }
    
    fun updateDescription(description: String) {
        _uiState.value = _uiState.value.copy(description = description, errorMessage = null)
    }
    
    fun selectMunicipality(municipalityId: Long?) {
        _uiState.value = _uiState.value.copy(selectedMunicipalityId = municipalityId, errorMessage = null)
    }
    
    fun selectServiceType(serviceTypeId: Long?) {
        _uiState.value = _uiState.value.copy(selectedServiceTypeId = serviceTypeId, errorMessage = null)
    }
    
    fun addAttachment(uri: Uri) {
        val currentUris = _uiState.value.attachmentUris.toMutableList()
        if (!currentUris.contains(uri)) {
            currentUris.add(uri)
            _uiState.value = _uiState.value.copy(attachmentUris = currentUris)
        }
    }
    
    fun removeAttachment(uri: Uri) {
        val currentUris = _uiState.value.attachmentUris.toMutableList()
        currentUris.remove(uri)
        _uiState.value = _uiState.value.copy(attachmentUris = currentUris)
    }
    
    fun submitRequest() {
        val state = _uiState.value
        
        if (state.title.trim().isEmpty() || state.description.trim().isEmpty()) {
            _uiState.value = state.copy(errorMessage = "Please fill in all required fields")
            return
        }
        
        if (state.selectedMunicipalityId == null) {
            _uiState.value = state.copy(errorMessage = "Please select a municipality")
            return
        }
        
        if (state.selectedServiceTypeId == null) {
            _uiState.value = state.copy(errorMessage = "Please select a service type")
            return
        }
        
        val user = SessionManager.getCurrentUser()
        if (user == null) {
            _uiState.value = state.copy(errorMessage = "User not logged in")
            return
        }
        
        viewModelScope.launch {
            _uiState.value = state.copy(isLoading = true, errorMessage = null)
            
            try {
                // Create a temporary request to get an ID for file naming
                val tempRequest = ServiceRequestModel(
                    0,
                    user.id,
                    state.selectedMunicipalityId,
                    state.selectedServiceTypeId,
                    state.title.trim(),
                    state.description.trim(),
                    "Pending",
                    System.currentTimeMillis(),
                    "",
                    null
                )
                
                val requestId = repository.insertRequest(tempRequest)
                
                // Save attachments
                val attachmentPaths = mutableListOf<String>()
                for (uri in state.attachmentUris) {
                    try {
                        val filePath = fileStorageHelper.saveAttachment(uri, requestId)
                        attachmentPaths.add(filePath)
                    } catch (e: Exception) {
                        // Continue even if some attachments fail
                        e.printStackTrace()
                    }
                }
                
                // Update request with attachment paths
                val updatedRequest = tempRequest.copy(
                    id = requestId,
                    attachmentPaths = attachmentPaths.joinToString(",")
                )
                repository.updateRequest(updatedRequest)
                
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    submitSuccess = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error submitting request: ${e.message}"
                )
            }
        }
    }
    
    fun resetSuccess() {
        _uiState.value = SubmitRequestUiState()
    }
}


