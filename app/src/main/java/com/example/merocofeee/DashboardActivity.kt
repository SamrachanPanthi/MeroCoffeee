package com.example.merocofeee

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image // ADDED
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState // ADDED
import androidx.compose.foundation.verticalScroll // ADDED
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale // ADDED
import androidx.compose.ui.res.painterResource // ADDED
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.BorderStroke
import com.example.merocofeee.LoginScreen


import com.example.merocofeee.R // Using the user's package name
import com.example.merocofeee.RegistrationActivity
import com.example.merocofeee.ui.theme.MeroCofeeeTheme


// --- 1. Data Models ---

data class QuickAction(
    val icon: ImageVector,
    val label: String
)

data class FoodItem(
    val id: Int,
    val label: String,
    val imageResId: Int // CHANGED to resource ID
)

data class CoffeeProduct(
    val id: Int,
    val name: String,
    val price: Int,
    val imageResId: Int // CHANGED to resource ID
)

// --- 2. Mock Data ---
// EXPANDED lists to ensure horizontal scrolling is visible

val quickActions = listOf(
    QuickAction(Icons.Default.Favorite, "Favorites"),
    QuickAction(Icons.AutoMirrored.Filled.List, "History"),
    QuickAction(Icons.Default.ShoppingCart, "Orders")
)

val foodItems = listOf(
    FoodItem(1, "croissants", R.drawable.cro), // Assuming R.drawable.croissant exists
    FoodItem(2, "muffins", R.drawable.muffin),
    FoodItem(3, "scones", R.drawable.scone),
    FoodItem(4, "donuts", R.drawable.donut),
    FoodItem(5, "bagels", R.drawable.bagel),
    FoodItem(6, "cookies", R.drawable.cookie),
    FoodItem(7, "pastries", R.drawable.pastry),
    FoodItem(8, "waffles", R.drawable.waffle)
)

val coffeeProducts = listOf(
    CoffeeProduct(1, "LATTE", 200, R.drawable.lattee),
    CoffeeProduct(2, "ESPRESSO", 160, R.drawable.expresso),
    CoffeeProduct(3, "AMERICANO", 300, R.drawable.americano),
    CoffeeProduct(4, "CAPPUCCINO", 250, R.drawable.cappuccino),
    CoffeeProduct(5, "MOCHA", 280, R.drawable.mocha),
    CoffeeProduct(6, "FLAT WHITE", 260, R.drawable.flatwhite),
    CoffeeProduct(7, "MACCHIATO", 220, R.drawable.macchiato),
    CoffeeProduct(8, "COLD BREW", 320, R.drawable.coldbrew)
)


// --- 3. UI Components ---
class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            DashboardScreen()
        }
    }
}
@Composable
fun DashboardScreen() {
    Scaffold(
        bottomBar = { BottomNavBar() },
        topBar = { TopStatusBar() }
    ) { paddingValues ->
        // Main content column
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
                .padding(horizontal = 16.dp)
                // ADDED for overall vertical scrolling
                .verticalScroll(rememberScrollState())
        ) {
            SearchBar()
            Spacer(modifier = Modifier.height(16.dp))

            QuickActionButtons(actions = quickActions)
            Spacer(modifier = Modifier.height(20.dp))

            HeroBanner()
            Spacer(modifier = Modifier.height(30.dp))

            SectionHeader(title = "FOODS TO GO WITH", isLarge = false)
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalFoodList(items = foodItems)
            Spacer(modifier = Modifier.height(30.dp))

            // Fixed typo: "AVILABLE" to "AVAILABLE" (based on previous user error fix)
            SectionHeader(title = "COFFEES AVAILABLE", isLarge = true, showArrow = true)
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalProductList(products = coffeeProducts)
            // Added extra space at the bottom to ensure content doesn't hit the bottom bar
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun TopStatusBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

    }
}

@Composable
fun SearchBar() {
    OutlinedTextField(
        value = "",
        onValueChange = { /* Handle input change */ },
        placeholder = { Text("Search", color = Color.Gray) },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.Black)
        },
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.LightGray.copy(alpha = 0.5f),
            unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f),
            cursorColor = Color.Black,
            focusedContainerColor = Color(0xFFF8F8F8),
            unfocusedContainerColor = Color(0xFFF8F8F8)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .padding(top = 8.dp)
    )
}

