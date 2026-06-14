package com.example.dz.domain.usecase.book

import com.example.dz.domain.repository.BookRepository

class GetBooksByCategoryUseCase(
    private val repository: BookRepository
) {
    suspend operator fun invoke(categoryId: String) = repository.getBooksByCategory(categoryId)
}
