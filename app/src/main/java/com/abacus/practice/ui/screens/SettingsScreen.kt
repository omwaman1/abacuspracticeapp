package com.abacus.practice.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abacus.practice.ui.theme.*

// Shared Preferences keys
private const val PREFS_NAME = "abacus_settings"
private const val KEY_SOUND_ENABLED = "sound_enabled"
private const val KEY_VIBRATION_ENABLED = "vibration_enabled"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE) }
    
    var soundEnabled by remember { mutableStateOf(prefs.getBoolean(KEY_SOUND_ENABLED, true)) }
    var vibrationEnabled by remember { mutableStateOf(prefs.getBoolean(KEY_VIBRATION_ENABLED, true)) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
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
                    containerColor = Primary
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
            // Preferences section
            Text(
                text = "Preferences",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Primary,
                modifier = Modifier.padding(vertical = 12.dp)
            )
            
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(6.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Surface)
            ) {
                Column {
                    // Sound toggle
                    SettingsToggleItem(
                        title = "Sound Effects",
                        subtitle = "Play sounds for correct/wrong answers",
                        icon = "🔊",
                        checked = soundEnabled,
                        onCheckedChange = { 
                            soundEnabled = it
                            prefs.edit().putBoolean(KEY_SOUND_ENABLED, it).apply()
                        }
                    )
                    
                    Divider(color = Background)
                    
                    // Vibration toggle
                    SettingsToggleItem(
                        title = "Vibration",
                        subtitle = "Vibrate on answer submission",
                        icon = "📳",
                        checked = vibrationEnabled,
                        onCheckedChange = { 
                            vibrationEnabled = it
                            prefs.edit().putBoolean(KEY_VIBRATION_ENABLED, it).apply()
                            // Test vibration when enabled
                            if (it) {
                                vibrateDevice(context, 100)
                            }
                        }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // About section
            Text(
                text = "About",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Primary,
                modifier = Modifier.padding(vertical = 12.dp)
            )
            
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(6.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Surface)
            ) {
                Column {
                    SettingsLinkItem(
                        title = "Rate App",
                        subtitle = "Love the app? Rate us on Play Store!",
                        icon = "⭐",
                        onClick = { 
                            openPlayStore(context)
                        }
                    )
                    
                    Divider(color = Background)
                    
                    SettingsLinkItem(
                        title = "Share App",
                        subtitle = "Share with friends",
                        icon = "📤",
                        onClick = { 
                            shareApp(context)
                        }
                    )
                    
                    Divider(color = Background)
                    
                    SettingsLinkItem(
                        title = "Privacy Policy",
                        subtitle = "Read our privacy policy",
                        icon = "🔒",
                        onClick = { 
                            openPrivacyPolicy(context)
                        }
                    )
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // App info
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Practice Abacus Speed",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "Version 1.0.0",
                    fontSize = 14.sp,
                    color = OnSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Made with ❤️ for Abacus learners",
                    fontSize = 14.sp,
                    color = OnSurfaceVariant
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

private fun vibrateDevice(context: Context, durationMs: Long) {
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            val vibrator = vibratorManager.defaultVibrator
            vibrator.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(durationMs)
            }
        }
    } catch (e: Exception) {
        // Ignore vibration errors
    }
}

private fun openPlayStore(context: Context) {
    val packageName = "com.abacus.practice"
    try {
        // Try to open in Play Store app
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    } catch (e: Exception) {
        // Fallback to browser
        val intent = Intent(Intent.ACTION_VIEW, 
            Uri.parse("https://play.google.com/store/apps/details?id=$packageName"))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}

private fun shareApp(context: Context) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "Practice Abacus Speed")
        putExtra(Intent.EXTRA_TEXT, 
            "Check out Practice Abacus Speed app! It helps you improve your abacus calculation speed.\n\nhttps://play.google.com/store/apps/details?id=com.abacus.practice")
    }
    context.startActivity(Intent.createChooser(shareIntent, "Share via"))
}

private fun openPrivacyPolicy(context: Context) {
    val privacyUrl = "https://omwaman1.github.io/abacuspracticeapp/privacy.html"
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(privacyUrl))
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
}

@Composable
private fun SettingsToggleItem(
    title: String,
    subtitle: String,
    icon: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = icon,
            fontSize = 28.sp
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = OnSurface
            )
            Text(
                text = subtitle,
                fontSize = 13.sp,
                color = OnSurfaceVariant
            )
        }
        
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Primary,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = OnSurfaceVariant.copy(alpha = 0.3f)
            )
        )
    }
}

@Composable
private fun SettingsLinkItem(
    title: String,
    subtitle: String,
    icon: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = icon,
            fontSize = 28.sp
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = OnSurface
            )
            Text(
                text = subtitle,
                fontSize = 13.sp,
                color = OnSurfaceVariant
            )
        }
        
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = OnSurfaceVariant,
            modifier = Modifier.size(24.dp)
        )
    }
}

// Helper functions to get settings from other screens
object SettingsManager {
    fun isSoundEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_SOUND_ENABLED, true)
    }
    
    fun isVibrationEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_VIBRATION_ENABLED, true)
    }
    
    fun vibrate(context: Context, durationMs: Long = 50) {
        if (isVibrationEnabled(context)) {
            vibrateDevice(context, durationMs)
        }
    }
}
