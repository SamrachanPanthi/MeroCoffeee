package com.example.merocofeee.view

import android.content.Intent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.merocofeee.R
import com.example.merocofeee.view.ui.theme.Brown
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileScreen() {
    val context = LocalContext.current
    var selectedIndex by remember { mutableStateOf(0) }

    // Get current logged-in user
    val firebaseUser = FirebaseAuth.getInstance().currentUser
    val name = firebaseUser?.displayName ?: "User"
    val email = firebaseUser?.email ?: "No Email"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brown)
    ) {
        when (selectedIndex) {
            // Main Profile Menu
            0 -> ProfileMainScreen(
                name = name,
                email = email,
                onNavigate = { selectedIndex = it },
                onBackClick = {
                    val intent = Intent(context, DashboardActivity2::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    context.startActivity(intent)
                    (context as? android.app.Activity)?.finish()
                }
            )

            // Sub-Screens
            1 -> EditProfileScreen(onBack = {})
            2 -> PrivacySecurityScreen(onBack = {})
            3 -> HelpSupportScreen(onBack = {})

            else -> ProfileMainScreen(
                name = name,
                email = email,
                onNavigate = { selectedIndex = it },
                onBackClick = {}
            )
        }
    }
}

@Composable
fun ProfileMainScreen(
    name: String,
    email: String,
    onNavigate: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brown)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Spacer(Modifier.height(20.dp))

        // HEADER
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.outline_arrow_back_24),
                contentDescription = "Back",
                tint = Color.Black,
                modifier = Modifier
                    .size(28.dp)
                    .clickable { onBackClick() }
            )

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Profile",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(28.dp)) // Balance the back icon
        }

        Spacer(Modifier.height(25.dp))

        // USER INFO CARD
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF2EEFF), RoundedCornerShape(20.dp))
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // For now, using static image; can be replaced with Firebase profile picture later
            Image(
                painter = painterResource(R.drawable.img),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(14.dp))
            )

            Spacer(Modifier.width(20.dp))

            Column {
                Text(name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(email, fontSize = 14.sp, color = Color.Gray)
                Text("Member since January 2024", fontSize = 12.sp, color = Color.Gray)
            }
        }

        Spacer(Modifier.height(30.dp))

        // MENU ITEMS
        ProfileItem(R.drawable.baseline_person_2_24, "Edit Profile") { onNavigate(1) }
        ProfileItem(R.drawable.outline_security_24, "Privacy & Security") { onNavigate(2) }

        ProfileItem(R.drawable.baseline_help_outline_24, "Help & Support") { onNavigate(3) }

        Spacer(Modifier.height(20.dp))

        LogoutItem {
            // 1. Logout using Firebase Auth
            FirebaseAuth.getInstance().signOut()

            // 2. Navigate to LoginActivity
            val intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)

            // 3. Finish current activity
            (context as? android.app.Activity)?.finish()
        }
    }
}

@Composable
fun ProfileItem(
    icon: Int,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .background(Color(0xFFF8F5FF), RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(26.dp)
        )

        Spacer(Modifier.width(18.dp))

        Text(label, fontSize = 16.sp, fontWeight = FontWeight.Medium)

        Spacer(Modifier.weight(1f))

        Icon(
            painter = painterResource(R.drawable.baseline_arrow_forward_ios_24),
            contentDescription = null,
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
fun LogoutItem(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .background(Color(0xFFF2EEFF), RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.outline_logout_24),
            contentDescription = null,
            tint = Color(0xFFD00000),
            modifier = Modifier.size(26.dp)
        )

        Spacer(Modifier.width(18.dp))

        Text(
            "Logout",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFD00000)
        )
    }
}
