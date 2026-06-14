package com.example.dz.domain.usecase.notification

import com.example.dz.domain.repository.NotificationRepository

class GetNotificationsUseCase(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke() = repository.getNotifications()
}
