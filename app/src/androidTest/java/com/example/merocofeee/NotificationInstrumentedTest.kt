package com.example.merocofeee

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.merocofeee.view.NotificationActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NotificationInstrumentedTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<NotificationActivity>()

    // ----------------------------------------
    // Test 1: Screen loads correctly
    // ----------------------------------------
    @Test
    fun testNotificationScreenLoads() {
        composeRule
            .onNodeWithText("Notifications")
            .assertIsDisplayed()
    }

    // ----------------------------------------
    // Test 2: Empty state text is shown
    // (safe even if Firebase fails)
    // ----------------------------------------
    @Test
    fun testEmptyNotificationMessageShown() {
        composeRule
            .onNodeWithText("No notifications yet")
            .assertIsDisplayed()
    }
}
