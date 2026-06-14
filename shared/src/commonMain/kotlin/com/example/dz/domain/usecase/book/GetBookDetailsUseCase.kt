package com.example.dz.domain.usecase.book

import com.example.dz.domain.repository.BookRepository

class GetBookDetailsUseCase(
    private val repository: BookRepository
) {
    suspend operator fun invoke(bookId: String) = repository.getBookDetails(bookId)
}
