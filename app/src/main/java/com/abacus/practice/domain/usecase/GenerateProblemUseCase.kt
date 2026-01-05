package com.abacus.practice.domain.usecase

import com.abacus.practice.domain.model.PracticeMode
import com.abacus.practice.domain.model.PracticeProblem
import kotlin.random.Random

/**
 * Use case for generating practice problems based on configuration
 */
class GenerateProblemUseCase {
    
    /**
     * Generate a single problem based on mode, digits, and rows
     */
    fun generate(mode: PracticeMode, digits: Int, rows: Int): PracticeProblem {
        val numbers = mutableListOf<Int>()
        val operations = mutableListOf<Char>()
        
        // Determine number range based on digits
        val minNumber = when (digits) {
            1 -> 1
            2 -> 10
            3 -> 100
            4 -> 1000
            else -> 1
        }
        
        val maxNumber = when (digits) {
            1 -> 9
            2 -> 99
            3 -> 999
            4 -> 9999
            else -> 9
        }
        
        // Generate first number
        numbers.add(Random.nextInt(minNumber, maxNumber + 1))
        
        // Generate remaining rows with operations
        var runningTotal = numbers[0]
        
        for (i in 1 until rows) {
            val operation = when (mode) {
                PracticeMode.ADDITION -> '+'
                PracticeMode.ADD_SUB, PracticeMode.MIX_ADD_SUB -> if (Random.nextBoolean()) '+' else '-'
                else -> '+'
            }
            
            val number = Random.nextInt(minNumber, maxNumber + 1)
            
            operations.add(operation)
            numbers.add(number)
            
            runningTotal = if (operation == '+') runningTotal + number else runningTotal - number
        }
        
        // Calculate correct answer
        var answer = numbers[0]
        for (i in 0 until operations.size) {
            answer = if (operations[i] == '+') answer + numbers[i + 1] else answer - numbers[i + 1]
        }
        
        return PracticeProblem(
            numbers = numbers,
            operations = operations,
            correctAnswer = answer
        )
    }
}
