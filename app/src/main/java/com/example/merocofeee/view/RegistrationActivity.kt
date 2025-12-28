package com.example.merocofeee.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.merocofeee.R
import com.example.merocofeee.model.UserModel
import com.example.merocofeee.repository.UserRepoImpl
import com.example.merocofeee.viewmodel.UserViewModel


class RegistrationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { RegistrationBody() }
    }
}

@Composable
fun RegistrationBody() {

    val userViewModel = remember { UserViewModel(UserRepoImpl()) }

    var fullName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var visibility by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val activity = context as? Activity

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFF3E7D4), Color(0xFF8B3C1C))
                )
            )
    ) {

        Image(
            painter = painterResource(id = R.drawable.img),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.85f
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(0.85f),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF4A2E1F))
            ) {

                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally


                ) {

                    Text(
                        "Sign Up",
                        fontSize = 32.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(20.dp))

                    // FULL NAME FIELD
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        leadingIcon = { Icon(Icons.Default.Person, null, tint = Color.White) },
                        placeholder = { Text("Full Name", color = Color.LightGray) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = fieldColors(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(Modifier.height(12.dp))

                    // USERNAME FIELD
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        leadingIcon = { Icon(Icons.Default.Person, null, tint = Color.White) },
                        placeholder = { Text("Username", color = Color.LightGray) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = fieldColors(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(Modifier.height(12.dp))

                    // EMAIL FIELD
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        leadingIcon = { Icon(Icons.Default.Email, null, tint = Color.White) },
                        placeholder = { Text("Email Address", color = Color.LightGray) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = fieldColors(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(Modifier.height(12.dp))

                    // PASSWORD FIELD
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        leadingIcon = { Icon(Icons.Default.Lock, null, tint = Color.White) },
                        placeholder = { Text("Password", color = Color.LightGray) },
                        visualTransformation = if (!visibility) PasswordVisualTransformation() else VisualTransformation.None,
                        trailingIcon = {
                            IconButton(onClick = { visibility = !visibility }) {
                                Icon(
                                    painterResource(
                                        if (visibility) R.drawable.outline_visibility_off_24
                                        else R.drawable.outline_visibility_24
                                    ), null
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = fieldColors(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(Modifier.height(12.dp))

                    // CONFIRM PASSWORD FIELD
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        leadingIcon = { Icon(Icons.Default.Lock, null, tint = Color.White) },
                        placeholder = { Text("Confirm Password", color = Color.LightGray) },
                        visualTransformation = if (!visibility) PasswordVisualTransformation() else VisualTransformation.None,
                        trailingIcon = {
                            IconButton(onClick = { visibility = !visibility }) {
                                Icon(
                                    painterResource(
                                        if (visibility) R.drawable.outline_visibility_off_24
                                        else R.drawable.outline_visibility_24
                                    ), null
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = fieldColors(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(Modifier.height(16.dp))

                    // SIGN UP BUTTON
                    Button(
                        onClick = {
                                                        if (fullName.isBlank()) {
                                toast(context, "Full name required!")
                                return@Button
                            }
                            if (username.isBlank()) {
                                toast(context, "Username required!")
                                return@Button
                            }
                            if (email.isBlank()) {
                                toast(context, "Email required!")
                                return@Button
                            }
                            if (password.isBlank()) {
                                toast(context, "Password required!")
                                return@Button
                            }
                            if (password != confirmPassword) {
                                toast(context, "Passwords do not match!")
                                return@Button
                            }

                            userViewModel.register(email, password) { success, msg, userId ->
                                if (success) {

                                    val model = UserModel(
                                        userId = userId,
                                        firstname = "",
                                        lastname = "",
                                        email = "",
                                        dob = " ",
                                        gender = "",
                                    )

                                    userViewModel.addUserTODatabase(userId, model) { ok, message ->
                                        if (ok) {
                                            Toast.makeText(context, "Registration successful!", Toast.LENGTH_SHORT).show()
                                          activity?.finish()
                                        } else {
                                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                        }
                                    }

                                } else {
                                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                }
                            }


                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE6A21A)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Sign Up", color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    Spacer(Modifier.height(8.dp))

                    Text(
                        "Already have an account?",
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier.clickable {
                            activity?.startActivity(Intent(context, LoginActivity::class.java))
                            activity?.finish()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    focusedBorderColor = Color(0xFFE6A21A),
    unfocusedBorderColor = Color.LightGray,
    cursorColor = Color.White
)

fun toast(context: Context, msg: String) =
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()

@Preview(showBackground = true)
@Composable
fun RegistrationPreview() {
    RegistrationBody()
}
