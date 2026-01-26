package com.example.merocofeee.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.merocofeee.model.OrderModel
import com.example.merocofeee.repository.OrderRepo
import com.example.merocofeee.repository.OrderRepoImpl

class OrderViewModel : ViewModel() {

    private val orderRepo: OrderRepo = OrderRepoImpl()

    private val _sales = MutableLiveData<List<OrderModel>>()
    val sales: LiveData<List<OrderModel>> = _sales

    fun placeOrder(order: OrderModel, callback: (Boolean, String) -> Unit) {
        orderRepo.placeOrder(order, callback)
    }

    fun fetchAllSales() {
        orderRepo.getAllSales { success, _, salesList ->
            if (success) {
                _sales.postValue(salesList ?: emptyList())
            }
        }
    }
}