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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abacus.practice.domain.model.PracticeMode
import com.abacus.practice.ui.theme.*
import kotlinx.coroutines.delay
import kotlin.random.Random

enum class PracticeState {
    SHOWING_NUMBERS,
    WAITING_ANSWER,
    SHOWING_RESULT
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PracticeScreen(
    mode: PracticeMode,
    digits: Int,
    rows: Int,
    timeSeconds: Int,
    onStartAgain: () -> Unit,
    onBackClick: () -> Unit
) {
    // Generate numbers and operations
    val (numbers, operations, correctAnswer) = remember {
        generateProblem(mode, digits, rows)
    }
    
    var practiceState by remember { mutableStateOf(PracticeState.SHOWING_NUMBERS) }
    var currentNumberIndex by remember { mutableIntStateOf(-1) }
    var userAnswer by remember { mutableStateOf("") }
    var isCorrect by remember { mutableStateOf<Boolean?>(null) }
    
    val modeColor = when (mode) {
        PracticeMode.ADDITION -> AdditionColor
        PracticeMode.ADD_SUB -> MixedColor
        PracticeMode.MIX_ADD_SUB -> Level3
        PracticeMode.DIVISION -> Level4
        PracticeMode.SPEED_TEST -> Level5
        PracticeMode.FLASH_CARDS -> Digit1Color
        PracticeMode.LISTENING -> Level1
        else -> Primary
    }
    
    val gradientColors = when (mode) {
        PracticeMode.ADDITION -> GradientGreen
        PracticeMode.ADD_SUB -> GradientPurple
        PracticeMode.MIX_ADD_SUB -> listOf(Level3, Level3.copy(alpha = 0.7f))
        PracticeMode.DIVISION -> listOf(Level4, Level4.copy(alpha = 0.7f))
        PracticeMode.SPEED_TEST -> listOf(Level5, Level5.copy(alpha = 0.7f))
        PracticeMode.FLASH_CARDS -> listOf(Digit1Color, Digit1Color.copy(alpha = 0.7f))
        PracticeMode.LISTENING -> listOf(Level1, Level1.copy(alpha = 0.7f))
        else -> GradientBlue
    }
    
    val modeTitle = when (mode) {
        PracticeMode.ADDITION -> "Addition"
        PracticeMode.ADD_SUB -> "Add/Sub"
        PracticeMode.MIX_ADD_SUB -> "Mixed"
        PracticeMode.DIVISION -> "Division"
        PracticeMode.SPEED_TEST -> "Speed Test"
        PracticeMode.FLASH_CARDS -> "Flash Cards"
        PracticeMode.LISTENING -> "Listening"
        else -> "Practice"
    }
    
    // Timer effect to show numbers one by one
    LaunchedEffect(Unit) {
        delay(500)
        
        for (i in numbers.indices) {
            currentNumberIndex = i
            delay(timeSeconds * 1000L)
        }
        
        practiceState = PracticeState.WAITING_ANSWER
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = modeTitle,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnPrimary
                    )
                },
                actions = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home",
                            tint = OnPrimary,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = modeColor
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (practiceState) {
                PracticeState.SHOWING_NUMBERS -> {
                    NumberDisplay(
                        currentIndex = currentNumberIndex,
                        numbers = numbers,
                        operations = operations,
                        totalNumbers = rows,
                        modeColor = modeColor,
                        gradientColors = gradientColors
                    )
                }
                
                PracticeState.WAITING_ANSWER -> {
                    AnswerInput(
                        userAnswer = userAnswer,
                        onAnswerChange = { userAnswer = it },
                        onCheckAnswer = {
                            val answer = userAnswer.toIntOrNull() ?: 0
                            isCorrect = (answer == correctAnswer)
                            practiceState = PracticeState.SHOWING_RESULT
                        },
                        onStartAgain = onStartAgain,
                        modeColor = modeColor
                    )
                }
                
                PracticeState.SHOWING_RESULT -> {
                    ResultDisplay(
                        isCorrect = isCorrect ?: false,
                        correctAnswer = correctAnswer,
                        userAnswer = userAnswer.toIntOrNull() ?: 0,
                        onStartAgain = onStartAgain,
                        modeColor = modeColor
                    )
                }
            }
        }
    }
}

