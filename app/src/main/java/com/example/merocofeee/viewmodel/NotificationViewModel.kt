package com.example.merocofeee.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.merocofeee.model.AppNotification
import com.example.merocofeee.repository.NotificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotificationViewModel : ViewModel() {

    private val repository = NotificationRepository()

    private val _notifications = MutableStateFlow<List<AppNotification>>(emptyList())
    val notifications: StateFlow<List<AppNotification>> = _notifications

    fun loadNotifications(userId: String) {
        viewModelScope.launch {
            repository.getNotifications(userId) { list ->
                _notifications.value = list.sortedByDescending { it.timestamp }
            }
        }
    }
}
