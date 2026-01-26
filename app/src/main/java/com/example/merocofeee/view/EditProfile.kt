package com.example.merocofeee.view



import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

import com.example.merocofeee.model.UserModel
import com.example.merocofeee.repository.UserRepoImpl
import com.example.merocofeee.viewmodel.UserViewModel

@Composable
fun EditProfileScreen(onBack: () -> Unit) {

    val context = LocalContext.current
    val viewModel = remember { UserViewModel(UserRepoImpl()) }

    val firebaseUser = viewModel.getCurrentUser()
    val userId = firebaseUser?.uid ?: return

    val user by viewModel.users.observeAsState()

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    LaunchedEffect(Unit) {
        viewModel.getUserById(userId)
    }

    LaunchedEffect(user) {
        user?.let {
            firstName = it.firstname
            lastName = it.lastname

            email = it.email
            gender = it.gender

            dob = it.dob
        }
    }

    val imagePicker =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
            imageUri = it
        }
    @OptIn(ExperimentalMaterial3Api::class)
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            //AsyncImage(
//                model = imageUri ?: user?.profileImage,
//                contentDescription = null,
//                modifier = Modifier
//                    .size(120.dp)
//                    .clip(CircleShape)
//                    .border(2.dp, Color.Black, CircleShape)
//                    .clickable { imagePicker.launch("image/*") },
//                contentScale = ContentScale.Crop
            //)

            Spacer(Modifier.height(20.dp))

            OutlinedTextField(firstName, { firstName = it }, label = { Text("First Name") })
            OutlinedTextField(lastName, { lastName = it }, label = { Text("Last Name") })

            OutlinedTextField(email, { email = it }, label = { Text("Email") })
            OutlinedTextField(gender, { gender = it }, label = { Text("Gender") })

            OutlinedTextField(dob, { dob = it }, label = { Text("Date of Birth") })

            Spacer(Modifier.height(20.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    val updatedUser = UserModel(
                        firstname = firstName,
                        lastname = lastName,
                      //  fullname = fullName,
                        gender = gender,

                        dob = dob,
                        email = email,
                        //profileImage = user?.profileImage ?: "",
                        userId = userId
                    )

                    viewModel.updateProfile(userId, updatedUser) { success, msg ->
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        if (success) onBack()
                    }
                }
            ) {
                Text("Update Profile")
            }
        }
    }
}
