package com.example.merocofeee.view
import ImageUtils
import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.merocofeee.R
import com.example.merocofeee.model.ProductModel
import com.example.merocofeee.repository.ProductRepoImpl
import com.example.merocofeee.viewmodel.ProductViewModel

class AddProductActivity : ComponentActivity() {
    lateinit var imageUtils: ImageUtils
    var selectedImageUri by mutableStateOf<Uri?>(null)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        imageUtils = ImageUtils(this, this)
        imageUtils.registerLaunchers { uri ->
            selectedImageUri = uri
        }
        setContent {
            AddProductBody(
                selectedImageUri = selectedImageUri,
                onPickImage = { imageUtils.launchImagePicker() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductBody(
    selectedImageUri: Uri?,
    onPickImage: () -> Unit
) {
    var pName by remember { mutableStateOf("") }
    var pPrice by remember { mutableStateOf("") }
    var pDesc by remember { mutableStateOf("") }
    var pCategory by remember { mutableStateOf("") }
    var isExpanded by remember { mutableStateOf(false) }

    val repo = remember { ProductRepoImpl() }
    val viewModel = remember { ProductViewModel(repo) }

    val context = LocalContext.current
    val activity = context as? Activity

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Product", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { activity?.finish() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFDF5E6), // CreamBackground
                    titleContentColor = Color(0xFF4E342E) // DarkBrown
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            onPickImage()
                        }

                ) {
                    if (selectedImageUri != null) {
                        AsyncImage(
                            model = selectedImageUri,
                            contentDescription = "Selected Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            painterResource(R.drawable.img),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = pName,
                    onValueChange = { pName = it },
                    placeholder = { Text("Enter product name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = pPrice,
                    onValueChange = { pPrice = it },
                    placeholder = { Text("Enter price") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = pDesc,
                    onValueChange = { pDesc = it },
                    placeholder = { Text("Enter Description") },
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                ExposedDropdownMenuBox(
                    expanded = isExpanded,
                    onExpandedChange = { isExpanded = it }
                ) {
                    OutlinedTextField(
                        value = pCategory,
                        onValueChange = {},
                        readOnly = true,
                        placeholder = { Text(text = "Select Category") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                    )

                    ExposedDropdownMenu(
                        expanded = isExpanded,
                        onDismissRequest = { isExpanded = false })
                    {
                        DropdownMenuItem(
                            text = { Text(text = "Expresso") },
                            onClick = {
                                pCategory = "Expresso"
                                isExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(text = "Latte") },
                            onClick = {
                                pCategory = "Latte"
                                isExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(text = "Bakery") },
                            onClick = {
                                pCategory = "Bakery"
                                isExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(text = "ColdCoffee") },
                            onClick = {
                                pCategory = "ColdCoffee"
                                isExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(text = "Cappuccino") },
                            onClick = {
                                pCategory = "Cappuccino"
                                isExpanded = false
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        if (selectedImageUri != null && pCategory.isNotBlank()) {
                            Toast.makeText(context, "Uploading image...", Toast.LENGTH_SHORT).show()
                            viewModel.uploadImage(context, selectedImageUri) { imageUrl ->
                                Log.d("AddProduct", "Image upload finished. URL received: $imageUrl")

                                if (!imageUrl.isNullOrEmpty()) {
                                    val model = ProductModel(
                                        title = pName,
                                        description = pDesc,
                                        imageUrl = imageUrl,
                                        price = pPrice.toDoubleOrNull() ?: 0.0, // Safer
                                        category = pCategory
                                    )
                                    viewModel.addProduct(model) { success, msg, _ ->
                                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                        if (success) {
                                            Log.d("AddProduct", "Product added to database.")
                                            activity?.finish()
                                        } else {
                                            Log.e("AddProduct", "DB Error: $msg")
                                        }
                                    }
                                } else {
                                    Log.e("AddProduct", "Image upload failed, received null or empty URL.")
                                    Toast.makeText(context, "Image upload failed. Please try again.", Toast.LENGTH_LONG).show()
                                }
                            }
                        } else {
                            Toast.makeText(context, "Please select an image and category first", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(52.dp)
                ) {
                    Text("Add Product", fontSize = 16.sp)
                }
            }
        }

    }
}

@Preview
@Composable
fun previewAddProductBody() {
    AddProductBody(
        selectedImageUri = null, // or pass a mock Uri if needed
        onPickImage = {} // no-op
    )
}