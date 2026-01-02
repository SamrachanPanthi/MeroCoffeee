package com.example.merocofeee

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.merocofeee.model.ProductModel
import com.example.merocofeee.repository.ProductRepoImpl
import com.example.merocofeee.repository.UserRepoImpl
import com.example.merocofeee.view.MenuActivity
import com.example.merocofeee.viewmodel.ProductViewModel
import com.example.merocofeee.viewmodel.UserViewModel

private val UserModel.fullName: String?

/* ---------------- COLORS ---------------- */
val Orange = Color(0xFFFF9800)
val Field = Color(0x68460505)
val promo = Color(0xFF6F4E37)

/* ---------------- CATEGORY MODEL ---------------- */
data class Category(val name: String)

val categories = listOf(
    Category("Expresso"),
    Category("Latte"),
    Category("Bakery"),
    Category("ColdCoffee"),
    Category("Cappuccino")
)

/* ---------------- DASHBOARD SCREEN ---------------- */
@Composable
fun DashboardScreen() {
    val userViewModel = remember { UserViewModel(UserRepoImpl()) }
    val productViewModel = remember { ProductViewModel(ProductRepoImpl()) }

    val user by userViewModel.users.observeAsState(null)
    val products by productViewModel.allProducts.observeAsState(initial = emptyList())

    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current

    // Load user & products
    LaunchedEffect(Unit) {
        userViewModel.getCurrentUser()?.uid?.let { id ->
            userViewModel.getUserById(id)
        }
        productViewModel.getAllProducts { _, _, _ -> }
    }

    // Load category products
    LaunchedEffect(selectedCategory) {
        selectedCategory?.let {
            productViewModel.getAllProductsByCategory(it.name) { _, _, _ -> }
        } ?: productViewModel.getAllProducts { _, _, _ -> }
    }

    // Filter products by search query
    val filteredProducts = products.filter {
        it.title.contains(searchQuery, ignoreCase = true)
    }.take(6) // 2x3 = 6 products

    Scaffold(
        containerColor = Color.Black,
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures { focusManager.clearFocus() }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            Column(Modifier.padding(16.dp)) {
                TopBar(user?.fullName)
                Spacer(Modifier.height(20.dp))
                SearchBar(searchQuery) { searchQuery = it }
                Spacer(Modifier.height(20.dp))
                PromoCard()
                Spacer(Modifier.height(20.dp))
                Text(
                    "Categories",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            CategorySelection(selectedCategory?.name) { name ->
                selectedCategory =
                    if (selectedCategory?.name == name) null
                    else categories.find { it.name == name }
            }

            Spacer(Modifier.height(20.dp))

            Column(Modifier.padding(horizontal = 16.dp)) {
                ProductGrid(filteredProducts)
                Spacer(Modifier.height(80.dp))
            }
        }
    }
}

/* ---------------- UI COMPONENTS ---------------- */

@Composable
fun TopBar(name: String?) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.AccountCircle,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(45.dp)
            )
            Spacer(Modifier.width(8.dp))
            Column {
                Text("Good Morning", color = Color.Gray, fontSize = 12.sp)
                Text(name ?: "User", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
        Icon(Icons.Default.Notifications, null, tint = Color.White)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(query: String, onChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Search", color = Color.Gray) },
        leadingIcon = { Icon(Icons.Default.Search, null, tint = Color.Gray) },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Field,
            unfocusedContainerColor = Field,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedIndicatorColor = Orange,
            unfocusedIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
fun PromoCard() {
    Card(
        Modifier
            .fillMaxWidth()
            .height(120.dp),
        colors = CardDefaults.cardColors(containerColor = promo),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Get Special Discounts", color = Color.White, fontWeight = FontWeight.Bold)
            Text(
                "Up to 35%",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun CategorySelection(selected: String?, onSelect: (String) -> Unit) {
    LazyRow(contentPadding = PaddingValues(horizontal = 16.dp)) {
        items(categories) {
            Box(
                modifier = Modifier
                    .padding(end = 10.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (selected == it.name) Orange else Field)
                    .clickable { onSelect(it.name) }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(it.name, color = Color.White)
            }
        }
    }
}

/* ---------------- PRODUCT GRID & CARD ---------------- */

@Composable
fun ProductGrid(products: List<ProductModel>) {
    // 2x3 layout: 2 columns, 3 rows (6 products max)
    Column {
        products.chunked(2).forEach { row ->
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                row.forEach { product ->
                    ProductCard(product, Modifier.weight(1f))
                }
                if (row.size == 1) Spacer(Modifier.weight(1f))
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
fun ProductCard(product: ProductModel, modifier: Modifier) {
    val context = LocalContext.current

    Card(
        modifier = modifier
            .clickable {
                context.startActivity(
                    Intent(context, MenuActivity::class.java)
                        .putExtra("productId", product.productId)
                )
            }
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Field),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column {
            // Load first image from database URL
            val imageUrl = product.imageUrl.firstOrNull()
            if (!imageUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = product.title,
                    placeholder = painterResource(R.drawable.img),
                    error = painterResource(R.drawable.img),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.img),
                    contentDescription = product.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
            }

            Column(Modifier.padding(12.dp)) {
                Text(
                    text = product.title,
                    color = Orange,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "NPR ${product.price}",
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )
            }
        }
    }
}

/* ---------------- PREVIEW ---------------- */
@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    DashboardScreen()
}