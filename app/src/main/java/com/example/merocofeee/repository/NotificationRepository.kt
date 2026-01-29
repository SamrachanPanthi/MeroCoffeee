package com.example.merocofeee.repository

import com.example.merocofeee.model.AppNotificationModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class NotificationRepository {
    private val database = FirebaseDatabase.getInstance().getReference("notifications")

    fun getNotifications(
        userId: String,
        onResult: (List<AppNotificationModel>) -> Unit
    ) {
        database.child(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val notifications = snapshot.children.mapNotNull { it.getValue(AppNotificationModel::class.java) }
                onResult(notifications)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
}
