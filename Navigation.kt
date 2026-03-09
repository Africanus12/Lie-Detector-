package com.liedetector.app.ui.navigation

import android.net.Uri
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.liedetector.app.data.database.LieDetectorDatabase
import com.liedetector.app.data.repository.AnalysisRepository
import com.liedetector.app.ui.screens.*

object Routes {
    const val HOME = "home"
    const val ANALYSIS = "analysis/{message}"
    const val HISTORY = "history"
    const val SCREENSHOT = "screenshot"
    const val RANKING = "ranking"
    const val PROFILE = "profile"
    const val GAME = "game"

    fun analysis(message: String) = "analysis/${Uri.encode(message)}"
}

@Composable
fun LieDetectorNavHost() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val database = remember { LieDetectorDatabase.getDatabase(context) }
    val repository = remember { AnalysisRepository(database.analysisDao()) }
    val analyses by repository.getAllAnalyses().collectAsState(initial = emptyList())

    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.HOME) {
            HomeScreen(
                onAnalyze = { message ->
                    navController.navigate(Routes.analysis(message))
                },
                onUploadScreenshot = {
                    navController.navigate(Routes.SCREENSHOT)
                },
                onNavigateToHistory = {
                    navController.navigate(Routes.HISTORY)
                },
                onNavigateToGame = {
                    navController.navigate(Routes.GAME)
                },
                onNavigateToProfile = {
                    navController.navigate(Routes.PROFILE)
                },
                onNavigateToRanking = {
                    navController.navigate(Routes.RANKING)
                }
            )
        }

        composable(
            route = Routes.ANALYSIS,
            arguments = listOf(navArgument("message") { type = NavType.StringType })
        ) { backStackEntry ->
            val message = Uri.decode(backStackEntry.arguments?.getString("message") ?: "")
            AnalysisScreen(
                message = message,
                onBack = { navController.popBackStack() },
                onNewAnalysis = {
                    navController.popBackStack(Routes.HOME, inclusive = false)
                }
            )
        }

        composable(Routes.HISTORY) {
            HistoryScreen(
                analyses = analyses,
                onBack = { navController.popBackStack() },
                onAnalysisClick = { entity ->
                    navController.navigate(Routes.analysis(entity.message))
                }
            )
        }

        composable(Routes.SCREENSHOT) {
            ScreenshotAnalyzerScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.RANKING) {
            RankingScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.PROFILE) {
            HonestyProfileScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.GAME) {
            GameScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
