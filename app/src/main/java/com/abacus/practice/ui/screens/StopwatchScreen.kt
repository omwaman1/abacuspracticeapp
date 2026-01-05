package com.abacus.practice.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abacus.practice.ui.theme.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StopwatchScreen(
    onBackClick: () -> Unit
) {
    var elapsedMs by remember { mutableLongStateOf(0L) }
    var isRunning by remember { mutableStateOf(false) }
    var lapTimes by remember { mutableStateOf<List<Long>>(emptyList()) }
    
    // Timer effect
    LaunchedEffect(isRunning) {
        while (isRunning) {
            delay(10)
            elapsedMs += 10
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Stopwatch",
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
                    containerColor = TimerOrange
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
            Spacer(modifier = Modifier.height(48.dp))
            
            // Timer display
            Box(
                modifier = Modifier
                    .size(250.dp)
                    .clip(CircleShape)
                    .background(TimerOrange.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(220.dp)
                        .clip(CircleShape)
                        .background(Surface),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = formatStopwatchTime(elapsedMs),
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold,
                            color = TimerOrange
                        )
                        
                        Text(
                            text = String.format("%02d", (elapsedMs % 1000) / 10),
                            fontSize = 24.sp,
                            color = OnSurfaceVariant
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // Control buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Reset button
                OutlinedButton(
                    onClick = {
                        isRunning = false
                        elapsedMs = 0
                        lapTimes = emptyList()
                    },
                    modifier = Modifier
                        .size(80.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = WrongRed
                    )
                ) {
                    Text("Reset", fontSize = 12.sp)
                }
                
                // Start/Stop button
                Button(
                    onClick = { isRunning = !isRunning },
                    modifier = Modifier
                        .size(100.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isRunning) WrongRed else CorrectGreen
                    )
                ) {
                    Text(
                        text = if (isRunning) "Stop" else "Start",
                        fontWeight = FontWeight.Bold
                    )
                }
                
                // Lap button
                OutlinedButton(
                    onClick = {
                        if (isRunning) {
                            lapTimes = lapTimes + elapsedMs
                        }
                    },
                    modifier = Modifier
                        .size(80.dp),
                    shape = CircleShape,
                    enabled = isRunning
                ) {
                    Text("Lap", fontSize = 12.sp)
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Lap times
            if (lapTimes.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Surface)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Lap Times",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        lapTimes.forEachIndexed { index, time ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Lap ${index + 1}")
                                Text(
                                    text = formatStopwatchTime(time),
                                    fontWeight = FontWeight.Bold,
                                    color = TimerOrange
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun formatStopwatchTime(ms: Long): String {
    val seconds = (ms / 1000) % 60
    val minutes = (ms / 1000) / 60
    return "%02d:%02d".format(minutes, seconds)
}
