package com.abacus.practice.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abacus.practice.domain.model.PracticeMode
import com.abacus.practice.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigScreen(
    mode: PracticeMode,
    onStartPractice: (digits: Int, rows: Int, timeSeconds: Int) -> Unit,
    onStartTablePractice: (tableNumber: Int, count: Int) -> Unit,
    onStartStopwatch: () -> Unit,
    onStartRecall: (digits: Int, count: Int, timeSeconds: Int) -> Unit,
    onStartCalculator: () -> Unit,
    onStartMultiply: (digits: Int, rows: Int, timeSeconds: Int) -> Unit,
    onBackClick: () -> Unit
) {
    val modeTitle = when (mode) {
        PracticeMode.ADDITION -> "Addition"
        PracticeMode.ADD_SUB -> "Add/Sub"
        PracticeMode.TABLE_PRACTICE -> "Table Practice"
        PracticeMode.STOPWATCH -> "Stopwatch"
        PracticeMode.RECALL_NUMBERS -> "Recall Numbers"
        PracticeMode.CALCULATOR -> "Calculator"
        PracticeMode.MIX_ADD_SUB -> "Mix Add/Sub"
        PracticeMode.MULTIPLY -> "Multiply"
        PracticeMode.DIVISION -> "Division"
        PracticeMode.SPEED_TEST -> "Speed Test"
        PracticeMode.FLASH_CARDS -> "Flash Cards"
        PracticeMode.LISTENING -> "Listening"
    }
    
    val modeColor = when (mode) {
        PracticeMode.ADDITION -> AdditionColor
        PracticeMode.ADD_SUB -> MixedColor
        PracticeMode.TABLE_PRACTICE -> Level2
        PracticeMode.STOPWATCH -> TimerOrange
        PracticeMode.RECALL_NUMBERS -> Digit3Color
        PracticeMode.CALCULATOR -> Digit2Color
        PracticeMode.MIX_ADD_SUB -> Level3
        PracticeMode.MULTIPLY -> SubtractionColor
        PracticeMode.DIVISION -> Level4
        PracticeMode.SPEED_TEST -> Level5
        PracticeMode.FLASH_CARDS -> Digit1Color
        PracticeMode.LISTENING -> Level1
    }
    
    val modeIcon = when (mode) {
        PracticeMode.ADDITION -> "➕"
        PracticeMode.ADD_SUB -> "➕➖"
        PracticeMode.TABLE_PRACTICE -> "✖️"
        PracticeMode.STOPWATCH -> "⏱️"
        PracticeMode.RECALL_NUMBERS -> "🧠"
        PracticeMode.CALCULATOR -> "🔢"
        PracticeMode.MIX_ADD_SUB -> "🔀"
        PracticeMode.MULTIPLY -> "✖️"
        PracticeMode.DIVISION -> "➗"
        PracticeMode.SPEED_TEST -> "🚀"
        PracticeMode.FLASH_CARDS -> "🃏"
        PracticeMode.LISTENING -> "👂"
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = modeTitle,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            // Mode icon
            Text(text = modeIcon, fontSize = 72.sp)
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Configure $modeTitle",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = OnBackground
            )
            
            Spacer(modifier = Modifier.height(28.dp))
            
            // Different config based on mode
            when (mode) {
                PracticeMode.ADDITION, PracticeMode.ADD_SUB, PracticeMode.MIX_ADD_SUB,
                PracticeMode.DIVISION, PracticeMode.SPEED_TEST, PracticeMode.FLASH_CARDS,
                PracticeMode.LISTENING -> {
                    ArithmeticConfigContent(
                        modeColor = modeColor,
                        onStart = onStartPractice
                    )
                }
                
                PracticeMode.TABLE_PRACTICE -> {
                    TablePracticeConfigContent(
                        modeColor = modeColor,
                        onStart = onStartTablePractice
                    )
                }
                
                PracticeMode.STOPWATCH -> {
                    StopwatchInfoContent(
                        modeColor = modeColor,
                        onStart = onStartStopwatch
                    )
                }
                
                PracticeMode.RECALL_NUMBERS -> {
                    RecallConfigContent(
                        modeColor = modeColor,
                        onStart = onStartRecall
                    )
                }
                
                PracticeMode.CALCULATOR -> {
                    CalculatorInfoContent(
                        modeColor = modeColor,
                        onStart = onStartCalculator
                    )
                }
                
                PracticeMode.MULTIPLY -> {
                    MultiplyConfigContent(
                        modeColor = modeColor,
                        onStart = onStartMultiply
                    )
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun ArithmeticConfigContent(
    modeColor: Color,
    onStart: (digits: Int, rows: Int, timeSeconds: Int) -> Unit
) {
    var digits by remember { mutableStateOf("1") }
    var rows by remember { mutableStateOf("5") }
    var timeSeconds by remember { mutableStateOf("1") }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(10.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            ConfigInputField(
                label = "Number of Digits",
                value = digits,
                onValueChange = { digits = it.filter { c -> c.isDigit() }.take(1) },
                placeholder = "1-4",
                icon = "🔢"
            )
            
            ConfigInputField(
                label = "Number of Rows",
                value = rows,
                onValueChange = { rows = it.filter { c -> c.isDigit() }.take(2) },
                placeholder = "3-20",
                icon = "📊"
            )
            
            ConfigInputField(
                label = "Time per Number (seconds)",
                value = timeSeconds,
                onValueChange = { timeSeconds = it.filter { c -> c.isDigit() }.take(2) },
                placeholder = "1-10",
                icon = "⏱️"
            )
        }
    }
    
    Spacer(modifier = Modifier.height(28.dp))
    
    StartButton(modeColor = modeColor) {
        val d = digits.toIntOrNull()?.coerceIn(1, 4) ?: 1
        val r = rows.toIntOrNull()?.coerceIn(3, 20) ?: 5
        val t = timeSeconds.toIntOrNull()?.coerceIn(1, 10) ?: 1
        onStart(d, r, t)
    }
}

@Composable
private fun TablePracticeConfigContent(
    modeColor: Color,
    onStart: (tableNumber: Int, count: Int) -> Unit
) {
    var tableNumber by remember { mutableStateOf("2") }
    var questionCount by remember { mutableStateOf("10") }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(10.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            ConfigInputField(
                label = "Table Number (2-20)",
                value = tableNumber,
                onValueChange = { tableNumber = it.filter { c -> c.isDigit() }.take(2) },
                placeholder = "2-20",
                icon = "✖️"
            )
            
            ConfigInputField(
                label = "Number of Questions",
                value = questionCount,
                onValueChange = { questionCount = it.filter { c -> c.isDigit() }.take(2) },
                placeholder = "5-20",
                icon = "❓"
            )
        }
    }
    
    Spacer(modifier = Modifier.height(28.dp))
    
    StartButton(modeColor = modeColor) {
        val t = tableNumber.toIntOrNull()?.coerceIn(2, 20) ?: 2
        val c = questionCount.toIntOrNull()?.coerceIn(5, 20) ?: 10
        onStart(t, c)
    }
}

@Composable
private fun StopwatchInfoContent(
    modeColor: Color,
    onStart: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(10.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Column(
            modifier = Modifier.padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "⏱️",
                fontSize = 56.sp
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Stopwatch Timer",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = OnSurface
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Use as a timer for your abacus practice. Track how long you take to solve problems.",
                fontSize = 15.sp,
                color = OnSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
    
    Spacer(modifier = Modifier.height(28.dp))
    
    StartButton(modeColor = modeColor, onClick = onStart)
}

@Composable
private fun RecallConfigContent(
    modeColor: Color,
    onStart: (digits: Int, count: Int, timeSeconds: Int) -> Unit
) {
    var digits by remember { mutableStateOf("1") }
    var numberCount by remember { mutableStateOf("5") }
    var timeSeconds by remember { mutableStateOf("2") }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(10.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            ConfigInputField(
                label = "Digits per Number",
                value = digits,
                onValueChange = { digits = it.filter { c -> c.isDigit() }.take(1) },
                placeholder = "1-4",
                icon = "🔢"
            )
            
            ConfigInputField(
                label = "Numbers to Remember",
                value = numberCount,
                onValueChange = { numberCount = it.filter { c -> c.isDigit() }.take(2) },
                placeholder = "3-10",
                icon = "🧠"
            )
            
            ConfigInputField(
                label = "Display Time (seconds)",
                value = timeSeconds,
                onValueChange = { timeSeconds = it.filter { c -> c.isDigit() }.take(2) },
                placeholder = "1-5",
                icon = "⏱️"
            )
        }
    }
    
    Spacer(modifier = Modifier.height(28.dp))
    
    StartButton(modeColor = modeColor) {
        val d = digits.toIntOrNull()?.coerceIn(1, 4) ?: 1
        val c = numberCount.toIntOrNull()?.coerceIn(3, 10) ?: 5
        val t = timeSeconds.toIntOrNull()?.coerceIn(1, 5) ?: 2
        onStart(d, c, t)
    }
}

@Composable
private fun CalculatorInfoContent(
    modeColor: Color,
    onStart: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(10.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Column(
            modifier = Modifier.padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🔢",
                fontSize = 56.sp
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Built-in Calculator",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = OnSurface
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Use this calculator to verify your abacus calculations.",
                fontSize = 15.sp,
                color = OnSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
    
    Spacer(modifier = Modifier.height(28.dp))
    
    StartButton(modeColor = modeColor, text = "🔢 Open Calculator", onClick = onStart)
}

@Composable
private fun MultiplyConfigContent(
    modeColor: Color,
    onStart: (digits: Int, rows: Int, timeSeconds: Int) -> Unit
) {
    var digits by remember { mutableStateOf("1") }
    var rows by remember { mutableStateOf("5") }
    var timeSeconds by remember { mutableStateOf("2") }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(10.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            ConfigInputField(
                label = "Number of Digits",
                value = digits,
                onValueChange = { digits = it.filter { c -> c.isDigit() }.take(1) },
                placeholder = "1-2",
                icon = "🔢"
            )
            
            ConfigInputField(
                label = "Number of Problems",
                value = rows,
                onValueChange = { rows = it.filter { c -> c.isDigit() }.take(2) },
                placeholder = "5-20",
                icon = "📊"
            )
            
            ConfigInputField(
                label = "Time per Problem (seconds)",
                value = timeSeconds,
                onValueChange = { timeSeconds = it.filter { c -> c.isDigit() }.take(2) },
                placeholder = "1-10",
                icon = "⏱️"
            )
        }
    }
    
    Spacer(modifier = Modifier.height(28.dp))
    
    StartButton(modeColor = modeColor) {
        val d = digits.toIntOrNull()?.coerceIn(1, 2) ?: 1
        val r = rows.toIntOrNull()?.coerceIn(5, 20) ?: 5
        val t = timeSeconds.toIntOrNull()?.coerceIn(1, 10) ?: 2
        onStart(d, r, t)
    }
}

@Composable
private fun ConfigInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: String
) {
    Column {
        Text(
            text = label,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = OnSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            placeholder = { Text(placeholder, fontSize = 18.sp) },
            leadingIcon = {
                Text(
                    text = icon,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Primary,
                unfocusedBorderColor = Primary.copy(alpha = 0.3f)
            ),
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
private fun StartButton(
    modeColor: Color,
    text: String = "🚀 Start Practice",
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = modeColor
        )
    ) {
        Text(
            text = text,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}
