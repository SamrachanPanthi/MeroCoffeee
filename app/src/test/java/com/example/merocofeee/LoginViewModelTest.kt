package com.example.merocofeee

import org.junit.Assert.*
import org.junit.Test

class LoginViewModelTest {

    @Test
    fun validEmailAndPassword_returnsTrue() {
        val email = "test@gmail.com"
        val password = "12345678"

        val result = email.contains("@") && password.length >= 6

        assertTrue(result)
    }

    @Test
    fun invalidEmail_returnsFalse() {
        val email = "testgmail.com"
        val password = "12345678"

        val result = email.contains("@") && password.length >= 6

        assertFalse(result)
    }
}
