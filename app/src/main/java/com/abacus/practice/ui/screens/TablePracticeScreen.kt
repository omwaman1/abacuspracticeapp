package com.abacus.practice.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abacus.practice.ui.theme.*
import kotlin.random.Random

enum class TablePracticeState {
    QUESTION,
    RESULT
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TablePracticeScreen(
    tableNumber: Int,
    questionCount: Int,
    onBackClick: () -> Unit
) {
    var currentQuestion by remember { mutableIntStateOf(0) }
    var multiplier by remember { mutableIntStateOf(Random.nextInt(1, 11)) }
    var userAnswer by remember { mutableStateOf("") }
    var correctCount by remember { mutableIntStateOf(0) }
    var wrongCount by remember { mutableIntStateOf(0) }
    var state by remember { mutableStateOf(TablePracticeState.QUESTION) }
    var lastAnswerCorrect by remember { mutableStateOf<Boolean?>(null) }
    
    val correctAnswer = tableNumber * multiplier
    
    fun checkAnswer() {
        val answer = userAnswer.toIntOrNull() ?: 0
        if (answer == correctAnswer) {
            correctCount++
            lastAnswerCorrect = true
        } else {
            wrongCount++
            lastAnswerCorrect = false
        }
        state = TablePracticeState.RESULT
    }
    
    fun nextQuestion() {
        if (currentQuestion < questionCount - 1) {
            currentQuestion++
            multiplier = Random.nextInt(1, 11)
            userAnswer = ""
            lastAnswerCorrect = null
            state = TablePracticeState.QUESTION
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Table of $tableNumber",
                        fontWeight = FontWeight.Bold,
                        color = OnPrimary
                    )
                },
                actions = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home",
                            tint = OnPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SpeedTestColor
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Progress
            Text(
                text = "Question ${currentQuestion + 1} of $questionCount",
                style = MaterialTheme.typography.titleMedium,
                color = OnSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            LinearProgressIndicator(
                progress = (currentQuestion + 1).toFloat() / questionCount,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = SpeedTestColor,
                trackColor = SpeedTestColor.copy(alpha = 0.2f)
            )
            
            // Score
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "✓ $correctCount",
                    color = CorrectGreen,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = 24.dp)
                )
                Text(
                    text = "✗ $wrongCount",
                    color = WrongRed,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Question card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "$tableNumber × $multiplier = ?",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnSurface
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    if (state == TablePracticeState.QUESTION) {
                        OutlinedTextField(
                            value = userAnswer,
                            onValueChange = { userAnswer = it.filter { c -> c.isDigit() } },
                            label = { Text("Your Answer") },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = { checkAnswer() }
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            textStyle = LocalTextStyle.current.copy(
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            ),
                            singleLine = true
                        )
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Button(
                            onClick = { checkAnswer() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SpeedTestColor
                            ),
                            enabled = userAnswer.isNotEmpty()
                        ) {
                            Text(
                                text = "Check Answer",
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    } else {
                        // Show result
                        Text(
                            text = if (lastAnswerCorrect == true) "✓ Correct!" else "✗ Wrong!",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (lastAnswerCorrect == true) CorrectGreen else WrongRed
                        )
                        
                        if (lastAnswerCorrect == false) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Correct answer: $correctAnswer",
                                style = MaterialTheme.typography.titleMedium,
                                color = CorrectGreen
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        if (currentQuestion < questionCount - 1) {
                            Button(
                                onClick = { nextQuestion() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = SpeedTestColor
                                )
                            ) {
                                Text(
                                    text = "Next Question →",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        } else {
                            // Final result
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "🎉 Practice Complete!",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                Button(
                                    onClick = onBackClick,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Primary
                                    )
                                ) {
                                    Text(
                                        text = "🏠 Go Home",
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
