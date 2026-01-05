package com.abacus.practice.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abacus.practice.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(
    onBackClick: () -> Unit
) {
    var display by remember { mutableStateOf("0") }
    var firstNumber by remember { mutableStateOf<Double?>(null) }
    var operation by remember { mutableStateOf<String?>(null) }
    var waitingForSecondNumber by remember { mutableStateOf(false) }
    
    fun onNumberClick(number: String) {
        if (waitingForSecondNumber) {
            display = number
            waitingForSecondNumber = false
        } else {
            display = if (display == "0") number else display + number
        }
    }
    
    fun onOperationClick(op: String) {
        firstNumber = display.toDoubleOrNull()
        operation = op
        waitingForSecondNumber = true
    }
    
    fun onEqualsClick() {
        val second = display.toDoubleOrNull() ?: return
        val first = firstNumber ?: return
        val op = operation ?: return
        
        val result = when (op) {
            "+" -> first + second
            "-" -> first - second
            "×" -> first * second
            "÷" -> if (second != 0.0) first / second else Double.NaN
            else -> return
        }
        
        display = if (result == result.toLong().toDouble()) {
            result.toLong().toString()
        } else {
            result.toString()
        }
        
        firstNumber = null
        operation = null
    }
    
    fun onClear() {
        display = "0"
        firstNumber = null
        operation = null
        waitingForSecondNumber = false
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Calculator",
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
                    containerColor = Digit2Color
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Display
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .shadow(4.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Surface)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        text = display,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnSurface,
                        textAlign = TextAlign.End
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Keypad
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Row 1: C, ±, %, ÷
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CalcButton("C", Modifier.weight(1f), WrongRed) { onClear() }
                    CalcButton("±", Modifier.weight(1f), OnSurfaceVariant) { display = (-display.toDouble()).toString() }
                    CalcButton("%", Modifier.weight(1f), OnSurfaceVariant) { display = (display.toDouble() / 100).toString() }
                    CalcButton("÷", Modifier.weight(1f), MixedColor) { onOperationClick("÷") }
                }
                
                // Row 2: 7, 8, 9, ×
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CalcButton("7", Modifier.weight(1f)) { onNumberClick("7") }
                    CalcButton("8", Modifier.weight(1f)) { onNumberClick("8") }
                    CalcButton("9", Modifier.weight(1f)) { onNumberClick("9") }
                    CalcButton("×", Modifier.weight(1f), MixedColor) { onOperationClick("×") }
                }
                
                // Row 3: 4, 5, 6, -
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CalcButton("4", Modifier.weight(1f)) { onNumberClick("4") }
                    CalcButton("5", Modifier.weight(1f)) { onNumberClick("5") }
                    CalcButton("6", Modifier.weight(1f)) { onNumberClick("6") }
                    CalcButton("-", Modifier.weight(1f), MixedColor) { onOperationClick("-") }
                }
                
                // Row 4: 1, 2, 3, +
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CalcButton("1", Modifier.weight(1f)) { onNumberClick("1") }
                    CalcButton("2", Modifier.weight(1f)) { onNumberClick("2") }
                    CalcButton("3", Modifier.weight(1f)) { onNumberClick("3") }
                    CalcButton("+", Modifier.weight(1f), MixedColor) { onOperationClick("+") }
                }
                
                // Row 5: 0, ., =
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CalcButton("0", Modifier.weight(2f)) { onNumberClick("0") }
                    CalcButton(".", Modifier.weight(1f)) { if (!display.contains(".")) display += "." }
                    CalcButton("=", Modifier.weight(1f), CorrectGreen) { onEqualsClick() }
                }
            }
        }
    }
}

@Composable
private fun CalcButton(
    text: String,
    modifier: Modifier = Modifier,
    textColor: Color = OnSurface,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(64.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }
    }
}
