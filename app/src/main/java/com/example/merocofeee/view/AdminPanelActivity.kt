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
            AdminScreen()
        }
    }
}

// Renamed data class
data class DashboardNavItem(
    val title: String,
    val icon: ImageVector
)

// Renamed nav items list
val dashboardBottomNavItems = listOf(
    DashboardNavItem("Dashboard", Icons.Default.Home),
    DashboardNavItem("Account", Icons.Default.Person)
)

@Composable
fun AdminScreen() {

    // Renamed state variable
    var currentTabIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    // Renamed ViewModel instance
    val adminUserViewModel = remember {
        UserViewModel(UserRepoImpl())
    }

    Scaffold(
        containerColor = Color.Black,
        bottomBar = {
            NavigationBar(
                containerColor = Color.Black
            ) {
                dashboardBottomNavItems.forEachIndexed { index, navItem ->
                    NavigationBarItem(
                        label = {
                            Text(navItem.title)
                        },
                        icon = {
                            Icon(
                                navItem.icon,
                                contentDescription = navItem.title
                            )
                        },
                        selected = currentTabIndex == index,
                        onClick = {
                            currentTabIndex = index
                        },
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

        Box(
            modifier = Modifier.padding(paddingValues)
        ) {
            when (currentTabIndex) {
                0 -> SalesRecordScreen()
                //1 -> AdminProfileScreen()
            }
        }
    }
}
