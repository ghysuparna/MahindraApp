package com.example.mahindraassignmentapp.data.repository

import com.example.mahindraassignmentapp.data.model.Notification
import retrofit2.Response

interface NotifRemoteDataSource {
    suspend fun getNotification(pageNo:Int): Response<List<Notification>>
}