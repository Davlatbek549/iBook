package com.example.dz.domain.usecase.book

import com.example.dz.domain.repository.DownloadRepository

/** Removes a book's offline download (file + database record). */
class DeleteDownloadUseCase(
    private val downloadRepository: DownloadRepository
) {
    suspend operator fun invoke(bookId: String) = downloadRepository.deleteDownload(bookId)
}
