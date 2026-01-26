package com.example.merocofeee.repository

import com.example.merocofeee.model.OrderModel

interface OrderRepo {

    fun placeOrder(order: OrderModel, callback: (Boolean, String) -> Unit)


    fun getAllSales(callback: (Boolean, String, List<OrderModel>?) -> Unit)
}
