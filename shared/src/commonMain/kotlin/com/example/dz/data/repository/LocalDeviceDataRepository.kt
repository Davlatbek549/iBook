package com.example.dz.data.repository

import com.example.dz.data.local.CollectionLocalDataSource
import com.example.dz.data.local.GoalLocalDataSource
import com.example.dz.data.local.LibraryLocalDataSource
import com.example.dz.data.local.LocalDataSource
import com.example.dz.data.local.ReviewLocalDataSource
import com.example.dz.data.local.file.FileStorage
import com.example.dz.domain.repository.DeviceDataRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class LocalDeviceDataRepository(
    private val library: LibraryLocalDataSource,
    private val collections: CollectionLocalDataSource,
    private val sessions: GoalLocalDataSource,
    private val reviews: ReviewLocalDataSource,
    private val files: FileStorage,
    private val local: LocalDataSource,
    private val io: CoroutineDispatcher = Dispatchers.IO,
) : DeviceDataRepository {

    override suspend fun eraseAccountData() = withContext(io) {
        files.deleteAll()
        library.removeAll()
        collections.deleteAll()
        // What someone read and what they thought of it is as personal as the shelf it sat on.
        sessions.clear()
        reviews.clear()
        local.clearUserData()
    }
}
