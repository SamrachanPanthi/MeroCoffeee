package com.example.merocofeee.repository

import com.example.merocofeee.model.AppNotification
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class NotificationRepository {
    private val database = FirebaseDatabase.getInstance().getReference("notifications")

