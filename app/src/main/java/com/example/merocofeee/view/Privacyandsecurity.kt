package com.example.merocofeee.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Phone

import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


private val CreamBackground = Color(0xFFFDF5E6)
private val DarkBrown = Color(0xFF4E342E)
private val SoftGray = Color(0xFF757575)


data class PrivacyItem(val icon: ImageVector, val title: String, val description: String)

@Composable
fun PrivacySecurityScreen(onBack: () -> Unit) {

    val privacyItems = listOf(
        PrivacyItem(Icons.Default.Lock, "Change Password", "Update your password regularly to keep your account secure."),
        PrivacyItem(Icons.Default.Notifications, "Manage App Permissions", "Control which data and device features our app can access."),
        PrivacyItem(Icons.Default.Phone, "Privacy Settings", "Control how your personal information and activity data are used."),
        PrivacyItem(Icons.Default.Share, "Data Sharing", "Manage how your data is shared with third-party services.")
    )

    Scaffold(
        containerColor = CreamBackground,
        topBar = { PrivacyTopAppBar(onBack = onBack) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            privacyItems.forEach { item ->
                PrivacyItemCard(item = item)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun PrivacyTopAppBar(onBack: () -> Unit) {
    Surface(shadowElevation = 4.dp, color = CreamBackground) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = DarkBrown)
            }
            Text(
                text = "Privacy & Security",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = DarkBrown),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(48.dp)) // Balance the back button
        }
    }
}

@Composable
fun PrivacyItemCard(item: PrivacyItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* TODO: Handle click for this item */ },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = null,
                tint = DarkBrown,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = item.title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkBrown
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.description,
                    fontSize = 15.sp,
                    color = SoftGray,
                    lineHeight = 22.sp
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFDF5E6)
@Composable
fun PrivacySecurityScreenPreview() {
    PrivacySecurityScreen(onBack = {})
}
