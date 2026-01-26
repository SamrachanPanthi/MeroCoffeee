package com.example.merocofeee.repository

import com.example.merocofeee.model.OrderModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.collections.mapNotNull
import kotlin.collections.reversed
import kotlin.jvm.java
import kotlin.run

class OrderRepoImpl : OrderRepo {

    private val salesRef = FirebaseDatabase.getInstance().getReference("sales")

    override fun placeOrder(order: OrderModel, callback: (Boolean, String) -> Unit) {
        val newOrderRef = salesRef.push()
        val orderId = newOrderRef.key ?: run {
            callback(false, "Could not generate order ID.")
            return
        }

        newOrderRef.setValue(order.copy(orderId = orderId))
            .addOnSuccessListener { callback(true, "Order placed successfully!") }
            .addOnFailureListener { callback(false, it.message ?: "Failed to place order.") }
    }

    override fun getAllSales(callback: (Boolean, String, List<OrderModel>?) -> Unit) {
        salesRef.orderByChild("timestamp").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val sales = snapshot.children.mapNotNull { it.getValue(OrderModel::class.java) }
                callback(true, "Sales fetched successfully", sales.reversed()) // Show newest first
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, error.message, null)
            }
        })
    }
}