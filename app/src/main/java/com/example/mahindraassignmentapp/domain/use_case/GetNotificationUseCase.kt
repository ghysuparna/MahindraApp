package com.example.mahindraassignmentapp.domain.use_case

import com.example.mahindraassignmentapp.data.model.Notification
import com.example.mahindraassignmentapp.domain.repository.NotificationRepository

class GetNotificationUseCase(
    private val repo : NotificationRepository
) {
    suspend fun execute(pageNo:Int):List<Notification> =repo.getNotification(pageNo)
}