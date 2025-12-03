package com.example.merocofeee

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// *** IMPORTANT: You must ensure R.drawable.img exists ***

class RegistrationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Using the new SignUpScreen Composable
            SignUpScreen(
                onLoginClick = {
                    // Navigate back to LoginActivity
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                },
                onSignUpSuccess = {
                    // Example: Navigate to Dashboard after successful sign-up
                    // val intent = Intent(this, DashboardActivity::class.java)
                    // startActivity(intent)
                    // finish()
                }
            )
        }
    }
}

// --- Composable Implementation ---

@Composable
fun SignUpScreen(
    onLoginClick: () -> Unit = {},
    onSignUpSuccess: () -> Unit = {}
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Use LocalContext to access resources if needed, though for this code it's implicit
    val context = LocalContext.current
    val backgroundImageResource = R.drawable.img

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(Color(0xFFF3E7D4), Color(0xFF8B3C1C))
                )
            )
    ) {

        // Background Image
        Image(
            painter = painterResource(id = backgroundImageResource),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.85f
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center


        ) {



            Card(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(0.85f),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF4A2E1F) // Dark brown
                )

            ) {

                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Sign Up",
                        fontSize = 32.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 40.dp)
                    )

                    Text(
                        text = "Create your account",
                        color = Color.White,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Full Name
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        placeholder = { Text("Full name", color = Color.LightGray) },
                        leadingIcon = {
                            Icon(Icons.Default.Person, contentDescription = null, tint = Color.White)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFFE6A21A), // Orange accent
                            unfocusedBorderColor = Color.LightGray,
                            cursorColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Email
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = { Text("Email address", color = Color.LightGray) },
                        leadingIcon = {
                            Icon(Icons.Default.Email, contentDescription = null, tint = Color.White)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFFE6A21A),
                            unfocusedBorderColor = Color.LightGray,
                            cursorColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Password
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = { Text("Password", color = Color.LightGray) },
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = Color.White)
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFFE6A21A),
                            unfocusedBorderColor = Color.LightGray,
                            cursorColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Already have account? Text (Clickable to login)
                    Text(
                        text = "Already have account?",
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier.clickable(onClick = onLoginClick) // Added clickable
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Sign Up button
                    Button(
                        onClick = onSignUpSuccess, // Triggers sign up success logic
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE6A21A)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Sign Up", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpPreview() {
    // Replace with your actual theme if needed
    // MeroCoffeeTheme {
    SignUpScreen()
    // }
}

