package com.example.dz.data.local.db

import app.cash.sqldelight.db.SqlDriver

/**
 * Creates the platform SQL driver backing the local database.
 *
 * Android provides a `Context` to its actual; iOS needs no arguments. Construct the platform actual
 * in platform DI and hand it to [createDatabase].
 */
expect class DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}
