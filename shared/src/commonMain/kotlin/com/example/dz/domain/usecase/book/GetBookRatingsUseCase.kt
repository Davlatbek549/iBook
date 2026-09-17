package com.example.dz.domain.usecase.book

import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.BookRatings
import com.example.dz.domain.repository.BookRepository

class GetBookRatingsUseCase(
    private val repository: BookRepository
) {
    suspend operator fun invoke(bookId: String): AppResult<BookRatings?> =
        repository.getBookRatings(bookId)
}
