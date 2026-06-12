package com.example.mahindraassignmentapp.domain.repository

import com.example.mahindraassignmentapp.data.model.Notification

interface NotificationRepository {
    suspend fun getNotification(pageNo:Int):List<Notification>
}