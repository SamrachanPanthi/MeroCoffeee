package com.example.merocofeee.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// Define the different screens/tabs for navigation
enum class DashboardTab {
    HOME, EXPLORE, CART, NOTIFICATIONS, PROFILE
}

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
    // State variable to track the selected tab
    var selectedTab by remember { mutableStateOf(DashboardTab.HOME) }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    ) { paddingValues ->
        // Display the content based on the selected tab
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (selectedTab) {
                DashboardTab.HOME -> HomeScreenContent()
                DashboardTab.EXPLORE -> ExploreContent()
                DashboardTab.CART -> CartScreenContent()
                DashboardTab.NOTIFICATIONS -> NotificationsScreenContent()
                DashboardTab.PROFILE -> ProfileScreenContent()
            }
        }
    }
}


@Composable
fun BottomNavBar(selectedTab: DashboardTab, onTabSelected: (DashboardTab) -> Unit) {
    NavigationBar(
        containerColor = Color.White,
        modifier = Modifier.height(70.dp),
        tonalElevation = 1.dp
    ) {
        val navItems = listOf(
            Triple(Icons.Default.Home, DashboardTab.HOME, "Home"),
            Triple(Icons.Default.LocationOn, DashboardTab.EXPLORE, "Explore"),
            Triple(Icons.Default.ShoppingCart, DashboardTab.CART, "Cart"),
            Triple(Icons.Default.Notifications, DashboardTab.NOTIFICATIONS, "Notifications"),
            Triple(Icons.Default.Person, DashboardTab.PROFILE, "Profile"),
        )

        navItems.forEach { (icon, tab, label) ->
            NavigationBarItem(
                onClick = { onTabSelected(tab) },
                selected = selectedTab == tab,
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = if (selectedTab == tab) Color.Black else Color.Gray
                    )
                },
                colors = NavigationBarItemDefaults.colors(indicatorColor = Color.White)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    DashboardScreen()
}