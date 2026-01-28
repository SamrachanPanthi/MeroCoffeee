package com.example.merocofeee.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.merocofeee.model.ProductModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CartViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var product1: ProductModel
    private lateinit var product2: ProductModel

    @Before
    fun setup() {
        // Clear cart before every test
        CartViewModel.cartItems.toMutableList().forEach {
            CartViewModel.removeFromCart(it)
        }

        product1 = ProductModel(
            productId = "1",
            title = "Latte",
            price = 100.0,
            quantity = 1,
            imageUrl = ""
        )

        product2 = ProductModel(
            productId = "2",
            title = "Cappuccino",
            price = 150.0,
            quantity = 1,
            imageUrl = ""
        )
    }

    @Test
    fun addToCart_addsNewItem() {
        CartViewModel.addToCart(product1)

        assertEquals(1, CartViewModel.cartItems.size)
        assertEquals("Latte", CartViewModel.cartItems[0].title)
    }

    @Test
    fun addToCart_increasesQuantityIfItemExists() {
        CartViewModel.addToCart(product1)
        CartViewModel.addToCart(product1)

        val item = CartViewModel.cartItems[0]
        assertEquals(2, item.quantity)
    }

    @Test
    fun removeFromCart_removesItem() {
        CartViewModel.addToCart(product1)
        CartViewModel.addToCart(product2)

        CartViewModel.removeFromCart(product1)

        assertEquals(1, CartViewModel.cartItems.size)
        assertEquals("Cappuccino", CartViewModel.cartItems[0].title)
    }

    @Test
    fun increaseQuantity_updatesQuantity() {
        CartViewModel.addToCart(product1)

        CartViewModel.increaseQuantity("1")

        assertEquals(2, CartViewModel.cartItems[0].quantity)
    }

    @Test
    fun decreaseQuantity_removesItemIfQuantityOne() {
        CartViewModel.addToCart(product1)

        CartViewModel.decreaseQuantity("1")

        assertEquals(0, CartViewModel.cartItems.size)
    }

    @Test
    fun subtotal_calculatedCorrectly() {
        CartViewModel.addToCart(product1) // 100
        CartViewModel.addToCart(product2) // 150

        val subtotal = CartViewModel.subtotal.value

        assertEquals(250.0, subtotal)
    }
}
