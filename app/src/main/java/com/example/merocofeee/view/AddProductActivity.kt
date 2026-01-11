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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.merocofeee.R
import coil3.compose.AsyncImage
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

@Composable
fun AddProductBody(
    selectedImageUri: Uri?,
    onPickImage: () -> Unit
) {
    var pName by remember { mutableStateOf("") }
    var pPrice by remember { mutableStateOf("") }
    var pDesc by remember { mutableStateOf("") }

    val repo = remember { ProductRepoImpl() }
    val viewModel = remember { ProductViewModel(repo) }

    val context = LocalContext.current
    val activity = context as? Activity



    Scaffold { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
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
                        .padding(10.dp)
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
                OutlinedTextField(
                    value = pName,
                    onValueChange = {
                        pName = it
                    },
                    placeholder = {
                        Text("Enter product name")
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = pPrice,
                    onValueChange = {
                        pPrice = it
                    },
                    placeholder = {
                        Text("Enter price")
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = pDesc,
                    onValueChange = {
                        pDesc = it
                    },
                    placeholder = {
                        Text("Enter Description")
                    },
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {
                        if (selectedImageUri != null) {
                            viewModel.uploadImage(context, selectedImageUri) { imageUrl ->
                                if (imageUrl != null) {
                                    val model = ProductModel(
                                        title = pName,
                                        description = pDesc,
                                        imageUrl = listOf(imageUrl),
                                        price = pPrice.toDouble()
                                    )
                                    viewModel.addProduct(model ) { success, msg, _ ->
                                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                        if (success) activity?.finish()
                                    }

                                } else {
                                    Log.e("Upload Error", "Failed to upload image to Cloudinary")
                                }
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "Please select an image first",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add Product")
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