@Composable
fun QuickActionButtons(actions: List<QuickAction>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        actions.forEach { action ->
            OutlinedButton(
                onClick = { /* Handle click */ },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                border = BorderStroke(1.dp, Color.LightGray),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Icon(
                    imageVector = action.icon,
                    contentDescription = action.label,
                    modifier = Modifier.size(20.dp),
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(action.label, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun HeroBanner() {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // REPLACED: Placeholder Box with Image
            Image(
                painter = painterResource(id = R.drawable.beans),
                contentDescription = "Coffee beans background",
                contentScale = ContentScale.Crop, // Ensures image fills the bounds
                modifier = Modifier.fillMaxSize()
            )

            // Dark overlay to make text pop
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
            )

            // Text "COFFEE ANYTIME"
            Text(
                text = "COFFEE\nANYTIME",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun SectionHeader(title: String, isLarge: Boolean, showArrow: Boolean = false) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showArrow) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Go to section",
                tint = Color.Gray,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
        }

        Text(
            text = title,
            fontSize = if (isLarge) 18.sp else 12.sp,
            fontWeight = if (isLarge) FontWeight.Bold else FontWeight.Normal,
            color = if (isLarge) Color.Black else Color.Gray,
        )
    }
}

@Composable
fun FoodItemCard(item: FoodItem) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(end = 16.dp)
    ) {
        // REPLACED: Placeholder Box with Image
        Image(
            painter = painterResource(id = item.imageResId),
            contentDescription = item.label,
            contentScale = ContentScale.Crop, // Crop to fill the circular bounds
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Color.LightGray) // Fallback background
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = item.label,
            fontSize = 12.sp,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun HorizontalFoodList(items: List<FoodItem>) {
    // Uses LazyRow for efficient horizontal scrolling (already in place)
    LazyRow(
        contentPadding = PaddingValues(horizontal = 0.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items) { item ->
            FoodItemCard(item)
        }
    }
}

@Composable
fun ProductCard(product: CoffeeProduct) {
    Column(
        modifier = Modifier
            .width(150.dp)
            .padding(end = 16.dp)
    ) {
        // Image Card
        Card(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {

            Image(
                painter = painterResource(id = product.imageResId),
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Price
        Text(
            text = product.price.toString(),
            fontSize = 12.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Normal
        )
        // Name (LATTE, ESPRESSO, etc.)
        Text(
            text = product.name,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

@Composable
fun HorizontalProductList(products: List<CoffeeProduct>) {
    // Uses LazyRow for efficient horizontal scrolling (already in place)
    LazyRow(
        contentPadding = PaddingValues(horizontal = 0.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(products) { product ->
            ProductCard(product)
        }
    }
}


@Composable
fun BottomNavBar() {
    NavigationBar(
        containerColor = Color.White,
        modifier = Modifier.height(70.dp),
        tonalElevation = 1.dp
    ) {
        NavigationBarItem(
            selected = true,
            onClick = { /* navigate */ },
            icon = { Icon(imageVector = Icons.Default.Home, contentDescription = "Home", tint = Color.Black) },
            colors = NavigationBarItemDefaults.colors(indicatorColor = Color.White)
        )
        NavigationBarItem(
            selected = false,
            onClick = { /* navigate */ },
            icon = { Icon(imageVector = Icons.Default.LocationOn, contentDescription = "Explore", tint = Color.Gray) },
            colors = NavigationBarItemDefaults.colors(indicatorColor = Color.White)
        )
        NavigationBarItem(
            selected = false,
            onClick = { /* navigate */ },
            icon = { Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "Cart", tint = Color.Gray) },
            colors = NavigationBarItemDefaults.colors(indicatorColor = Color.White)
        )
        NavigationBarItem(
            selected = false,
            onClick = {  },
            icon = { Icon(imageVector = Icons.Default.Notifications, contentDescription = "Notifications", tint = Color.Gray) },
            colors = NavigationBarItemDefaults.colors(indicatorColor = Color.White)
        )
        NavigationBarItem(
            selected = false,
            onClick = { /* navigate */ },
            icon = { Icon(imageVector = Icons.Default.Person, contentDescription = "Profile", tint = Color.Gray) },
            colors = NavigationBarItemDefaults.colors(indicatorColor = Color.White)
        )
    }
}

// --- 4. Preview ---

@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    MeroCofeeeTheme {  }
        DashboardScreen()
    }
