package com.example.merocofeee.view

import android.R.color.white
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.merocofeee.repository.UserRepoImpl
import com.example.merocofeee.viewmodel.UserViewModel


class AdminPanelActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen()
        }
    }
}

data class NavItem(
    val label: String,
    val icon: ImageVector,
)

val bottomNavItems = listOf(
    NavItem("Home", Icons.Default.Add),
    NavItem("Profile", Icons.Default.Person)
)

@Composable
fun MainScreen() {
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
    val userViewModel = remember { UserViewModel(UserRepoImpl()) }

    Scaffold(
        containerColor = Color.Black,
        bottomBar = {
            NavigationBar(
                containerColor = Color.Black
            ) {
                bottomNavItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        label = { Text(item.label) },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Blue,
                            unselectedIconColor = Color.Gray,
                            selectedTextColor = White,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedIndex) {
//                0 -> DashboardScreen()
//                1 -> NotificationsScreenContent()

            }
        }
    }
}
