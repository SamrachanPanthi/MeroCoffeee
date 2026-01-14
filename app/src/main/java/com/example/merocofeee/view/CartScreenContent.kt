package com.example.merocofeee.view

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.merocofeee.model.ProductModel
import com.example.merocofeee.repository.UserRepoImpl
import com.example.merocofeee.viewmodel.CartViewModel
import com.example.merocofeee.viewmodel.UserViewModel


// --- THEME COLORS ---
private val CreamBackground = Color(0xFFFDF5E6)
private val DarkBrown = Color(0xFF4E342E)
private val AccentOrange = Color(0xFFFF9800)
private val SoftGray = Color(0xFF757575)

@Composable
fun Cart(onBack: () -> Unit = {}) {
    val cartItems = CartViewModel.cartItems
    val subtotal by CartViewModel.subtotal.observeAsState(0.0)

    CartScreenStateless(
        cartItems = cartItems,
        subtotal = subtotal,
        onBack = onBack,
        onIncreaseQuantity = { productId -> CartViewModel.increaseQuantity(productId) },
        onDecreaseQuantity = { productId -> CartViewModel.decreaseQuantity(productId) },
        onRemoveItem = { product ->
            CartViewModel.removeFromCart(product)
        }
    )
}

@Composable
fun CartScreenStateless(
    cartItems: List<ProductModel>,
    subtotal: Double,
    onBack: () -> Unit,
    onIncreaseQuantity: (String) -> Unit,
    onDecreaseQuantity: (String) -> Unit,
    onRemoveItem: (ProductModel) -> Unit
) {
    Scaffold(
        topBar = { CartTopAppBar(onBack = onBack) },
        bottomBar = { CheckoutBottomBar(subtotal = subtotal, cartItems = cartItems) },
        containerColor = CreamBackground
    ) { paddingValues ->
        if (cartItems.isEmpty()) {
            EmptyCartView(modifier = Modifier.padding(paddingValues))
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                item {
                    Text(
                        text = "Review Order",
                        style = typography.headlineMedium.copy(fontWeight = FontWeight.Bold, color = DarkBrown),
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }

                items(cartItems) { product ->
                    CartItemRow(
                        product = product,
                        onIncrease = { onIncreaseQuantity(product.productId) },
                        onDecrease = { onDecreaseQuantity(product.productId) },
                        onRemove = { onRemoveItem(product) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item { PriceSummary(subtotal = subtotal) }
            }
        }
    }
}

@Composable
fun CartTopAppBar(onBack: () -> Unit) {
    Surface(shadowElevation = 4.dp, color = CreamBackground) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = DarkBrown)
            }
            Text(
                text = "My Cart",
                style = typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = DarkBrown),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(48.dp)) // Balance the back button
        }
    }
}

@Composable
fun CartItemRow(
    product: ProductModel,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.title,
                placeholder = painterResource(id = R.drawable.img),
                error = painterResource(id = R.drawable.img),
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(80.dp).clip(RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(product.title, style = typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = DarkBrown))
                Spacer(modifier = Modifier.height(4.dp))
                Text("Rs. ${product.price}", style = typography.bodyLarge.copy(color = AccentOrange, fontWeight = FontWeight.SemiBold))
                Spacer(modifier = Modifier.height(8.dp))
                QuantitySelector(quantity = product.quantity, onIncrease = onIncrease, onDecrease = onDecrease)
            }
            IconButton(onClick = onRemove) {
                Icon(Icons.Default.Delete, contentDescription = "Remove", tint = SoftGray)
            }
        }
    }
}

