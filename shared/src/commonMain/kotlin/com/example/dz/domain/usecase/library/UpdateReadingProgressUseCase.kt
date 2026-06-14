package com.example.dz.domain.usecase.library

import com.example.dz.domain.repository.LibraryRepository

class UpdateReadingProgressUseCase(
    private val repository: LibraryRepository
) {
    suspend operator fun invoke(bookId: String, progressPercent: Int) =
        repository.updateReadingProgress(bookId, progressPercent)
}
