package com.example.merocofeee.view

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
import androidx.compose.foundation.lazy.items
import com.example.merocofeee.R


import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

import com.example.merocofeee.model.OrderModel
import com.example.merocofeee.viewmodel.OrderViewModel
import kotlin.collections.forEach
import kotlin.text.format

// --- THEME COLORS ---
private val CreamBackground = Color(0xFFFDF5E6)
private val DarkBrown = Color(0xFF4E342E)
private val SoftGray = Color(0xFF757575)

@Composable
fun AdminOrdersScreen() {
    val orderViewModel: OrderViewModel = remember { OrderViewModel() }
    val orders by orderViewModel.sales.observeAsState(emptyList())
    val lifecycleOwner = LocalLifecycleOwner.current

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
        topBar = { AdminOrdersTopAppBar() },
        containerColor = CreamBackground
    ) { paddingValues ->
        if (orders.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text("No orders yet.", style = MaterialTheme.typography.bodyLarge.copy(color = SoftGray))
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(orders) {
                    AdminOrderCard(order = it)
                }
            }
        }
    }
}

@Composable
fun AdminOrdersTopAppBar() {
    Surface(shadowElevation = 4.dp, color = CreamBackground) {
        Text(
            text = "Incoming Orders",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold, color = DarkBrown),
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        )
    }
}

@Composable
fun AdminOrderCard(order: OrderModel) {
    Card(
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(order.orderId, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = DarkBrown))
               // Text(formatTimestamp(order.timestamp as? Long ?: 0L), style = MaterialTheme.typography.bodySmall.copy(color = com.example.merocoffee.view.SoftGray))
                        }
            Spacer(modifier = Modifier.height(4.dp))
            Text("User: ${order.user}", style = MaterialTheme.typography.bodyMedium.copy(color = SoftGray))
            Spacer(modifier = Modifier.height(12.dp))

            //order.items.forEach {
                //Text("• ${it.quantity}x ${it.title}")
           // }

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(painter = painterResource(R.drawable.baseline_schedule_24), contentDescription = "Pickup Time", tint = DarkBrown, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Pickup: ${order.pickupTime}", fontWeight = FontWeight.SemiBold, color = DarkBrown)
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text("Total: Rs. ${String.format("%.0f", order.totalPrice)}", style = MaterialTheme.typography.titleMedium.copy(color = DarkBrown, fontWeight = FontWeight.ExtraBold))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdminOrdersScreenPreview() {
    AdminOrdersScreen()
}
