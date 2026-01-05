package com.abacus.practice.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.abacus.practice.domain.model.PracticeMode
import com.abacus.practice.ui.screens.*

/**
 * Main navigation graph for the app
 */
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route
    ) {
        // Dashboard
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onModeSelected = { mode ->
                    // Calculator opens directly, others go to config
                    if (mode == PracticeMode.CALCULATOR) {
                        navController.navigate(Screen.Calculator.route)
                    } else if (mode == PracticeMode.STOPWATCH) {
                        navController.navigate(Screen.Stopwatch.route)
                    } else {
                        navController.navigate(Screen.Config.createRoute(mode.name))
                    }
                },
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }
        
        // Config Screen
        composable(
            route = Screen.Config.route,
            arguments = listOf(navArgument("mode") { type = NavType.StringType })
        ) { backStackEntry ->
            val modeStr = backStackEntry.arguments?.getString("mode") ?: "ADDITION"
            val mode = try {
                PracticeMode.valueOf(modeStr)
            } catch (e: Exception) {
                PracticeMode.ADDITION
            }
            
            ConfigScreen(
                mode = mode,
                onStartPractice = { digits, rows, timeSeconds ->
                    navController.navigate(
                        Screen.Practice.createRoute(modeStr, digits, rows, timeSeconds)
                    )
                },
                onStartTablePractice = { tableNumber, count ->
                    navController.navigate(
                        Screen.TablePractice.createRoute(tableNumber, count)
                    )
                },
                onStartStopwatch = {
                    navController.navigate(Screen.Stopwatch.route)
                },
                onStartRecall = { digits, count, timeSeconds ->
                    navController.navigate(
                        Screen.RecallNumbers.createRoute(digits, count, timeSeconds)
                    )
                },
                onStartCalculator = {
                    navController.navigate(Screen.Calculator.route)
                },
                onStartMultiply = { digits, count, timeSeconds ->
                    navController.navigate(
                        Screen.Multiply.createRoute(digits, count, timeSeconds)
                    )
                },
                onBackClick = { navController.popBackStack() }
            )
        }
        
        // Practice Screen (Addition, Add/Sub, Mix Add/Sub, Division, Speed Test, Flash Cards, Listening)
        composable(
            route = Screen.Practice.route,
            arguments = listOf(
                navArgument("mode") { type = NavType.StringType },
                navArgument("digits") { type = NavType.IntType },
                navArgument("rows") { type = NavType.IntType },
                navArgument("timeSeconds") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val modeStr = backStackEntry.arguments?.getString("mode") ?: "ADDITION"
            val digits = backStackEntry.arguments?.getInt("digits") ?: 1
            val rows = backStackEntry.arguments?.getInt("rows") ?: 5
            val timeSeconds = backStackEntry.arguments?.getInt("timeSeconds") ?: 1
            
            PracticeScreen(
                mode = try { PracticeMode.valueOf(modeStr) } catch (e: Exception) { PracticeMode.ADDITION },
                digits = digits,
                rows = rows,
                timeSeconds = timeSeconds,
                onStartAgain = {
                    navController.popBackStack(Screen.Config.createRoute(modeStr), false)
                },
                onBackClick = { 
                    navController.popBackStack(Screen.Dashboard.route, false)
                }
            )
        }
        
        // Table Practice Screen
        composable(
            route = Screen.TablePractice.route,
            arguments = listOf(
                navArgument("tableNumber") { type = NavType.IntType },
                navArgument("count") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val tableNumber = backStackEntry.arguments?.getInt("tableNumber") ?: 2
            val count = backStackEntry.arguments?.getInt("count") ?: 10
            
            TablePracticeScreen(
                tableNumber = tableNumber,
                questionCount = count,
                onBackClick = { 
                    navController.popBackStack(Screen.Dashboard.route, false)
                }
            )
        }
        
        // Stopwatch Screen - Opens directly
        composable(Screen.Stopwatch.route) {
            StopwatchScreen(
                onBackClick = { 
                    navController.popBackStack(Screen.Dashboard.route, false)
                }
            )
        }
        
        // Recall Numbers Screen
        composable(
            route = Screen.RecallNumbers.route,
            arguments = listOf(
                navArgument("digits") { type = NavType.IntType },
                navArgument("count") { type = NavType.IntType },
                navArgument("timeSeconds") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val digits = backStackEntry.arguments?.getInt("digits") ?: 1
            val count = backStackEntry.arguments?.getInt("count") ?: 5
            val timeSeconds = backStackEntry.arguments?.getInt("timeSeconds") ?: 2
            
            RecallNumbersScreen(
                digits = digits,
                numberCount = count,
                displayTimeSeconds = timeSeconds,
                onBackClick = { 
                    navController.popBackStack(Screen.Dashboard.route, false)
                }
            )
        }
        
        // Calculator Screen - Opens directly
        composable(Screen.Calculator.route) {
            CalculatorScreen(
                onBackClick = { 
                    navController.popBackStack(Screen.Dashboard.route, false)
                }
            )
        }
        
        // Multiply Practice Screen
        composable(
            route = Screen.Multiply.route,
            arguments = listOf(
                navArgument("digits") { type = NavType.IntType },
                navArgument("count") { type = NavType.IntType },
                navArgument("timeSeconds") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val digits = backStackEntry.arguments?.getInt("digits") ?: 1
            val count = backStackEntry.arguments?.getInt("count") ?: 5
            val timeSeconds = backStackEntry.arguments?.getInt("timeSeconds") ?: 2
            
            MultiplyPracticeScreen(
                digits = digits,
                problemCount = count,
                timeSeconds = timeSeconds,
                onBackClick = { 
                    navController.popBackStack(Screen.Dashboard.route, false)
                }
            )
        }
        
        // Settings Screen
        composable(Screen.Settings.route) {
            SettingsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
