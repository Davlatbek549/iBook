package com.example.dz.data.repository

import com.example.dz.core.error.AppError
import com.example.dz.core.result.AppResult
import com.example.dz.data.local.LocalDataSource
import com.example.dz.domain.model.AppNotification
import com.example.dz.domain.repository.NotificationRepository

class NotificationRepositoryImpl(private val local: LocalDataSource) : NotificationRepository {

    override suspend fun getNotifications(): AppResult<List<AppNotification>> {
        return AppResult.Success(emptyList())
    }

    override suspend fun markAsRead(notificationId: String): AppResult<AppNotification> {
        local.saveSetting("notif_read_$notificationId", "true")
        return AppResult.Error(AppError.NotFound)
    }
}
