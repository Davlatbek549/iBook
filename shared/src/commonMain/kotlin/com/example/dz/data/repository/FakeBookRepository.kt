package com.example.dz.data.repository

import com.example.dz.core.error.AppError
import com.example.dz.core.result.AppResult
import com.example.dz.data.fake.FakeData
import com.example.dz.domain.model.Book
import com.example.dz.domain.model.Category
import com.example.dz.domain.repository.BookRepository

class FakeBookRepository : BookRepository {
    override suspend fun searchBooks(query: String): AppResult<List<Book>> {
        val normalizedQuery = query.trim()
        val books = if (normalizedQuery.isBlank()) {
            FakeData.books
        } else {
            FakeData.books.filter { it.title.contains(normalizedQuery, ignoreCase = true) }
        }
        return AppResult.Success(books)
    }

    override suspend fun getHomeBooks(): AppResult<List<Book>> = AppResult.Success(FakeData.books)

    override suspend fun getBooksByCategory(categoryId: String): AppResult<List<Book>> =
        AppResult.Success(
            FakeData.books.filter { book ->
                book.categories.any { category -> category.id == categoryId }
            }.ifEmpty { FakeData.books }
        )

    override suspend fun getBookDetails(bookId: String): AppResult<Book> =
        FakeData.books.firstOrNull { it.id == bookId }
            ?.let { AppResult.Success(it) }
            ?: AppResult.Error(AppError.NotFound)

    override suspend fun getCategories(): AppResult<List<Category>> = AppResult.Success(FakeData.categories)
}
