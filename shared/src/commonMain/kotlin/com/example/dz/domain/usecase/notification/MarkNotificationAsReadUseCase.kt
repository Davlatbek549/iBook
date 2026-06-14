package com.example.dz.domain.usecase.notification

import com.example.dz.domain.repository.NotificationRepository

class MarkNotificationAsReadUseCase(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(notificationId: String) = repository.markAsRead(notificationId)
}
