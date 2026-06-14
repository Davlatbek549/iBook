package com.example.dz.domain.usecase.library

import com.example.dz.domain.repository.LibraryRepository

class GetContinueReadingUseCase(
    private val repository: LibraryRepository
) {
    suspend operator fun invoke() = repository.getContinueReading()
}
