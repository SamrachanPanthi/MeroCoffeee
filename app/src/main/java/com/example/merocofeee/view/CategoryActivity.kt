package com.example.merocofeee.view

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

import com.example.merocofeee.R
import kotlin.text.replace

// Data class for a coffee category
data class CoffeeCategory(val id: String, val title: String, val imageUrl: String)

// Coffee Brand Colors
val BrownPrimary = Color(0xFF6F4E37)
val SoftCream = Color(0xFFFDF5E6)

class CategoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Placeholder list: Replace with your actual Cloudinary URLs
            val categories = listOf(
                CoffeeCategory(
                    "1",
                    "Hot Coffee",
                    "https://res.cloudinary.com/demo/image/upload/v1/hot_coffee.jpg"
                ),
                CoffeeCategory(
                    "2",
                    "Iced Brew",
                    "https://res.cloudinary.com/demo/image/upload/v1/iced_coffee.jpg"
                ),
                CoffeeCategory(
                    "3",
                    "Bakery",
                    "https://res.cloudinary.com/demo/image/upload/v1/croissant.jpg"
                ),
                CoffeeCategory(
                    "4",
                    "Coffee Beans",
                    "https://res.cloudinary.com/demo/image/upload/v1/beans.jpg"
                )
            )
            MeroCoffeeCategoryScreen(categories)
        }
    }
}

@Composable
fun MeroCoffeeCategoryScreen(categories: List<CoffeeCategory>) {
    val context = LocalContext.current
    val activity = context as? Activity

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { activity?.finish() },
                    modifier = Modifier.background(Color.White, CircleShape)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = BrownPrimary)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Mero Categories",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = BrownPrimary
                )
            }
        },
        containerColor = SoftCream // Warm cafe background
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(categories) { category ->
                CategoryCard(category)
            }
        }
    }
}

@Composable
fun CategoryCard(category: CoffeeCategory) {
    // Cloudinary Optimization: Auto-resize to 400px and adjust quality
    val optimizedUrl = category.imageUrl.replace("/upload/", "/upload/w_400,c_fill,q_auto,f_auto/")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clickable {
                // Navigate to the list of items for this category
                // val intent = Intent(context, ProductListActivity::class.java)
                // intent.putExtra("categoryName", category.title)
                // context.startActivity(intent)
            },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = optimizedUrl,
                contentDescription = category.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.img) // Your placeholder drawable
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = category.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = BrownPrimary
                )
            }
        }
    }
}