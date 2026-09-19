package com.example.dz.domain.usecase.review

import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.BookReview
import com.example.dz.domain.repository.ReviewRepository

class GetMyReviewUseCase(private val repository: ReviewRepository) {
    suspend operator fun invoke(bookId: String): AppResult<BookReview?> =
        repository.getReview(bookId)
}

class SaveMyReviewUseCase(private val repository: ReviewRepository) {
    suspend operator fun invoke(bookId: String, stars: Int, note: String): AppResult<BookReview> =
        repository.saveReview(bookId, stars, note)
}

class DeleteMyReviewUseCase(private val repository: ReviewRepository) {
    suspend operator fun invoke(bookId: String): AppResult<Unit> = repository.deleteReview(bookId)
}
