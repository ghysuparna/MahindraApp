package com.example.mahindraassignmentapp.data.repository

import com.example.mahindraassignmentapp.data.api.ApiService
import com.example.mahindraassignmentapp.data.model.Notification
import retrofit2.Response

class NotifRemoteDataSourceImpl(
    private val apiService: ApiService
) : NotifRemoteDataSource {
    override suspend fun getNotification(
        pageNo: Int): Response<List<Notification>> {
        return apiService.getNotifications(pageNo)
    }
}