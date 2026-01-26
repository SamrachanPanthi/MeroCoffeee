package com.example.merocofeee.model

import com.google.firebase.database.ServerValue

data class OrderModel(
    val orderId: String = "",
    val items: List<ProductModel> = emptyList(),
    val totalPrice: Double = 0.0,
    val timestamp: Any = ServerValue.TIMESTAMP, // Automatically set by Firebase
    val user: String = "", // ID of the user who made the purchase
    val pickupTime: String = " "
)
