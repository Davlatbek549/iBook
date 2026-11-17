package com.example.dz.data.remote.api

import com.example.dz.domain.model.Author
import com.example.dz.domain.model.Book
import com.example.dz.domain.model.Collection
import com.example.dz.domain.model.LibraryBook
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.expectSuccess
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable

/** The existing dz-server contract. Download locations are deliberately never sent. */
interface ReadingDataApi {
    suspend fun library(): List<LibraryBook>
    suspend fun saveBook(book: LibraryBook)
    suspend fun progress(bookId: String, percent: Int)
    suspend fun collections(): List<Collection>
    suspend fun saveCollection(collection: Collection)
    suspend fun deleteCollection(id: String)
}

class KtorReadingDataApi(private val client: HttpClient, private val baseUrl: String) : ReadingDataApi {
    override suspend fun library() = client.get("$baseUrl/library/books")
        .body<LibraryResponse>().books.map { it.domain() }

    override suspend fun saveBook(book: LibraryBook) {
        client.put("$baseUrl/library/books/${book.book.id.encodeURLPathPart()}") {
            contentType(ContentType.Application.Json)
            setBody(BookSnapshot.from(book))
        }
    }

    override suspend fun progress(bookId: String, percent: Int) {
        client.patch("$baseUrl/library/books/${bookId.encodeURLPathPart()}") {
            contentType(ContentType.Application.Json)
            setBody(ProgressRequest(percent))
        }
    }

    override suspend fun collections() = client.get("$baseUrl/collections")
        .body<CollectionsResponse>().collections.map { it.domain() }

    override suspend fun saveCollection(collection: Collection) {
        client.put("$baseUrl/collections/${collection.id.encodeURLPathPart()}") {
            contentType(ContentType.Application.Json)
            setBody(CollectionSnapshot(collection.title, collection.description,
                collection.books.map { CollectionBook(it.id, it.title, it.authors.firstOrNull()?.name, it.coverUrl) }))
        }
    }

    override suspend fun deleteCollection(id: String) {
        // A repeated delete after a lost response is already complete on the server.
        client.delete("$baseUrl/collections/${id.encodeURLPathPart()}") { expectSuccess = false }
            .also { response ->
                if (!response.status.isSuccess() && response.status != HttpStatusCode.NotFound) {
                    throw io.ktor.client.plugins.ResponseException(response, "Collection deletion failed")
                }
            }
    }
}

@Serializable private data class LibraryResponse(val books: List<BookSnapshot>)
@Serializable private data class ProgressRequest(val progressPercent: Int)
@Serializable private data class BookSnapshot(
    val title: String,
    val author: String? = null,
    val coverUrl: String? = null,
    val description: String? = null,
    val textUrl: String? = null,
    val isFree: Boolean = true,
    val isFavorite: Boolean = false,
    val progressPercent: Int = 0,
    val bookId: String = "",
) {
    fun domain() = LibraryBook(Book(bookId, title, authors(author), coverUrl, description,
        isFree = isFree, textUrl = textUrl), progressPercent, isFavorite = isFavorite)

    companion object {
        fun from(entry: LibraryBook) = with(entry.book) {
            BookSnapshot(title, authors.firstOrNull()?.name, coverUrl, description, textUrl,
                isFree, entry.isFavorite, entry.progressPercent)
        }
    }
}

@Serializable private data class CollectionsResponse(val collections: List<CollectionSnapshot>)
@Serializable private data class CollectionSnapshot(
    val title: String,
    val description: String? = null,
    val books: List<CollectionBook> = emptyList(),
    val id: String = "",
) {
    fun domain() = Collection(id, title, description, books = books.map {
        Book(it.bookId, it.title, authors(it.author), it.coverUrl)
    })
}
@Serializable private data class CollectionBook(
    val bookId: String, val title: String, val author: String? = null, val coverUrl: String? = null,
)
private fun authors(name: String?) = name?.let { listOf(Author(it, it)) }.orEmpty()