@Composable
fun QuantitySelector(quantity: Int, onIncrease: () -> Unit, onDecrease: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(CreamBackground)
            .border(1.dp, DarkBrown.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
    ) {
        IconButton(onClick = onDecrease, modifier = Modifier.size(36.dp)) {
            Icon(Icons.Default.Remove, contentDescription = "Decrease quantity", tint = DarkBrown)
        }
        Text(
            text = "$quantity",
            style = typography.bodyLarge.copy(fontWeight = FontWeight.Bold, color = DarkBrown),
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        IconButton(onClick = onIncrease, modifier = Modifier.size(36.dp)) {
            Icon(Icons.Default.Add, contentDescription = "Increase quantity", tint = DarkBrown)
        }
    }
}

@Composable
fun PriceSummary(subtotal: Double) {
    val shipping = 100.0
    val total = subtotal + shipping
    Column(modifier = Modifier.padding(top = 32.dp, bottom = 16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Subtotal", style = typography.bodyLarge.copy(color = SoftGray))
            Text("Rs. ${String.format("%.0f", subtotal)}", style = typography.bodyLarge.copy(color = DarkBrown))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Shipping", style = typography.bodyLarge.copy(color = SoftGray))
            Text("Rs. ${String.format("%.0f", shipping)}", style = typography.bodyLarge.copy(color = DarkBrown))
        }
        Divider(modifier = Modifier.padding(vertical = 16.dp), color = SoftGray.copy(alpha = 0.2f))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Total", style = typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = DarkBrown))
            Text("Rs. ${String.format("%.0f", total)}", style = typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = DarkBrown))
        }
    }
}

@Composable
fun CheckoutBottomBar(subtotal: Double, cartItems: List<ProductModel>) {
    val context = LocalContext.current
    val orderViewModel = remember { OrderViewModel() }
    val userViewModel = remember { UserViewModel(UserRepoImpl()) }

    val shipping = 100.0
    val total = subtotal + shipping

    Surface(modifier = Modifier.fillMaxWidth(), color = Color.White, shadowElevation = 8.dp) {
        Button(
            onClick = {
                val userId = userViewModel.getCurrentUser()?.uid ?: ""
                if (userId.isNotEmpty() && cartItems.isNotEmpty()) {
                    val order = OrderModel(
                        items = cartItems,
                        totalPrice = total,
                        user = userId
                    )
                    orderViewModel.placeOrder(order) { success, message ->
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        if (success) {
                            // Important: Clear the cart via the ViewModel
                            cartItems.forEach { CartViewModel.removeFromCart(it) }
                            val intent = Intent(context, OrderPlacedActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            }
                            context.startActivity(intent)
                        }
                    }
                } else {
                    Toast.makeText(context, "Cannot place an empty order.", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp).height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DarkBrown)
        ) {
            Text("Proceed to Checkout (Rs. ${String.format("%.0f", total)})", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Composable
fun EmptyCartView(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(painter = painterResource(id = R.drawable.img), contentDescription = "Empty Cart", modifier = Modifier.size(150.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text("Your Cart is Empty", style = typography.headlineSmall.copy(fontWeight = FontWeight.Bold, color = DarkBrown))
            Text("Looks like you haven\'t added anything to your cart yet.", color = SoftGray, textAlign = TextAlign.Center, modifier = Modifier.padding(horizontal = 32.dp))
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xFFFDF5E6)
@Composable
fun CartScreenWithItemsPreview() {
    val sampleItems = listOf(
        ProductModel(productId = "1", title = "Espresso", price = 250.0, imageUrl = "", quantity = 2),
        ProductModel(productId = "2", title = "Croissant", price = 150.0, imageUrl = "", quantity = 1)
    )
    CartScreenStateless(
        cartItems = sampleItems,
        subtotal = 650.0,
        onBack = {},
        onIncreaseQuantity = {},
        onDecreaseQuantity = {},
        onRemoveItem = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFDF5E6)
@Composable
fun EmptyCartScreenPreview() {
    CartScreenStateless(
        cartItems = emptyList(),
        subtotal = 0.0,
        onBack = {},
        onIncreaseQuantity = {},
        onDecreaseQuantity = {},
        onRemoveItem = {}
    )
}