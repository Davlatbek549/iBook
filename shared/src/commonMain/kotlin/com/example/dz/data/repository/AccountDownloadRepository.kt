package com.example.dz.data.repository

import com.example.dz.core.result.AppResult
import com.example.dz.data.sync.ReadingDataSync
import com.example.dz.domain.model.DownloadedContent
import com.example.dz.domain.repository.DownloadRepository

/** Shares the cache/account lock with sync; a download cannot be lost during a cache refresh. */
class AccountDownloadRepository(
    private val delegate: DownloadRepository,
    private val sync: ReadingDataSync,
) : DownloadRepository {
    override suspend fun downloadBook(bookId: String): AppResult<Unit> =
        flatten(sync.cached { delegate.downloadBook(bookId) }).also { sync.requestSync() }
    override suspend fun deleteDownload(bookId: String): AppResult<Unit> =
        flatten(sync.cached { delegate.deleteDownload(bookId) })
    override suspend fun isDownloaded(bookId: String): Boolean =
        (sync.cached { delegate.isDownloaded(bookId) } as? AppResult.Success)?.data ?: false
    override suspend fun getDownloadedContent(bookId: String): DownloadedContent? =
        (sync.cached { delegate.getDownloadedContent(bookId) } as? AppResult.Success)?.data

    private fun <T> flatten(result: AppResult<AppResult<T>>): AppResult<T> = when (result) {
        is AppResult.Success -> result.data
        is AppResult.Error -> result
    }
}
