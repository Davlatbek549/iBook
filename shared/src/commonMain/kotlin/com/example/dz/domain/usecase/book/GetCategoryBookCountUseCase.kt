package com.example.dz.domain.usecase.book

import com.example.dz.domain.repository.BookRepository

class GetCategoryBookCountUseCase(
    private val repository: BookRepository
) {
    suspend operator fun invoke(categoryId: String) = repository.getCategoryBookCount(categoryId)
}
