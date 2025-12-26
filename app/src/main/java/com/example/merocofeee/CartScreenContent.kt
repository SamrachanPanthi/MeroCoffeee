package com.example.merocofeee

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.BorderStroke




// --- Data Models ---
data class CartItem(
    val id: Int,
    val name: String,
    val description: String,
    val quantity: Int,
    val price: Int,
    val imageResId: Int
)

data class OptionItemData(
    val title: String,
    val value: String,
    val isDelivery: Boolean = false
)

// --- Mock Data ---
// NOTE: Using standard Android drawables as placeholders (android.R.drawable.X).
// Replace these with your actual R.drawable.coffee_image IDs.
val cartItems = listOf(
    CartItem(1, "LATTE", "Size: Large\nNotes: Extra Shot", 1, 200, android.R.drawable.ic_dialog_map),
    CartItem(2, "ESPRESSO", "Size: Small\nNotes: Decaf", 2, 320, android.R.drawable.ic_dialog_email)
)

val options = listOf(
    OptionItemData("SHIPPING", "Add shipping address"),
    OptionItemData("PAYMENT", "Cash on Delivery (COD)"),
    OptionItemData("PROMOS", "Apply promo code"),
    OptionItemData("DELIVERY", "100", isDelivery = true)
)

// --- Helper Functions ---
private fun calculateSubtotal(items: List<CartItem>): Int {
    return items.sumOf { it.price * it.quantity }
}

private fun calculateTotal(subtotal: Int, options: List<OptionItemData>): Int {
    val deliveryFee = options.firstOrNull { it.isDelivery }?.value?.toIntOrNull() ?: 0
    return subtotal + deliveryFee
}


// ==========================================================
// 2. MAIN COMPOSABLE
// ==========================================================

@Composable
fun CartScreenContent(onBack: () -> Unit = {}) { // Renamed and added optional onBack
    val subtotal = calculateSubtotal(cartItems)
    val total = calculateTotal(subtotal, options)
    val scrollState = rememberScrollState()

    // Scaffold for structure: TopBar, Content, BottomBar
    Scaffold(
        topBar = { MyCartTopAppBar(onBack = onBack) },
        bottomBar = { PlaceOrderBottomBar(total = total) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .background(Color.White)
        ) {
            OptionsSection(options.filterNot { it.isDelivery })

            Divider(thickness = 8.dp, color = Color(0xFFF5F5F5))

            ItemsHeader()
            CartItemsList(cartItems)

            PriceSummary(subtotal = subtotal, options = options)
        }
    }
}




@Composable
fun MyCartTopAppBar(onBack: () -> Unit) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color.White)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
            Text(
                text = "MY CART",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.width(48.dp))
        }
        Divider(color = Color(0xFFEEEEEE), thickness = 1.dp)
    }
}

@Composable
fun OptionItem(data: OptionItemData) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .clickable { /* Handle click */ }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = data.title,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = data.value,
                color = Color.Gray,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Next",
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
        Divider(color = Color(0xFFEEEEEE), thickness = 1.dp, modifier = Modifier.padding(start = 16.dp))
    }
}

@Composable
fun OptionsSection(options: List<OptionItemData>) {
    Column {
        options.forEach { item ->
            OptionItem(item)
        }
    }
}

@Composable
fun ItemsHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "ITEM", fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.width(70.dp))
        Text(text = "DESCRIPTION", fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.weight(1f))
        Text(text = "PRICE", fontWeight = FontWeight.Bold, fontSize = 12.sp, textAlign = TextAlign.End, modifier = Modifier.width(60.dp))
    }
}

@Composable
fun CartItemRow(item: CartItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Image(
            painter = painterResource(id = R.drawable.lattee),

            contentDescription = item.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(70.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFEEEEEE))
        )
        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = item.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
            Text(text = "Qty: ${item.quantity}", color = Color.DarkGray, fontSize = 12.sp)
            Text(text = item.description, color = Color.Gray, fontSize = 12.sp)
        }


        Text(
            text = "rs.${item.price * item.quantity}",
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            textAlign = TextAlign.End,
            color = Color.Black,
            modifier = Modifier.width(60.dp)
        )
    }
}

@Composable
fun CartItemsList(items: List<CartItem>) {
    Column {
        items.forEach { item ->
            CartItemRow(item)
        }
    }
}

@Composable
fun PriceRow(label: String, amount: Int, isTotal: Boolean = false, isDelivery: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = if (isTotal) "Total" else if (isDelivery) label else "$label (${cartItems.size} items)",
            fontWeight = if (isTotal) FontWeight.ExtraBold else FontWeight.SemiBold,
            fontSize = if (isTotal) 18.sp else 16.sp,
            color = Color.Black
        )
        Text(
            text = "rs.$amount",
            fontWeight = if (isTotal) FontWeight.ExtraBold else FontWeight.SemiBold,
            fontSize = if (isTotal) 18.sp else 16.sp,
            color = if (isTotal) Color.Black else Color.DarkGray
        )
    }
}

@Composable
fun PriceSummary(subtotal: Int, options: List<OptionItemData>) {
    Spacer(modifier = Modifier.height(24.dp))
    Divider(thickness = 8.dp, color = Color(0xFFF5F5F5))
    Spacer(modifier = Modifier.height(8.dp))

    PriceRow(label = "Subtotal", amount = subtotal)

    options.filter { it.isDelivery }.forEach { deliveryOption ->
        PriceRow(
            label = deliveryOption.title,
            amount = deliveryOption.value.toIntOrNull() ?: 0,
            isDelivery = true
        )
    }

    Spacer(modifier = Modifier.height(12.dp))
    Divider(color = Color.Black.copy(alpha = 0.5f), thickness = 1.dp, modifier = Modifier.padding(horizontal = 16.dp))
    Spacer(modifier = Modifier.height(12.dp))

    PriceRow(label = "Total", amount = calculateTotal(subtotal, options), isTotal = true)
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun PlaceOrderBottomBar(total: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Button(
            onClick = { /* Handle order placement */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Place order (rs.$total)", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CartScreenContentPreview() {
    CartScreenContent(onBack = {})
}