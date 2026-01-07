package com.example.municipalityservices.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.municipalityservices.model.ServiceRequestModel
import com.example.municipalityservices.ui.viewmodel.CitizenViewModel
import com.example.municipalityservices.ui.viewmodel.SubmitRequestViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitizenHomeScreen(
    viewModel: CitizenViewModel,
    onNavigateToRequestDetail: (Long) -> Unit,
    onNavigateToSubmitRequest: () -> Unit,
    onLogout: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Service Requests") },
                actions = {
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.Logout, contentDescription = "Logout")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToSubmitRequest) {
                Icon(Icons.Default.Add, contentDescription = "Submit Request")
            }
        }
    ) { padding ->
        if (uiState.isLoading && uiState.requests.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState.requests.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.Description,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "No requests yet",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        "Tap the + button to submit a request",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.requests) { request ->
                    RequestCard(
                        request = request,
                        onClick = { onNavigateToRequestDetail(request.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun RequestCard(
    request: ServiceRequestModel,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = request.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = request.description.take(100) + if (request.description.length > 100) "..." else "",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusChip(status = request.status)
                Text(
                    text = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                        .format(Date(request.createdAt)),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun StatusChip(status: String) {
    val color = when (status) {
        "Approved" -> MaterialTheme.colorScheme.primary
        "Rejected" -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.secondary
    }
    
    Surface(
        color = color.copy(alpha = 0.2f),
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = status,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelSmall,
            color = color
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestDetailScreen(
    requestId: Long,
    citizenViewModel: CitizenViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by citizenViewModel.uiState.collectAsStateWithLifecycle()
    val request = uiState.requests.find { it.id == requestId }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Request Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (request == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Request not found")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = request.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    StatusChip(status = request.status)
                }
                
                item {
                    Divider()
                }
                
                item {
                    Column {
                        Text(
                            "Description",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = request.description,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
                
                item {
                    Divider()
                }
                
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                "Submitted",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                SimpleDateFormat("MMM dd, yyyy 'at' HH:mm", Locale.getDefault())
                                    .format(Date(request.createdAt)),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
                
                if (request.attachmentPaths.isNotEmpty()) {
                    item {
                        Divider()
                    }
                    item {
                        Column {
                            Text(
                                "Attachments",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            val paths = request.attachmentPaths.split(",").filter { it.isNotEmpty() }
                            paths.forEach { path ->
                                Text(
                                    text = path.substringAfterLast("/"),
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubmitRequestScreen(
    submitViewModel: SubmitRequestViewModel,
    citizenViewModel: CitizenViewModel,
    onNavigateBack: () -> Unit,
    fileStorageHelper: com.example.municipalityservices.util.FileStorageHelper
) {
    val submitState by submitViewModel.uiState.collectAsStateWithLifecycle()
    val citizenState by citizenViewModel.uiState.collectAsStateWithLifecycle()
    
    val attachmentPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { submitViewModel.addAttachment(it) }
    }
    
    LaunchedEffect(submitState.submitSuccess) {
        if (submitState.submitSuccess) {
            onNavigateBack()
            submitViewModel.resetSuccess()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Submit Request") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                OutlinedTextField(
                    value = submitState.title,
                    onValueChange = submitViewModel::updateTitle,
                    label = { Text("Title *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
            
            item {
                OutlinedTextField(
                    value = submitState.description,
                    onValueChange = submitViewModel::updateDescription,
                    label = { Text("Description *") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    maxLines = 5
                )
            }
            
            item {
                var municipalityExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = municipalityExpanded,
                    onExpandedChange = { municipalityExpanded = !municipalityExpanded }
                ) {
                    OutlinedTextField(
                        value = citizenState.municipalities.find { it.id == submitState.selectedMunicipalityId }?.name ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Municipality *") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = municipalityExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = municipalityExpanded,
                        onDismissRequest = { municipalityExpanded = false }
                    ) {
                        citizenState.municipalities.forEach { municipality ->
                            DropdownMenuItem(
                                text = { Text(municipality.name) },
                                onClick = {
                                    submitViewModel.selectMunicipality(municipality.id)
                                    municipalityExpanded = false
                                }
                            )
                        }
                    }
                }
            }
            
            item {
                var serviceTypeExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = serviceTypeExpanded,
                    onExpandedChange = { serviceTypeExpanded = !serviceTypeExpanded }
                ) {
                    OutlinedTextField(
                        value = citizenState.serviceTypes.find { it.id == submitState.selectedServiceTypeId }?.name ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Service Type *") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = serviceTypeExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = serviceTypeExpanded,
                        onDismissRequest = { serviceTypeExpanded = false }
                    ) {
                        citizenState.serviceTypes.forEach { serviceType ->
                            DropdownMenuItem(
                                text = { Text(serviceType.name) },
                                onClick = {
                                    submitViewModel.selectServiceType(serviceType.id)
                                    serviceTypeExpanded = false
                                }
                            )
                        }
                    }
                }
            }
            
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Attachments",
                        style = MaterialTheme.typography.titleSmall
                    )
                    TextButton(onClick = { attachmentPickerLauncher.launch("*/*") }) {
                        Icon(Icons.Default.AttachFile, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add")
                    }
                }
                
                if (submitState.attachmentUris.isNotEmpty()) {
                    submitState.attachmentUris.forEach { uri ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = uri.toString().substringAfterLast("/"),
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = { submitViewModel.removeAttachment(uri) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Remove")
                            }
                        }
                    }
                }
            }
            
            if (submitState.errorMessage != null) {
                item {
                    Text(
                        text = submitState.errorMessage ?: "",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            
            item {
                Button(
                    onClick = submitViewModel::submitRequest,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = !submitState.isLoading
                ) {
                    if (submitState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Submit Request")
                    }
                }
            }
        }
    }
}


