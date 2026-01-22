package com.example.merocofeee.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle

import androidx.compose.material.icons.filled.Home

import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext

class AdminPanelActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AdminMainScreen()
        }
    }
}

data class AdminNavItem(
    val label: String,
    val icon: ImageVector,
    val isActivity: Boolean = false // Flag to check if it should launch an Activity
)

val adminNavItems = listOf(
    AdminNavItem("Sales", Icons.Default.Home),
    AdminNavItem("Orders", Icons.Default.ShoppingCart),
    AdminNavItem("Add Product", Icons.Default.AddCircle, isActivity = true)
)

@Composable
fun AdminMainScreen() {
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
    val context = LocalContext.current

    Scaffold(
        containerColor = Color(0xFFFDF5E6), // Using the light theme background
        bottomBar = {
            NavigationBar(
                containerColor = Color.White
            ) {
                adminNavItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        label = { Text(item.label) },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        // Only the composable screen can be 'selected'.
                        selected = !item.isActivity && selectedIndex == index,
                        onClick = {
                            if (item.isActivity) {
                                // If it's an activity, launch it directly
                                context.startActivity(Intent(context, AddProductActivity::class.java))
                            } else {
                                // Otherwise, switch the displayed composable
                                selectedIndex = index
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF4E342E), // DarkBrown
                            unselectedIconColor = Color.Gray,
                            selectedTextColor = Color(0xFF4E342E),
                            unselectedTextColor = Color.Gray,
                            indicatorColor = Color(0xFFFDF5E6) // CreamBackground
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            // The when block now handles the new Orders screen
            when (selectedIndex) {
                0 -> SalesRecordScreen()
                1 -> AdminOrdersScreen()

            }
        }
    }
}
