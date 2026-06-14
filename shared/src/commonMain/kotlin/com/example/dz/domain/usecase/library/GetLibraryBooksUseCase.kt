package com.example.dz.domain.usecase.library

import com.example.dz.domain.repository.LibraryRepository

class GetLibraryBooksUseCase(
    private val repository: LibraryRepository
) {
    suspend operator fun invoke() = repository.getLibraryBooks()
}
