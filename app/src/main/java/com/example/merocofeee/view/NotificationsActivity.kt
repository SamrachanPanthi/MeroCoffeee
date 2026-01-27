package com.example.merocofeee.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.merocofeee.ui.theme.MeroCofeeeTheme

class NotificationActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MeroCofeeeTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    NotificationsScreenContent()
                }
            }
        }
    }
}
