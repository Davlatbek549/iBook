package com.example.dz.domain.usecase.book

import com.example.dz.domain.repository.DownloadRepository

/** Downloads a book for offline reading and records it in the library. */
class DownloadBookUseCase(
    private val downloadRepository: DownloadRepository
) {
    suspend operator fun invoke(bookId: String) = downloadRepository.downloadBook(bookId)
}
