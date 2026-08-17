package com.example.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.DashboardShell
import com.example.ui.screens.business.ProblemIntakeScreen
import com.example.ui.screens.business.AnalysisResultScreen
import com.example.ui.screens.business.ProjectConfirmationScreen
import com.example.ui.screens.student.ProjectDetailsScreen
import com.example.domain.UserRole
import com.example.domain.JsonParser
import com.example.domain.MicroProject

object Routes {
    const val LOGIN = "login"
    const val DASHBOARD_SHELL = "dashboard/{role}"
    const val PROBLEM_INTAKE = "problem_intake"
    const val ANALYSIS_RESULT = "analysis_result/{problem}"
    const val PROJECT_CONFIRMATION = "project_confirmation/{projectJson}"
    const val PROJECT_DETAILS = "project_details/{projectId}"
    
    fun dashboardRoute(role: String) = "dashboard/$role"
    fun analysisResultRoute(problem: String) = "analysis_result/$problem"
    fun projectConfirmationRoute(projectJson: String) = "project_confirmation/$projectJson"
    fun projectDetailsRoute(projectId: String) = "project_details/$projectId"
}

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = Routes.LOGIN) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginAsBusiness = {
                    navController.navigate(Routes.dashboardRoute("BUSINESS")) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onLoginAsStudent = {
                    navController.navigate(Routes.dashboardRoute("STUDENT")) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Routes.DASHBOARD_SHELL) { backStackEntry ->
            val roleStr = backStackEntry.arguments?.getString("role") ?: "STUDENT"
            val initialRole = if (roleStr == "BUSINESS") UserRole.BUSINESS else UserRole.STUDENT
            
            DashboardShell(
                initialRole = initialRole,
                onNavigateToProblemIntake = { navController.navigate(Routes.PROBLEM_INTAKE) },
                onNavigateToProjectDetails = { projectId ->
                    navController.navigate(Routes.projectDetailsRoute(projectId))
                }
            )
        }
        
        composable(Routes.PROBLEM_INTAKE) {
            ProblemIntakeScreen(
                onBack = { navController.popBackStack() },
                onAnalyze = { problem ->
                    val encoded = java.net.URLEncoder.encode(problem, "UTF-8")
                    navController.navigate(Routes.analysisResultRoute(encoded))
                }
            )
        }
        
        composable(Routes.ANALYSIS_RESULT) { backStackEntry ->
            val encodedProblem = backStackEntry.arguments?.getString("problem") ?: ""
            val problem = java.net.URLDecoder.decode(encodedProblem, "UTF-8")
            
            AnalysisResultScreen(
                problemText = problem,
                onBack = { navController.popBackStack() },
                onGenerateProject = { microProject ->
                    val json = JsonParser.moshi.adapter(MicroProject::class.java).toJson(microProject)
                    val encodedJson = java.net.URLEncoder.encode(json, "UTF-8")
                    navController.navigate(Routes.projectConfirmationRoute(encodedJson))
                }
            )
        }
        
        composable(Routes.PROJECT_CONFIRMATION) { backStackEntry ->
            val encodedJson = backStackEntry.arguments?.getString("projectJson") ?: ""
            val json = java.net.URLDecoder.decode(encodedJson, "UTF-8")
            val microProject = JsonParser.parse<MicroProject>(json)
            
            ProjectConfirmationScreen(
                microProject = microProject,
                onBack = { navController.popBackStack() },
                onConfirm = {
                    navController.navigate(Routes.dashboardRoute("BUSINESS")) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Routes.PROJECT_DETAILS) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getString("projectId") ?: ""
            ProjectDetailsScreen(
                projectId = projectId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
