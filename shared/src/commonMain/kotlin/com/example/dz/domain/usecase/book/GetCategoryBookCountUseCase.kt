package com.example.dz.domain.usecase.book

import com.example.dz.core.result.AppResult
import com.example.dz.domain.repository.BookRepository

/** No count endpoint exists, so this is the real list's size — one real network call, same as the list itself. */
class GetCategoryBookCountUseCase(
    private val repository: BookRepository
) {
    suspend operator fun invoke(categoryId: String): AppResult<Int> =
        when (val result = repository.getBooksByCategory(categoryId)) {
            is AppResult.Success -> AppResult.Success(result.data.size)
            is AppResult.Error -> result
        }
}
