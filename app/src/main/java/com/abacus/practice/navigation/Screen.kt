package com.abacus.practice.navigation

/**
 * Navigation routes for the app
 */
sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    
    object Config : Screen("config/{mode}") {
        fun createRoute(mode: String) = "config/$mode"
    }
    
    // Addition, Add/Sub, Mix Add/Sub practice
    object Practice : Screen("practice/{mode}/{digits}/{rows}/{timeSeconds}") {
        fun createRoute(mode: String, digits: Int, rows: Int, timeSeconds: Int) = 
            "practice/$mode/$digits/$rows/$timeSeconds"
    }
    
    // Table Practice
    object TablePractice : Screen("table_practice/{tableNumber}/{count}") {
        fun createRoute(tableNumber: Int, count: Int) = "table_practice/$tableNumber/$count"
    }
    
    // Stopwatch
    object Stopwatch : Screen("stopwatch")
    
    // Recall Numbers
    object RecallNumbers : Screen("recall/{digits}/{count}/{timeSeconds}") {
        fun createRoute(digits: Int, count: Int, timeSeconds: Int) = 
            "recall/$digits/$count/$timeSeconds"
    }
    
    // Calculator
    object Calculator : Screen("calculator")
    
    // Multiply Practice
    object Multiply : Screen("multiply/{digits}/{count}/{timeSeconds}") {
        fun createRoute(digits: Int, count: Int, timeSeconds: Int) = 
            "multiply/$digits/$count/$timeSeconds"
    }
    
    object Settings : Screen("settings")
    
    object PrivacyPolicy : Screen("privacy_policy")
}
