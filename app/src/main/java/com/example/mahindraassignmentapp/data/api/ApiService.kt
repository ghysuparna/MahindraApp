package com.example.mahindraassignmentapp.data.api

import com.example.mahindraassignmentapp.Constants
import com.example.mahindraassignmentapp.data.model.Notification
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("posts")
    suspend fun getNotifications(
        @Query("_page") page: Int = 1,
        @Query("_limit") limit: Int = Constants.ITEM_LIMIT,
        ): Response<List<Notification>>
}
