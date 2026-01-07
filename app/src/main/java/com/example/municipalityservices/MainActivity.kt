package com.example.municipalityservices

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.municipalityservices.data.DatabaseInitializer
import com.example.municipalityservices.data.local.AppDatabase
import com.example.municipalityservices.data.repository.LocalRepository
import com.example.municipalityservices.ui.navigation.NavGraph
import com.example.municipalityservices.ui.navigation.Screen
import com.example.municipalityservices.ui.screens.LoginScreen
import com.example.municipalityservices.ui.theme.MunicipalityServicesTheme
import com.example.municipalityservices.ui.viewmodel.*
import com.example.municipalityservices.util.FileStorageHelper
import com.example.municipalityservices.util.SessionManager

class MainActivity : ComponentActivity() {
    private lateinit var database: AppDatabase
    private lateinit var repository: LocalRepository
    private lateinit var fileStorageHelper: FileStorageHelper
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize database
        database = AppDatabase.getDatabase(this)
        repository = LocalRepository(database)
        
        // Initialize file storage
        fileStorageHelper = FileStorageHelper(this)
        
        // Initialize sample data if needed
        DatabaseInitializer.initializeIfNeeded(this)
        
        enableEdgeToEdge()
        setContent {
            MunicipalityServicesTheme {
                val navController = rememberNavController()
                
                // Create ViewModels
                val loginViewModel: LoginViewModel = viewModel {
                    LoginViewModel(repository)
                }
                
                val citizenViewModel: CitizenViewModel = viewModel {
                    CitizenViewModel(repository)
                }
                
                val mayorViewModel: MayorViewModel = viewModel {
                    MayorViewModel(repository)
                }
                
                val adminViewModel: AdminViewModel = viewModel {
                    AdminViewModel(repository)
                }
                
                val submitRequestViewModel: SubmitRequestViewModel = viewModel {
                    SubmitRequestViewModel(repository, fileStorageHelper)
                }
                
                // Determine start destination based on session
                val startDestination = remember {
                    val currentUser = SessionManager.getCurrentUser()
                    when {
                        currentUser == null -> Screen.Login.route
                        SessionManager.isCitizen() -> Screen.CitizenHome.route
                        SessionManager.isMayor() -> Screen.MayorHome.route
                        SessionManager.isAdmin() -> Screen.AdminHome.route
                        else -> Screen.Login.route
                    }
                }
                
                NavGraph(
                    navController = navController,
                    loginViewModel = loginViewModel,
                    citizenViewModel = citizenViewModel,
                    mayorViewModel = mayorViewModel,
                    adminViewModel = adminViewModel,
                    submitRequestViewModel = submitRequestViewModel,
                    fileStorageHelper = fileStorageHelper,
                    startDestination = startDestination
                )
                
                // Handle login success navigation
                val loginState by loginViewModel.uiState.collectAsState()
                LaunchedEffect(loginState.loginSuccess) {
                    if (loginState.loginSuccess) {
                        val user = SessionManager.getCurrentUser()
                        val destination = when (user?.role) {
                            "Citizen" -> Screen.CitizenHome.route
                            "Mayor" -> Screen.MayorHome.route
                            "Admin" -> Screen.AdminHome.route
                            else -> Screen.Login.route
                        }
                        navController.navigate(destination) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                }
            }
        }
    }
}