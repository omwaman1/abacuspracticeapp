package com.abacus.practice.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
fun PrivacyPolicyScreen(
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Privacy Policy",
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Header
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(6.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Surface)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "🧮", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Practice Abacus Speed",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Last Updated: January 5, 2026",
                        fontSize = 13.sp,
                        color = OnSurfaceVariant
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Content sections
            PolicySection(
                title = "📜 Introduction",
                content = "Welcome to Practice Abacus Speed. We respect your privacy and are committed to protecting your personal information. This Privacy Policy explains how we collect, use, and safeguard your information when you use our mobile application.\n\nBy using the App, you agree to the collection and use of information in accordance with this policy."
            )
            
            PolicySection(
                title = "📊 Information We Collect",
                content = "Information You Provide:\n• User Preferences: Settings such as sound effects and vibration preferences.\n• Practice Data: Your scores, progress, and performance statistics stored locally.\n\nInformation Collected Automatically:\n• Device Information: Device model, OS version for analytics.\n• Usage Data: Features used, time spent, and session data."
            )
            
            PolicySection(
                title = "🔧 How We Use Information",
                content = "We use the information to:\n• Provide and maintain the App\n• Save your preferences and settings\n• Track your practice progress\n• Improve and optimize the App\n• Analyze usage patterns"
            )
            
            PolicySection(
                title = "💾 Data Storage",
                content = "🔒 Your data stays on your device!\n\nAll your practice data and preferences are stored locally on your device. We do not collect or store personal data on external servers. Your data remains on your device and under your control.",
                isHighlighted = true
            )
            
            PolicySection(
                title = "🔗 Third-Party Services",
                content = "The App uses Google Play Services which may collect information. Please refer to Google's Privacy Policy for more information."
            )
            
            PolicySection(
                title = "👨‍👩‍👧‍👦 Children's Privacy",
                content = "The App is suitable for users of all ages, including children. We do not knowingly collect personally identifiable information from children under 13. The App is designed as an educational tool for practicing abacus and math skills."
            )
            
            PolicySection(
                title = "🔐 Data Security",
                content = "We value your trust and strive to use commercially acceptable means of protecting your information. However, no method of transmission over the internet is 100% secure."
            )
            
            PolicySection(
                title = "⚖️ Your Rights",
                content = "You have the right to:\n• Access: View your practice data within the App\n• Delete: Clear all data by uninstalling or clearing app data\n• Update: Modify preferences within App settings"
            )
            
            PolicySection(
                title = "📧 Contact Us",
                content = "If you have questions about our Privacy Policy:\n\nEmail: omwaman1@gmail.com\nApp: Practice Abacus Speed"
            )
            
            PolicySection(
                title = "✅ Consent",
                content = "By using our App, you hereby consent to our Privacy Policy and agree to its terms."
            )
            
            // Footer
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "© 2026 Practice Abacus Speed\nAll rights reserved.",
                fontSize = 12.sp,
                color = OnSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun PolicySection(
    title: String,
    content: String,
    isHighlighted: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .shadow(4.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isHighlighted) Primary.copy(alpha = 0.1f) else Surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = if (isHighlighted) Primary else MixedColor
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = content,
                fontSize = 14.sp,
                lineHeight = 22.sp,
                color = OnSurface
            )
        }
    }
}
