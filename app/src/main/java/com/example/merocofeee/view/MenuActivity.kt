
package com.example.merocofeee.view

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.merocofeee.R
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.merocofeee.model.ProductModel

import com.example.merocofeee.repository.ProductRepoImpl
import com.example.merocofeee.viewmodel.CartViewModel
import com.example.merocofeee.viewmodel.ProductViewModel
import kotlin.collections.forEach
import kotlin.text.format
import kotlin.text.isNotEmpty

// --- THEME COLORS (consistent with homescreen.kt) ---
private val CreamBackground = Color(0xFFFDF5E6)
private val DarkBrown = Color(0xFF4E342E)
private val AccentOrange = Color(0xFFFF9800)
private val SoftGray = Color(0xFF757575)

class MenuActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val productId = intent.getStringExtra("productId") ?: ""

        setContent {
            MenuScreen(productId)
        }
    }
}

// ==========================================================
// MAIN MENU SCREEN
// ==========================================================
@Composable
fun MenuScreen(productId: String) {
    val productViewModel = remember { ProductViewModel(ProductRepoImpl()) }
    val product by productViewModel.product.observeAsState()

    LaunchedEffect(productId) {
        if (productId.isNotEmpty()) {
            productViewModel.getProductById(productId) { _, _, _ -> }
        }
    }

    Scaffold(containerColor = CreamBackground) { padding ->
        if (product == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = DarkBrown)
            }
        } else {
            val item = product!!
            MenuContent(item)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MenuContent(product: ProductModel) {
    val context = LocalContext.current
    val activity = context as? Activity

    var selectedSize by remember { mutableStateOf("Medium") }
    val selectedExtras = remember { mutableStateListOf<String>() }
    val priceOfExtra = 50.0 // Define price for extras

    val finalPrice = product.price + (selectedExtras.size * priceOfExtra)

    Column(modifier = Modifier.fillMaxSize()) {
        // Scrollable content area
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
            ) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.title,
                    placeholder = painterResource(id = R.drawable.img),
                    error = painterResource(id = R.drawable.img),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // Back Button
                IconButton(
                    onClick = { activity?.finish() },
                    modifier = Modifier
                        .padding(16.dp)
                        .clip(CircleShape)
                        .background(DarkBrown.copy(alpha = 0.5f))
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
            }

            // --- DETAILS SHEET ---
            Column(modifier = Modifier.padding(24.dp)) {
                // Title and Rating
                Text(product.title, style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.ExtraBold, color = DarkBrown))
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = "Rating", tint = AccentOrange, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${product.rating} (${product.ratingCount} reviews)", color = SoftGray, fontSize = 16.sp)
                }

                // Description
                Spacer(modifier = Modifier.height(16.dp))
                Text(product.description, color = SoftGray, fontSize = 16.sp, lineHeight = 24.sp)

                // Divider
                Spacer(modifier = Modifier.height(24.dp))
                Spacer(modifier = Modifier.height(1.dp).fillMaxWidth().background(SoftGray.copy(alpha = 0.2f)))

                // Size Selector
                Spacer(modifier = Modifier.height(24.dp))
                Text("Size", style = MaterialTheme.typography.titleLarge.copy(color = DarkBrown, fontWeight = FontWeight.Bold))
                Row(modifier = Modifier.padding(top = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    listOf("Small").forEach {
                        CustomizationButton(label = it, isSelected = selectedSize == it) { selectedSize = it }
                    }
                }

                // Extras Selector
                Spacer(modifier = Modifier.height(24.dp))
                Text("Extras (+Rs. $priceOfExtra)", style = MaterialTheme.typography.titleLarge.copy(color = DarkBrown, fontWeight = FontWeight.Bold))
                FlowRow(modifier = Modifier.padding(top = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    val extras = listOf(
                        "Extra Shot",
                        "Oat Milk",
                        "Vanilla Syrup",
                        "Caramel Drizzle",
                        "Whipped Cream"
                    )
                    extras.forEach { extra ->
                        CustomizationButton(label = extra, isSelected = selectedExtras.contains(extra)) {
                            if (selectedExtras.contains(extra)) selectedExtras.remove(extra) else selectedExtras.add(extra)
                        }
                    }
                }
            }
        }

        // --- BOTTOM BAR ---
        ProductBottomBar(price = finalPrice) {
            CartViewModel.addToCart(product)
            Toast.makeText(context, "${product.title} added to cart", Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun CustomizationButton(label: String, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (isSelected) AccentOrange.copy(alpha = 0.1f) else Color.Transparent
    val textColor = if (isSelected) AccentOrange else DarkBrown
    val borderColor = if (isSelected) AccentOrange else SoftGray.copy(alpha = 0.5f)

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Text(label, color = textColor, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
    }
}

@Composable
fun ProductBottomBar(price: Double, onAddToCart: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Price", style = MaterialTheme.typography.bodyMedium.copy(color = SoftGray))
                Text("Rs. ${String.format("%.0f", price)}", style = MaterialTheme.typography.headlineMedium.copy(color = DarkBrown, fontWeight = FontWeight.ExtraBold))
            }
            Button(
                onClick = onAddToCart,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkBrown),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Icon(Icons.Default.ShoppingCart, contentDescription = "Add to Cart", tint = Color.White)
                Spacer(modifier = Modifier.width(12.dp))
                Text("Add to Cart", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}
