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

enum class RecallState {
    SHOWING_NUMBERS,
    ENTER_ANSWER,
    RESULT
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecallNumbersScreen(
    digits: Int,
    numberCount: Int,
    displayTimeSeconds: Int,
    onBackClick: () -> Unit
) {
    // Generate numbers to remember
    val numbers = remember {
        val min = when (digits) { 1 -> 1; 2 -> 10; 3 -> 100; else -> 1000 }
        val max = when (digits) { 1 -> 9; 2 -> 99; 3 -> 999; else -> 9999 }
        (1..numberCount).map { Random.nextInt(min, max + 1) }
    }
    
    var state by remember { mutableStateOf(RecallState.SHOWING_NUMBERS) }
    var currentNumberIndex by remember { mutableIntStateOf(-1) }
    var userAnswers by remember { mutableStateOf<List<String>>(List(numberCount) { "" }) }
    var currentAnswerIndex by remember { mutableIntStateOf(0) }
    var currentInput by remember { mutableStateOf("") }
    
    // Show numbers one by one
    LaunchedEffect(Unit) {
        delay(500)
        for (i in numbers.indices) {
            currentNumberIndex = i
            delay(displayTimeSeconds * 1000L)
        }
        state = RecallState.ENTER_ANSWER
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Recall Numbers",
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
                    containerColor = Digit1Color
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
            when (state) {
                RecallState.SHOWING_NUMBERS -> {
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    Text(
                        text = "Remember these numbers!",
                        style = MaterialTheme.typography.titleMedium,
                        color = OnSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    LinearProgressIndicator(
                        progress = ((currentNumberIndex + 1).toFloat() / numberCount).coerceIn(0f, 1f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp),
                        color = Digit1Color,
                        trackColor = Digit1Color.copy(alpha = 0.2f)
                    )
                    
                    Spacer(modifier = Modifier.height(64.dp))
                    
                    if (currentNumberIndex >= 0 && currentNumberIndex < numbers.size) {
                        Card(
                            modifier = Modifier
                                .size(200.dp)
                                .shadow(16.dp, RoundedCornerShape(24.dp)),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = Surface)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = numbers[currentNumberIndex].toString(),
                                    fontSize = 48.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Digit1Color
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = "Number ${currentNumberIndex + 1} of $numberCount",
                            color = OnSurfaceVariant
                        )
                    } else {
                        Card(
                            modifier = Modifier
                                .size(200.dp)
                                .shadow(16.dp, RoundedCornerShape(24.dp)),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = Surface)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Get Ready!",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Digit1Color
                                )
                            }
                        }
                    }
                }
                
                RecallState.ENTER_ANSWER -> {
                    Text(
                        text = "🧠",
                        fontSize = 64.sp
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Enter number ${currentAnswerIndex + 1} of $numberCount",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = OnBackground
                    )
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    OutlinedTextField(
                        value = currentInput,
                        onValueChange = { currentInput = it.filter { c -> c.isDigit() } },
                        label = { Text("Your Answer") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                val newAnswers = userAnswers.toMutableList()
                                newAnswers[currentAnswerIndex] = currentInput
                                userAnswers = newAnswers
                                
                                if (currentAnswerIndex < numberCount - 1) {
                                    currentAnswerIndex++
                                    currentInput = ""
                                } else {
                                    state = RecallState.RESULT
                                }
                            }
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        ),
                        singleLine = true
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Button(
                        onClick = {
                            val newAnswers = userAnswers.toMutableList()
                            newAnswers[currentAnswerIndex] = currentInput
                            userAnswers = newAnswers
                            
                            if (currentAnswerIndex < numberCount - 1) {
                                currentAnswerIndex++
                                currentInput = ""
                            } else {
                                state = RecallState.RESULT
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Digit1Color
                        ),
                        enabled = currentInput.isNotEmpty()
                    ) {
                        Text(
                            text = if (currentAnswerIndex < numberCount - 1) "Next →" else "Check Results",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
                
                RecallState.RESULT -> {
                    val correctCount = numbers.zip(userAnswers).count { (num, ans) ->
                        num.toString() == ans
                    }
                    
                    Text(
                        text = if (correctCount == numberCount) "🎉" else if (correctCount > numberCount / 2) "👍" else "📚",
                        fontSize = 64.sp
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "$correctCount / $numberCount Correct!",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (correctCount == numberCount) CorrectGreen else Primary
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Show comparison
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(4.dp, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Surface)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            numbers.forEachIndexed { index, number ->
                                val userAns = userAnswers.getOrNull(index) ?: ""
                                val isCorrect = number.toString() == userAns
                                
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Correct: $number",
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Your: $userAns ${if (isCorrect) "✓" else "✗"}",
                                        color = if (isCorrect) CorrectGreen else WrongRed,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.weight(1f))
                    
                    Button(
                        onClick = onBackClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
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
                    
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}
