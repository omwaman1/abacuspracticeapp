package com.abacus.practice.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abacus.practice.domain.model.PracticeMode
import com.abacus.practice.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onModeSelected: (PracticeMode) -> Unit,
    onSettingsClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Practice Sums",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnPrimary
                    )
                },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = OnPrimary,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Primary
                )
            )
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(paddingValues)
        ) {
            // Addition
            item {
                PracticeModeCard(
                    title = "Addition",
                    icon = "➕",
                    gradientColors = GradientGreen,
                    onClick = { onModeSelected(PracticeMode.ADDITION) }
                )
            }
            
            // Add/Sub
            item {
                PracticeModeCard(
                    title = "Add/Sub",
                    icon = "➕➖",
                    gradientColors = GradientPurple,
                    onClick = { onModeSelected(PracticeMode.ADD_SUB) }
                )
            }
            
            // Table Practice
            item {
                PracticeModeCard(
                    title = "Tables",
                    icon = "✖️",
                    gradientColors = GradientBlue,
                    onClick = { onModeSelected(PracticeMode.TABLE_PRACTICE) }
                )
            }
            
            // Stopwatch
            item {
                PracticeModeCard(
                    title = "Stopwatch",
                    icon = "⏱️",
                    gradientColors = GradientOrange,
                    onClick = { onModeSelected(PracticeMode.STOPWATCH) }
                )
            }
            
            // Recall Numbers
            item {
                PracticeModeCard(
                    title = "Recall",
                    icon = "🧠",
                    gradientColors = GradientPink,
                    onClick = { onModeSelected(PracticeMode.RECALL_NUMBERS) }
                )
            }
            
            // Calculator - Opens directly
            item {
                PracticeModeCard(
                    title = "Calculator",
                    icon = "🔢",
                    gradientColors = listOf(Digit2Color, Digit2Color.copy(alpha = 0.7f)),
                    onClick = { onModeSelected(PracticeMode.CALCULATOR) }
                )
            }
            
            // Mix Add/Sub
            item {
                PracticeModeCard(
                    title = "Mix +/-",
                    icon = "🔀",
                    gradientColors = listOf(Level3, Level3.copy(alpha = 0.7f)),
                    onClick = { onModeSelected(PracticeMode.MIX_ADD_SUB) }
                )
            }
            
            // Multiply
            item {
                PracticeModeCard(
                    title = "Multiply",
                    icon = "✖️",
                    gradientColors = GradientRed,
                    onClick = { onModeSelected(PracticeMode.MULTIPLY) }
                )
            }
            
            // Division - New
            item {
                PracticeModeCard(
                    title = "Division",
                    icon = "➗",
                    gradientColors = listOf(Level4, Level4.copy(alpha = 0.7f)),
                    onClick = { onModeSelected(PracticeMode.DIVISION) }
                )
            }
            
            // Speed Test - New
            item {
                PracticeModeCard(
                    title = "Speed Test",
                    icon = "🚀",
                    gradientColors = listOf(Level5, Level5.copy(alpha = 0.7f)),
                    onClick = { onModeSelected(PracticeMode.SPEED_TEST) }
                )
            }
            
            // Flash Cards - New
            item {
                PracticeModeCard(
                    title = "Flash Cards",
                    icon = "🃏",
                    gradientColors = listOf(Digit1Color, Digit1Color.copy(alpha = 0.7f)),
                    onClick = { onModeSelected(PracticeMode.FLASH_CARDS) }
                )
            }
            
            // Listening - New
            item {
                PracticeModeCard(
                    title = "Listening",
                    icon = "👂",
                    gradientColors = listOf(Level1, Level1.copy(alpha = 0.7f)),
                    onClick = { onModeSelected(PracticeMode.LISTENING) }
                )
            }
        }
    }
}

@Composable
fun PracticeModeCard(
    title: String,
    icon: String,
    gradientColors: List<Color>,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .shadow(8.dp, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(gradientColors)
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Icon
                Text(
                    text = icon,
                    fontSize = 36.sp
                )
                
                Spacer(modifier = Modifier.height(6.dp))
                
                // Title
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
        }
    }
}
