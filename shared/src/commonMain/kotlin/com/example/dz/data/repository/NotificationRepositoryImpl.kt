package com.example.dz.data.repository

import com.example.dz.core.error.AppError
import com.example.dz.core.result.AppResult
import com.example.dz.data.fake.FakeData
import com.example.dz.data.local.LocalDataSource
import com.example.dz.domain.model.AppNotification
import com.example.dz.domain.repository.NotificationRepository

class NotificationRepositoryImpl(private val local: LocalDataSource) : NotificationRepository {

    override suspend fun getNotifications(): AppResult<List<AppNotification>> {
        val notifications = FakeData.notifications.map { notification ->
            val isRead = local.getSetting("notif_read_${notification.id}") == "true"
            notification.copy(isRead = isRead)
        }
        return AppResult.Success(notifications)
    }

    override suspend fun markAsRead(notificationId: String): AppResult<AppNotification> {
        local.saveSetting("notif_read_$notificationId", "true")
        return FakeData.notifications
            .firstOrNull { it.id == notificationId }
            ?.copy(isRead = true)
            ?.let { AppResult.Success(it) }
            ?: AppResult.Error(AppError.NotFound)
    }
}
