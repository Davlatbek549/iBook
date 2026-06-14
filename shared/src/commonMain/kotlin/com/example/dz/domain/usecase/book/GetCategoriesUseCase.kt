package com.example.dz.domain.usecase.book

import com.example.dz.domain.repository.BookRepository

class GetCategoriesUseCase(
    private val repository: BookRepository
) {
    suspend operator fun invoke() = repository.getCategories()
}
