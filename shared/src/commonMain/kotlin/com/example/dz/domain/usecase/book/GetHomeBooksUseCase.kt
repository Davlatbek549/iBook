package com.example.dz.domain.usecase.book

import com.example.dz.domain.repository.BookRepository

class GetHomeBooksUseCase(
    private val repository: BookRepository
) {
    suspend operator fun invoke() = repository.getHomeBooks()
}
