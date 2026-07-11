package com.example.dz.data.local

import com.example.dz.database.DzDatabase
import com.example.dz.domain.model.Author
import com.example.dz.domain.model.Book
import com.example.dz.domain.model.Collection

/**
 * Local persistence for user collections, backed by SQLDelight. Collection membership is stored in
 * a join table with denormalized book display fields, so a collection renders without the library.
 */
class CollectionLocalDataSource(private val database: DzDatabase) {
    private val collectionQueries = database.collectionQueries
    private val bookQueries = database.collectionBookQueries

    fun getCollections(): List<Collection> =
        collectionQueries.selectAll().executeAsList().map { row ->
            Collection(
                id = row.id,
                title = row.title,
                description = row.description,
                books = booksFor(row.id)
            )
        }

    fun getCollection(collectionId: String): Collection? {
        val row = collectionQueries.selectById(collectionId).executeAsOneOrNull() ?: return null
        return Collection(
            id = row.id,
            title = row.title,
            description = row.description,
            books = booksFor(row.id)
        )
    }

    /** Creates a collection and its book membership atomically. [createdAt] is epoch millis. */
    fun create(collection: Collection, createdAt: Long) {
        database.transaction {
            collectionQueries.insert(
                id = collection.id,
                title = collection.title,
                description = collection.description,
                created_at = createdAt
            )
            replaceBooks(collection.id, collection.books)
        }
    }

    /** Updates a collection's metadata and replaces its book membership atomically. */
    fun update(collection: Collection) {
        database.transaction {
            collectionQueries.updateMeta(
                title = collection.title,
                description = collection.description,
                id = collection.id
            )
            replaceBooks(collection.id, collection.books)
        }
    }

    fun delete(collectionId: String) {
        database.transaction {
            bookQueries.deleteByCollection(collectionId)
            collectionQueries.deleteById(collectionId)
        }
    }

    private fun replaceBooks(collectionId: String, books: List<Book>) {
        bookQueries.deleteByCollection(collectionId)
        books.forEach { book ->
            bookQueries.insert(
                collection_id = collectionId,
                book_id = book.id,
                title = book.title,
                author = book.authors.firstOrNull()?.name,
                cover_url = book.coverUrl
            )
        }
    }

    private fun booksFor(collectionId: String): List<Book> =
        bookQueries.selectByCollection(collectionId).executeAsList().map { row ->
            Book(
                id = row.book_id,
                title = row.title,
                authors = row.author?.let { listOf(Author(id = it, name = it)) }.orEmpty(),
                coverUrl = row.cover_url
            )
        }
}
