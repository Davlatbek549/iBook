package com.example.dz.data.local.db

import com.example.dz.database.DzDatabase

/** File name of the on-device SQLite database. */
internal const val DATABASE_NAME = "dz.db"

/** Builds the SQLDelight [DzDatabase] from a platform [DatabaseDriverFactory]. */
fun createDatabase(driverFactory: DatabaseDriverFactory): DzDatabase =
    DzDatabase(driverFactory.createDriver())
