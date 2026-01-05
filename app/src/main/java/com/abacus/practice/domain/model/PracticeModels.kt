package com.abacus.practice.domain.model

/**
 * Enum representing different practice modes
 */
enum class PracticeMode {
    ADDITION,
    ADD_SUB,
    TABLE_PRACTICE,
    STOPWATCH,
    RECALL_NUMBERS,
    CALCULATOR,
    MIX_ADD_SUB,
    MULTIPLY,
    DIVISION,
    SPEED_TEST,
    FLASH_CARDS,
    LISTENING
}

/**
 * Configuration for a practice session
 */
data class PracticeConfig(
    val mode: PracticeMode = PracticeMode.ADDITION,
    val digits: Int = 1, // 1-4
    val rows: Int = 5,   // 3-20
    val timeSeconds: Int = 1, // seconds per number
    val tableNumber: Int = 2  // for table practice
)

/**
 * Represents a single practice problem
 */
data class PracticeProblem(
    val numbers: List<Int>,
    val operations: List<Char>, // '+', '-', '×', '÷'
    val correctAnswer: Int
)

/**
 * Result of a practice session
 */
data class PracticeResult(
    val config: PracticeConfig,
    val correct: Int,
    val wrong: Int,
    val totalTimeTakenMs: Long
)
