package com.example.dz.domain.repository

import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.Book
import com.example.dz.domain.model.Category

interface BookRepository {
    suspend fun searchBooks(query: String): AppResult<List<Book>>
    suspend fun getHomeBooks(): AppResult<List<Book>>
    suspend fun getBooksByCategory(categoryId: String): AppResult<List<Book>>
    suspend fun getBookDetails(bookId: String): AppResult<Book>
    suspend fun getCategories(): AppResult<List<Category>>

    /** Fetches the raw plain-text body of a book from its remote [textUrl]. */
    suspend fun getBookText(textUrl: String): AppResult<String>
}
