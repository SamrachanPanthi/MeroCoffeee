package com.example.merocofeee

import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun email_is_valid() {
        val email = "user@gmail.com"
        assertTrue(email.contains("@"))
    }

    @Test
    fun password_is_not_empty() {
        val password = "123456"
        assertTrue(password.isNotEmpty())
    }
}
