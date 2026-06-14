package com.example.dz.domain.repository

import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.AppNotification

interface NotificationRepository {
    suspend fun getNotifications(): AppResult<List<AppNotification>>
    suspend fun markAsRead(notificationId: String): AppResult<AppNotification>
}
