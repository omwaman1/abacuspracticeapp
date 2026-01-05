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
import kotlinx.coroutines.delay
import kotlin.random.Random

enum class MultiplyState {
    SHOWING_PROBLEM,
    WAITING_ANSWER,
    RESULT
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultiplyPracticeScreen(
    digits: Int,
    problemCount: Int,
    timeSeconds: Int,
    onBackClick: () -> Unit
) {
    // Generate all multiplication problems
    val problems = remember {
        val min = if (digits == 1) 2 else 10
        val max = if (digits == 1) 9 else 99
        (1..problemCount).map {
            val a = Random.nextInt(min, max + 1)
            val b = Random.nextInt(2, 10) // Keep second number simple
            Triple(a, b, a * b)
        }
    }
    
    var currentProblemIndex by remember { mutableIntStateOf(0) }
    var state by remember { mutableStateOf(MultiplyState.SHOWING_PROBLEM) }
    var userAnswer by remember { mutableStateOf("") }
    var correctCount by remember { mutableIntStateOf(0) }
    var wrongCount by remember { mutableIntStateOf(0) }
    var lastAnswerCorrect by remember { mutableStateOf<Boolean?>(null) }
    var showCountdown by remember { mutableIntStateOf(timeSeconds) }
    
    val currentProblem = problems.getOrNull(currentProblemIndex)
    
    // Countdown timer for each problem
    LaunchedEffect(currentProblemIndex, state) {
        if (state == MultiplyState.SHOWING_PROBLEM && currentProblem != null) {
            showCountdown = timeSeconds
            while (showCountdown > 0) {
                delay(1000)
                showCountdown--
            }
            state = MultiplyState.WAITING_ANSWER
        }
    }
    
    fun checkAnswer() {
        val answer = userAnswer.toIntOrNull() ?: 0
        val correct = currentProblem?.third ?: 0
        if (answer == correct) {
            correctCount++
            lastAnswerCorrect = true
        } else {
            wrongCount++
            lastAnswerCorrect = false
        }
        state = MultiplyState.RESULT
    }
    
    fun nextProblem() {
        if (currentProblemIndex < problemCount - 1) {
            currentProblemIndex++
            userAnswer = ""
            lastAnswerCorrect = null
            state = MultiplyState.SHOWING_PROBLEM
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Multiply Practice",
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
                    containerColor = SubtractionColor
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
            // Progress and score
            Text(
                text = "Problem ${currentProblemIndex + 1} of $problemCount",
                style = MaterialTheme.typography.titleMedium,
                color = OnSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            LinearProgressIndicator(
                progress = (currentProblemIndex + 1).toFloat() / problemCount,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = SubtractionColor,
                trackColor = SubtractionColor.copy(alpha = 0.2f)
            )
            
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
            
            if (currentProblem != null) {
                when (state) {
                    MultiplyState.SHOWING_PROBLEM -> {
                        // Show problem with countdown
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
                                    text = "⏱️ $showCountdown",
                                    fontSize = 24.sp,
                                    color = TimerOrange,
                                    fontWeight = FontWeight.Bold
                                )
                                
                                Spacer(modifier = Modifier.height(24.dp))
                                
                                Text(
                                    text = "${currentProblem.first} × ${currentProblem.second}",
                                    fontSize = 48.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SubtractionColor
                                )
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                Text(
                                    text = "Calculate now!",
                                    color = OnSurfaceVariant
                                )
                            }
                        }
                    }
                    
                    MultiplyState.WAITING_ANSWER -> {
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
                                    text = "${currentProblem.first} × ${currentProblem.second} = ?",
                                    fontSize = 36.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = OnSurface
                                )
                                
                                Spacer(modifier = Modifier.height(24.dp))
                                
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
                                        containerColor = SubtractionColor
                                    ),
                                    enabled = userAnswer.isNotEmpty()
                                ) {
                                    Text(
                                        text = "Check Answer",
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                    
                    MultiplyState.RESULT -> {
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
                                    text = if (lastAnswerCorrect == true) "✓ Correct!" else "✗ Wrong!",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (lastAnswerCorrect == true) CorrectGreen else WrongRed
                                )
                                
                                if (lastAnswerCorrect == false) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = "${currentProblem.first} × ${currentProblem.second} = ${currentProblem.third}",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = CorrectGreen
                                    )
                                }
                                
                                Spacer(modifier = Modifier.height(24.dp))
                                
                                if (currentProblemIndex < problemCount - 1) {
                                    Button(
                                        onClick = { nextProblem() },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(56.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = SubtractionColor
                                        )
                                    ) {
                                        Text(
                                            text = "Next →",
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                } else {
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
}
