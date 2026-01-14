package com.example.merocofeee.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.merocofeee.model.ProductModel


object CartViewModel {

    private val _cartItems = mutableStateListOf<ProductModel>()
    val cartItems: List<ProductModel> = _cartItems

    private val _subtotal = MutableLiveData(0.0)
    val subtotal: LiveData<Double> = _subtotal

    fun addToCart(product: ProductModel) {
        val existingItemIndex = _cartItems.indexOfFirst { it.productId == product.productId }

        if (existingItemIndex != -1) {
            val existingItem = _cartItems[existingItemIndex]
            // Create a new object with the updated quantity
            val updatedItem = existingItem.copy(quantity = existingItem.quantity + 1)
            // Replace the old item with the new one
            _cartItems[existingItemIndex] = updatedItem
        } else {
            // If it's a new item, ensure its quantity is 1
            _cartItems.add(product.copy(quantity = 1))
        }
        recalculateSubtotal()
    }

    fun removeFromCart(product: ProductModel) {
        _cartItems.removeAll { it.productId == product.productId }
        recalculateSubtotal()
    }

    fun increaseQuantity(productId: String) {
        val itemIndex = _cartItems.indexOfFirst { it.productId == productId }
        if (itemIndex != -1) {
            val item = _cartItems[itemIndex]
            // Create a new object with the incremented quantity
            val updatedItem = item.copy(quantity = item.quantity + 1)
            // Replace the item at the specific index to trigger a UI update
            _cartItems[itemIndex] = updatedItem
            recalculateSubtotal()
        }
    }

    fun decreaseQuantity(productId: String) {
        val itemIndex = _cartItems.indexOfFirst { it.productId == productId }
        if (itemIndex != -1) {
            val item = _cartItems[itemIndex]
            if (item.quantity > 1) {
                // Create a new object with the decremented quantity
                val updatedItem = item.copy(quantity = item.quantity - 1)
                // Replace the item to trigger UI update
                _cartItems[itemIndex] = updatedItem
            } else {
                // Remove the item if quantity is 1
                _cartItems.removeAt(itemIndex)
            }
            recalculateSubtotal()
        }
    }

    private fun recalculateSubtotal() {
        _subtotal.value = _cartItems.sumOf { it.price * it.quantity }
    }
}