package com.example.merocofeee.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.merocofeee.viewmodel.OrderViewModel
import com.example.merocofeee.model.OrderModel
import com.example.merocofeee.model.ProductModel

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// --- THEME COLORS ---
private val CreamBackground = Color(0xFFFDF5E6)
private val DarkBrown = Color(0xFF4E342E)
private val SoftGray = Color(0xFF757575)

// ==========================================================
// MAIN SALES RECORD SCREEN
// ==========================================================
@Composable
fun SalesRecordScreen() {
    val orderViewModel: OrderViewModel = remember { OrderViewModel() }
    val sales by orderViewModel.sales.observeAsState(emptyList())
    val lifecycleOwner = LocalLifecycleOwner.current

    // Calculate TOTAL SALES AMOUNT
    val totalSalesAmount = sales.sumOf { it.totalPrice }

    // Refresh sales when screen resumes
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                orderViewModel.fetchAllSales()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Scaffold(
        topBar = { SalesTopAppBar() },
        containerColor = CreamBackground
    ) { paddingValues ->

        if (sales.isEmpty()) {
            EmptySalesView(modifier = Modifier.padding(paddingValues))
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {

                // ===== TOTAL SALES SUMMARY CARD =====
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Total Sales",
                            style = MaterialTheme.typography.titleMedium,
                            color = SoftGray
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Rs. ${String.format("%.0f", totalSalesAmount)}",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = DarkBrown
                            )
                        )
                    }
                }

                // ===== SALES LIST =====
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 120.dp,
                        bottom = 16.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(sales) { sale ->
                        SaleCard(order = sale)
                    }
                }
            }
        }
    }
}

// ==========================================================
// UI COMPONENTS
// ==========================================================

@Composable
fun SalesTopAppBar() {
    Surface(shadowElevation = 4.dp, color = CreamBackground) {
        Text(
            text = "Sales Records",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = DarkBrown
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

@Composable
fun SaleCard(order: OrderModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = order.orderId,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = DarkBrown
                    )
                )
                Text(
                    text = formatTimestamp(order.timestamp as? Long ?: 0L),
                    style = MaterialTheme.typography.bodySmall.copy(color = SoftGray)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Total: Rs. ${String.format("%.0f", order.totalPrice)}",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = DarkBrown
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Items
            order.items.forEach {
                Text(
                    text = "• ${it.quantity} x ${it.title}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Pickup Time (if available)
            if (order.pickupTime.isNotBlank()) {
                Text(
                    text = "Pickup Time: ${order.pickupTime}",
                    style = MaterialTheme.typography.bodySmall.copy(color = SoftGray)
                )
            }
        }
    }
}

@Composable
fun EmptySalesView(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No sales records yet.",
            style = MaterialTheme.typography.bodyLarge.copy(color = SoftGray)
        )
    }
}

// ==========================================================
// UTIL
// ==========================================================

fun formatTimestamp(timestamp: Long): String {
    if (timestamp == 0L) return ""
    val date = Date(timestamp)
    val format = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    return format.format(date)
}

// ==========================================================
// PREVIEW
// ==========================================================

@Preview(showBackground = true, backgroundColor = 0xFFFDF5E6)
@Composable
fun SalesRecordScreenPreview() {
    val sales = listOf(
        OrderModel(
            orderId = "ORDER-101",
            items = listOf(
                ProductModel(title = "Espresso", quantity = 2, price = 250.0),
                ProductModel(title = "Croissant", quantity = 1, price = 150.0)
            ),
            totalPrice = 650.0,
            pickupTime = "10:30 AM",
            timestamp = System.currentTimeMillis()
        )
    )

    MaterialTheme {
        LazyColumn {
            items(sales) { SaleCard(it) }
        }
    }
}
