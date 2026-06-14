package com.example.dz.data.repository

import com.example.dz.core.error.AppError
import com.example.dz.core.result.AppResult
import com.example.dz.data.fake.FakeData
import com.example.dz.domain.model.AppNotification
import com.example.dz.domain.repository.NotificationRepository

class FakeNotificationRepository : NotificationRepository {
    override suspend fun getNotifications(): AppResult<List<AppNotification>> =
        AppResult.Success(FakeData.notifications)

    override suspend fun markAsRead(notificationId: String): AppResult<AppNotification> =
        FakeData.notifications.firstOrNull { it.id == notificationId }
            ?.copy(isRead = true)
            ?.let { AppResult.Success(it) }
            ?: AppResult.Error(AppError.NotFound)
}
