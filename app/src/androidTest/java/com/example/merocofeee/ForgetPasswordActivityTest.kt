package com.example.merocofeee

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.merocofeee.view.ForgetPasswordActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ForgetPasswordActivityTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ForgetPasswordActivity>()

    // 1️⃣ Screen loads successfully
    @Test
    fun screenLoadsSuccessfully() {
        composeTestRule
            .onNodeWithText("Forgot Password")
            .assertIsDisplayed()
    }

    // 2️⃣ Email field is visible
    @Test
    fun emailFieldExists() {
        composeTestRule
            .onNodeWithText("Email")
            .assertIsDisplayed()
    }

    // 3️⃣ Button is visible
    @Test
    fun sendResetButtonExists() {
        composeTestRule
            .onNodeWithText("Send Reset Link")
            .assertIsDisplayed()
    }

    // 4️⃣ Clicking button with empty email does not crash
    @Test
    fun emptyEmailClickDoesNotCrash() {
        composeTestRule
            .onNodeWithText("Send Reset Link")
            .performClick()

        composeTestRule.waitForIdle()
    }

    // 5️⃣ Return to login is clickable
    @Test
    fun returnBackToLoginClickable() {
        composeTestRule
            .onNodeWithText("Return back to login")
            .performClick()

        composeTestRule.waitForIdle()
    }
}
