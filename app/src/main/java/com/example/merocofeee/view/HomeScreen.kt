package com.example.merocofeee.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import com.example.merocofeee.R

// --- 1. Data Models (Simplified, ensure these are accessible/imported) ---

data class QuickAction(val icon: ImageVector, val label: String)
data class FoodItem(val id: Int, val label: String, val imageResId: Int)
data class CoffeeProduct(val id: Int, val name: String, val price: Int, val imageResId: Int)


val quickActions = listOf(
    QuickAction(Icons.Default.Favorite, "Favorites"),
    QuickAction(Icons.AutoMirrored.Filled.List, "History"),
    QuickAction(Icons.Default.ShoppingCart, "Orders")
)

val foodItems = listOf(
    FoodItem(1, "croissants", R.drawable.cro),
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


// --------------------------------------------------------------------------
// 3. Main Screen Composable
// --------------------------------------------------------------------------

@Composable

fun HomeScreenContent() {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        item { Spacer(modifier = Modifier.height(8.dp)) }
        //item { androidx.compose.material3.SearchBar  }

        item { QuickActionButtons(actions = quickActions) }

        item { HeroBanner() }

        item { SectionHeader("FOODS TO GO WITH", false) }
        item { HorizontalFoodList(items = foodItems) }

        item { SectionHeader("COFFEES AVAILABLE", true, showArrow = true) }

        item { VerticalProductList(products = coffeeProducts) }

        item { Spacer(modifier = Modifier.height(80.dp)) }
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
            Image(
                // R.drawable.beans must exist
                painter = painterResource(id = R.drawable.beans),
                contentDescription = "Coffee beans background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
            )

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
        Image(
            // R.drawable.cro, R.drawable.muffin, etc. must exist
            painter = painterResource(id = item.imageResId),
            contentDescription = item.label,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
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
    LazyRow(
        contentPadding = PaddingValues(horizontal = 0.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
//        LazyListScope.items(items) { item ->
//            FoodItemCard(item)
       // }
    }
}

@Composable
fun ProductCard(product: CoffeeProduct) {
    Column(
        modifier = Modifier
            .width(150.dp)
            .padding(end = 16.dp)
    ) {
        Card(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Image(
                // R.drawable.lattee, R.drawable.expresso, etc. must exist
                painter = painterResource(id = product.imageResId),
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "PKR ${product.price}",
            fontSize = 12.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Normal
        )
        Text(
            text = product.name,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

@Composable
fun VerticalProductList(products: List<CoffeeProduct>) {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        products.forEach { product ->
            ProductCard(product)
        }
    }
}

// Optional Preview for this file
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreenContent()
}