package com.example.mahindraassignmentapp.data.repository

import com.example.mahindraassignmentapp.data.model.Notification
import com.example.mahindraassignmentapp.domain.repository.NotificationRepository

class NotifRepositoryImpl(private val remoteDataSource: NotifRemoteDataSource):NotificationRepository {

    override suspend fun getNotification(
        pageNo: Int
    ): List<Notification> {

        val response = remoteDataSource.getNotification(pageNo)

        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception(
                response.errorBody()?.string()
                    ?: response.message()
            )
        }
    }

}