package com.example.merocofeee

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.merocofeee.view.*
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginInstrumentedTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<LoginActivity>()

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    // ------------------------------------
    // Test 1: Navigate to Registration
    // ------------------------------------
    @Test
    fun testNavigationToRegistration() {
        composeRule
            .onNodeWithText("Register", useUnmergedTree = true)
            .performClick()

        composeRule.waitUntil(5_000) {
            Intents.getIntents().isNotEmpty()
        }

        Intents.intended(
            hasComponent(RegistrationActivity::class.java.name)
        )
    }

    // ------------------------------------
    // Test 2: Admin Login
    // ------------------------------------
    @Test
    fun testAdminLogin_navigatesToAdminPanel() {
        // Email field (first textfield)
        composeRule.onAllNodes(isFocusable())[0]
            .performTextInput("admin123@gmail.com")

        // Password field (second textfield)
        composeRule.onAllNodes(isFocusable())[1]
            .performTextInput("merocoffee")

        composeRule.onNodeWithText("Sign In", useUnmergedTree = true)
            .performClick()

        composeRule.waitUntil(5_000) {
            Intents.getIntents().isNotEmpty()
        }

        Intents.intended(
            hasComponent(AdminPanelActivity::class.java.name)
        )
    }

    // ------------------------------------
    // Test 3: Normal User Login
    // ------------------------------------
    @Test
    fun testUserLogin_navigatesToDashboard() {
        composeRule.onAllNodes(isFocusable())[0]
            .performTextInput("test@gmail.com")

        composeRule.onAllNodes(isFocusable())[1]
            .performTextInput("123456")

        composeRule.onNodeWithText("Sign In", useUnmergedTree = true)
            .performClick()

        composeRule.waitUntil(5_000) {
            Intents.getIntents().isNotEmpty()
        }
        Intents.intended(
            hasComponent(DashboardActivity2::class.java.name)
        )
    }
}

