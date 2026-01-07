package com.example.municipalityservices.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.municipalityservices.ui.screens.*
import com.example.municipalityservices.ui.viewmodel.*
import com.example.municipalityservices.util.FileStorageHelper
import com.example.municipalityservices.util.SessionManager

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object CitizenHome : Screen("citizen_home")
    object CitizenRequestDetail : Screen("citizen_request_detail/{requestId}") {
        fun createRoute(requestId: Long) = "citizen_request_detail/$requestId"
    }
    object CitizenSubmitRequest : Screen("citizen_submit_request")
    object MayorHome : Screen("mayor_home")
    object MayorRequestDetail : Screen("mayor_request_detail/{requestId}") {
        fun createRoute(requestId: Long) = "mayor_request_detail/$requestId"
    }
    object AdminHome : Screen("admin_home")
    object AdminMunicipalities : Screen("admin_municipalities")
    object AdminUsers : Screen("admin_users")
}

@Composable
fun NavGraph(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    citizenViewModel: CitizenViewModel,
    mayorViewModel: MayorViewModel,
    adminViewModel: AdminViewModel,
    submitRequestViewModel: SubmitRequestViewModel,
    fileStorageHelper: FileStorageHelper,
    startDestination: String = Screen.Login.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = {
                    // Navigation is handled in MainActivity via LaunchedEffect
                }
            )
        }
        
        composable(Screen.CitizenHome.route) {
            CitizenHomeScreen(
                viewModel = citizenViewModel,
                onNavigateToRequestDetail = { requestId ->
                    navController.navigate(Screen.CitizenRequestDetail.createRoute(requestId))
                },
                onNavigateToSubmitRequest = {
                    navController.navigate(Screen.CitizenSubmitRequest.route)
                },
                onLogout = {
                    SessionManager.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        
        composable(
            route = "citizen_request_detail/{requestId}",
            arguments = listOf(navArgument("requestId") { type = NavType.LongType })
        ) { backStackEntry ->
            val requestId = backStackEntry.arguments?.getLong("requestId")
            if (requestId != null) {
                RequestDetailScreen(
                    requestId = requestId,
                    citizenViewModel = citizenViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
        
        composable(Screen.CitizenSubmitRequest.route) {
            SubmitRequestScreen(
                submitViewModel = submitRequestViewModel,
                citizenViewModel = citizenViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                    citizenViewModel.refresh()
                },
                fileStorageHelper = fileStorageHelper
            )
        }
        
        composable(Screen.MayorHome.route) {
            MayorHomeScreen(
                viewModel = mayorViewModel,
                onNavigateToRequestDetail = { requestId ->
                    navController.navigate(Screen.MayorRequestDetail.createRoute(requestId))
                },
                onLogout = {
                    SessionManager.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        
        composable(
            route = "mayor_request_detail/{requestId}",
            arguments = listOf(navArgument("requestId") { type = NavType.LongType })
        ) { backStackEntry ->
            val requestId = backStackEntry.arguments?.getLong("requestId")
            if (requestId != null) {
                MayorRequestDetailScreen(
                    requestId = requestId,
                    mayorViewModel = mayorViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
        
        composable(Screen.AdminHome.route) {
            AdminHomeScreen(
                viewModel = adminViewModel,
                onNavigateToMunicipalities = {
                    navController.navigate(Screen.AdminMunicipalities.route)
                },
                onNavigateToUsers = {
                    navController.navigate(Screen.AdminUsers.route)
                },
                onLogout = {
                    SessionManager.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.AdminMunicipalities.route) {
            AdminMunicipalitiesScreen(
                viewModel = adminViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.AdminUsers.route) {
            AdminUsersScreen(
                viewModel = adminViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