@Composable
private fun NumberDisplay(
    currentIndex: Int,
    numbers: List<Int>,
    operations: List<Char>,
    totalNumbers: Int,
    modeColor: Color,
    gradientColors: List<Color>
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(20.dp)
    ) {
        Text(
            text = "${(currentIndex + 1).coerceAtLeast(0)} / $totalNumbers",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = modeColor
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        LinearProgressIndicator(
            progress = ((currentIndex + 1).toFloat() / totalNumbers).coerceIn(0f, 1f),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(10.dp)
                .clip(RoundedCornerShape(5.dp)),
            color = modeColor,
            trackColor = modeColor.copy(alpha = 0.2f)
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        if (currentIndex >= 0 && currentIndex < numbers.size) {
            Card(
                modifier = Modifier
                    .size(220.dp)
                    .shadow(16.dp, RoundedCornerShape(28.dp)),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Brush.verticalGradient(gradientColors)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (currentIndex > 0) {
                            Text(
                                text = operations[currentIndex - 1].toString(),
                                fontSize = 44.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        
                        Text(
                            text = numbers[currentIndex].toString(),
                            fontSize = 64.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        } else {
            Card(
                modifier = Modifier
                    .size(220.dp)
                    .shadow(16.dp, RoundedCornerShape(28.dp)),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Brush.verticalGradient(gradientColors)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Get Ready!",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = "👀 Watch carefully...",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = OnSurfaceVariant
        )
    }
}

@Composable
private fun AnswerInput(
    userAnswer: String,
    onAnswerChange: (String) -> Unit,
    onCheckAnswer: () -> Unit,
    onStartAgain: () -> Unit,
    modeColor: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(20.dp)
    ) {
        Text(
            text = "🤔",
            fontSize = 72.sp
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "What's your answer?",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = OnBackground
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        OutlinedTextField(
            value = userAnswer,
            onValueChange = { newValue ->
                if (newValue.isEmpty() || newValue.matches(Regex("^-?\\d*$"))) {
                    onAnswerChange(newValue)
                }
            },
            label = { Text("Enter Answer", fontSize = 16.sp) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { onCheckAnswer() }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(80.dp),
            shape = RoundedCornerShape(18.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = modeColor,
                unfocusedBorderColor = modeColor.copy(alpha = 0.5f)
            ),
            textStyle = LocalTextStyle.current.copy(
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            ),
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onStartAgain,
                modifier = Modifier
                    .weight(1f)
                    .height(58.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = modeColor
                )
            ) {
                Text(
                    text = "🔄 Again",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Button(
                onClick = onCheckAnswer,
                modifier = Modifier
                    .weight(1f)
                    .height(58.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = modeColor
                ),
                enabled = userAnswer.isNotEmpty()
            ) {
                Text(
                    text = "✓ Check",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun ResultDisplay(
    isCorrect: Boolean,
    correctAnswer: Int,
    userAnswer: Int,
    onStartAgain: () -> Unit,
    modeColor: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(20.dp)
    ) {
        Text(
            text = if (isCorrect) "🎉" else "😔",
            fontSize = 88.sp
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = if (isCorrect) "Correct!" else "Wrong!",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = if (isCorrect) CorrectGreen else WrongRed
        )
        
        Spacer(modifier = Modifier.height(28.dp))
        
        if (!isCorrect) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .shadow(6.dp, RoundedCornerShape(18.dp)),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Surface)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Your answer: $userAnswer",
                        fontSize = 18.sp,
                        color = WrongRed,
                        fontWeight = FontWeight.Medium
                    )
                    
                    Spacer(modifier = Modifier.height(10.dp))
                    
                    Text(
                        text = "Correct answer:",
                        fontSize = 16.sp,
                        color = OnSurfaceVariant
                    )
                    
                    Text(
                        text = "$correctAnswer",
                        fontSize = 44.sp,
                        fontWeight = FontWeight.Bold,
                        color = CorrectGreen
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(40.dp))
        
        Button(
            onClick = onStartAgain,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(60.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = modeColor
            )
        ) {
            Text(
                text = "🔄 Start Again",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

private fun generateProblem(
    mode: PracticeMode,
    digits: Int,
    rows: Int
): Triple<List<Int>, List<Char>, Int> {
    val numbers = mutableListOf<Int>()
    val operations = mutableListOf<Char>()
    
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
    
    numbers.add(Random.nextInt(minNumber, maxNumber + 1))
    
    var runningTotal = numbers[0]
    
    for (i in 1 until rows) {
        val operation = when (mode) {
            PracticeMode.ADDITION -> '+'
            PracticeMode.ADD_SUB, PracticeMode.MIX_ADD_SUB -> if (Random.nextBoolean()) '+' else '-'
            PracticeMode.DIVISION -> '÷'
            else -> '+'
        }
        
        val number = Random.nextInt(minNumber, maxNumber + 1)
        
        operations.add(operation)
        numbers.add(number)
        
        runningTotal = when (operation) {
            '+' -> runningTotal + number
            '-' -> runningTotal - number
            else -> runningTotal + number
        }
    }
    
    return Triple(numbers, operations, runningTotal)
}
