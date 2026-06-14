package com.example.dz.domain.usecase.book

import com.example.dz.domain.repository.BookRepository

class SearchBooksUseCase(
    private val repository: BookRepository
) {
    suspend operator fun invoke(query: String) = repository.searchBooks(query)
}
