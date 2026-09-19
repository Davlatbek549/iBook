package com.example.dz.domain.usecase.library

import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.Book
import com.example.dz.domain.repository.LibraryRepository

/**
 * Puts a book on the reader's shelf the moment they open it.
 *
 * Opening a book is what makes it one of yours — it is what "Reading" on the shelf means, and what
 * the home screen's continue-reading card is looking for. Until this ran, a book only ever reached
 * the shelf by being downloaded, so reading one left no trace anywhere and the progress the reader
 * wrote each time a page turned updated a row that did not exist.
 */
class AddToLibraryUseCase(
    private val repository: LibraryRepository
) {
    suspend operator fun invoke(book: Book): AppResult<Unit> = repository.add(book)
}
