package com.example.merocofeee

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import coil3.compose.AsyncImage
import com.example.merocofeee.model.ProductModel
import com.example.merocofeee.repository.ProductRepoImpl
import com.example.merocofeee.repository.UserRepoImpl
import com.example.merocofeee.view.MenuActivity
import com.example.merocofeee.viewmodel.ProductViewModel
import com.example.merocofeee.viewmodel.UserViewModel
import java.util.Calendar

// --- NEW THEME COLORS ---
private val CreamBackground = Color(0xFFFDF5E6)
private val DarkBrown = Color(0xFF4E342E)
private val AccentOrange = Color(0xFFFF9800)
private val SoftGray = Color(0xFF757575)

// --- DATA MODELS ---
data class Category(val name: String)

val categories = listOf(
    Category("Expresso"),
    Category("Latte"),
    Category("Bakery"),
    Category("ColdCoffee"),
    Category("Cappuccino")
)


// ==========================================================
// MAIN DASHBOARD SCREEN
// ==========================================================
@Composable
fun DashboardScreen() {
    val productViewModel = remember { ProductViewModel(ProductRepoImpl()) }
    val userViewModel = remember { UserViewModel(UserRepoImpl()) }

    val products by productViewModel.allProducts.observeAsState(initial = emptyList())
    val user by userViewModel.users.observeAsState(null)
    val isLoading by productViewModel.loading.observeAsState(initial = true)

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    val lifecycleOwner = LocalLifecycleOwner.current

    // Refreshes the product list every time the screen is resumed.
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                userViewModel.getCurrentUser()?.uid?.let { id -> userViewModel.getUserById(id) }
                // On resume, load based on the currently selected category or all products
                if (selectedCategory == null) {
                    productViewModel.getAllProducts { _, _, _ -> }
                } else {
                    productViewModel.getAllProductsByCategory(selectedCategory!!.name) { _, _, _ -> }
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    // Fetches products when the category selection changes
    LaunchedEffect(selectedCategory) {
        if (selectedCategory != null) {
            productViewModel.getAllProductsByCategory(selectedCategory!!.name) { _, _, _ -> }
        } else {
            productViewModel.getAllProducts { _, _, _ -> }
        }
    }

    Scaffold(containerColor = CreamBackground) {
        if (isLoading && products.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = DarkBrown)
            }
        } else {
            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .padding(it)) {

                // --- HEADER ---
                item { TopHeader(name = user?.fullname) }

                // --- SEARCH BAR ---
                item { SearchBar(query = searchQuery, onQueryChange = { searchQuery = it }) }

                // --- PROMO BANNER ---
                item { PromoBanner() }

                // --- FEATURED PRODUCTS ---
                item { FeaturedProductsSection(products.take(5)) }

                // --- CATEGORIES ---
                item {
                    CategorySection(
                        categories = categories,
                        selectedCategory = selectedCategory?.name,
                        onCategorySelected = { categoryName ->
                            selectedCategory = if (selectedCategory?.name == categoryName) {
                                null // Deselect if clicking the same category again
                            } else {
                                categories.find { it.name == categoryName }
                            }
                        }
                    )
                }

                // --- ALL PRODUCTS GRID ---
                item {
                    Text(
                        text = if (selectedCategory != null) "${selectedCategory?.name} Products" else "All Products",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, color = DarkBrown),
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 16.dp)
                    )
                }

                // Grid logic within LazyColumn
                val filteredProducts = products.filter {
                    it.title.contains(searchQuery, ignoreCase = true)
                }
                items(filteredProducts.chunked(2)) { rowItems ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        rowItems.forEach { product ->
                            NewProductCard(product = product, modifier = Modifier.weight(1f))
                        }
                        if (rowItems.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

// ==========================================================
// NEW UI COMPONENTS
// ==========================================================

@Composable
fun TopHeader(name: String?) {
    val greeting = remember {
        when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
            in 0..11 -> "Good Morning,"
            in 12..16 -> "Good Afternoon,"
            else -> "Good Evening,"
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = greeting,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = name ?: "Coffee Lover",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold, color = DarkBrown)
            )
        }
        Image(
            painter = painterResource(id = R.drawable.img), // Replace with a user profile pic if available
            contentDescription = "Profile",
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
        )
    }
}

@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        placeholder = { Text("Search for your coffee...", color = SoftGray) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = SoftGray) },
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        )
    )
}

@Composable
fun PromoBanner() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = DarkBrown)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 20.dp)
            ) {
                Text("Special Offer", style = MaterialTheme.typography.bodySmall.copy(color = AccentOrange))
                Text("Get 35% Off", style = MaterialTheme.typography.headlineMedium.copy(color = Color.White, fontWeight = FontWeight.ExtraBold))
                Text("on all Latte products", style = MaterialTheme.typography.bodyMedium.copy(color = Color.White.copy(alpha = 0.8f)))
            }
        }
    }
}

@Composable
fun FeaturedProductsSection(products: List<ProductModel>) {
    Column(modifier = Modifier.padding(top = 24.dp)) {
        Text(
            text = "Featured Drinks",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, color = DarkBrown),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(products) { product ->
                FeaturedProductCard(product)
            }
        }
    }
}

@Composable
fun FeaturedProductCard(product: ProductModel) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .width(160.dp)
            .clickable { context.startActivity(Intent(context, MenuActivity::class.java).putExtra("productId", product.productId)) },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.title,
                placeholder = painterResource(id = R.drawable.img),
                error = painterResource(id = R.drawable.img),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
            Column(modifier = Modifier.padding(12.dp)) {
                Text(product.title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DarkBrown, maxLines = 1)
                Text("Rs. ${product.price}", color = AccentOrange, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun CategorySection(
    categories: List<Category>,
    selectedCategory: String?,
    onCategorySelected: (String) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(categories) { category ->
            val isSelected = selectedCategory == category.name
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isSelected) DarkBrown else Color.White)
                    .clickable { onCategorySelected(category.name) }
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Text(
                    text = category.name,
                    color = if (isSelected) Color.White else DarkBrown,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}


@Composable
fun NewProductCard(product: ProductModel, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Card(
        modifier = modifier
            .clickable {
                context.startActivity(
                    Intent(context, MenuActivity::class.java).putExtra("productId", product.productId)
                )
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(modifier = Modifier.height(150.dp)) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.title,
                    placeholder = painterResource(R.drawable.img),
                    error = painterResource(R.drawable.img),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(product.title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DarkBrown, maxLines = 1)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = AccentOrange, modifier = Modifier.size(16.dp))
                    Text("${product.rating}", fontSize = 12.sp, color = SoftGray, modifier = Modifier.padding(start = 4.dp))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Rs. ${product.price}", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = DarkBrown)
                    IconButton(
                        onClick = {
//                            CartViewModel.addToCart(product)
//                            Toast.makeText(context, "${product.title} added to cart", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(AccentOrange)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add to cart", tint = Color.White)
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xFFFDF5E6)
@Composable
fun DashboardPreview() {
    DashboardScreen()
}