package com.example.dz.data.repository

import com.example.dz.core.error.AppError
import com.example.dz.core.result.AppResult
import com.example.dz.data.local.CollectionLocalDataSource
import com.example.dz.data.local.LibraryLocalDataSource
import com.example.dz.data.sync.ReadingDataSync
import com.example.dz.domain.model.Book
import com.example.dz.domain.model.Collection
import com.example.dz.domain.model.LibraryBook
import com.example.dz.domain.model.ReadingProgress
import com.example.dz.domain.repository.CollectionRepository
import com.example.dz.domain.repository.LibraryRepository

class SyncedLibraryRepository(
    private val sync: ReadingDataSync,
    private val local: LibraryLocalDataSource,
) : LibraryRepository {
    override suspend fun add(book: Book) = sync.add(book)
    override suspend fun getLibraryBooks(): AppResult<List<LibraryBook>> {
        val remote = sync.syncNow(force = false)
        val cached = sync.cached { local.getLibraryBooks() }
        return if (remote is AppResult.Error && cached is AppResult.Success && cached.data.isEmpty()) remote else cached
    }
    override suspend fun getContinueReading(): AppResult<LibraryBook?> {
        val remote = sync.syncNow(force = false)
        val cached = sync.cached { local.getContinueReading() }
        return if (remote is AppResult.Error && cached is AppResult.Success && cached.data == null) remote else cached
    }
    override suspend fun updateReadingProgress(bookId: String, progressPercent: Int): AppResult<ReadingProgress> =
        when (val result = sync.progress(bookId, progressPercent)) {
            is AppResult.Error -> result
            is AppResult.Success -> AppResult.Success(ReadingProgress(bookId, progressPercent = progressPercent.coerceIn(0, 100)))
        }
}

class SyncedCollectionRepository(
    private val sync: ReadingDataSync,
    private val local: CollectionLocalDataSource,
) : CollectionRepository {
    override suspend fun getCollections(): AppResult<List<Collection>> {
        val remote = sync.syncNow(force = false)
        val cached = sync.cached { local.getCollections() }
        return if (remote is AppResult.Error && cached is AppResult.Success && cached.data.isEmpty()) remote else cached
    }
    override suspend fun getCollectionDetails(collectionId: String): AppResult<Collection> {
        sync.syncNow(force = false)
        return when (val result = sync.cached { local.getCollection(collectionId) }) {
            is AppResult.Error -> result
            is AppResult.Success -> result.data?.let { AppResult.Success(it) } ?: AppResult.Error(AppError.NotFound)
        }
    }
    override suspend fun createCollection(title: String) = sync.create(title)
    override suspend fun updateCollection(collection: Collection): AppResult<Collection> =
        when (val result = sync.put(collection)) {
            is AppResult.Error -> result
            is AppResult.Success -> AppResult.Success(collection)
        }
    override suspend fun deleteCollection(collectionId: String) = sync.delete(collectionId)
}
