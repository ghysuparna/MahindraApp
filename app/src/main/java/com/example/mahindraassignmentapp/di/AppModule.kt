package com.example.mahindraassignmentapp.di

import android.app.Application
import android.content.Context
import com.example.mahindraassignmentapp.Constants
import com.example.mahindraassignmentapp.data.api.ApiClient
import com.example.mahindraassignmentapp.data.api.ApiService
import com.example.mahindraassignmentapp.data.repository.NotifRemoteDataSource
import com.example.mahindraassignmentapp.data.repository.NotifRemoteDataSourceImpl
import com.example.mahindraassignmentapp.data.repository.NotifRepositoryImpl
import com.example.mahindraassignmentapp.domain.repository.NotificationRepository
import com.example.mahindraassignmentapp.domain.use_case.GetNotificationUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideRetrofit(
        @ApplicationContext
        context: Context
    ): Retrofit {

        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(ApiClient.makeOkHttpClient())
            .build()
    }

    @Provides
    @Singleton
    internal fun provideContext(application: Application): Context = application


    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideNotifRemoteDataSource(
        apiService: ApiService
    ): NotifRemoteDataSource {
        return NotifRemoteDataSourceImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideNotificationRepository(
        remoteDataSource: NotifRemoteDataSource
    ): NotificationRepository {
        return NotifRepositoryImpl(remoteDataSource)
    }

    @Provides
    fun provideGetNotificationUseCase(
        repository: NotificationRepository
    ): GetNotificationUseCase {
        return GetNotificationUseCase(repository)
    }
}