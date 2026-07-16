package com.example.dz.data.repository

import com.example.dz.core.error.AppError
import com.example.dz.core.result.AppResult
import com.example.dz.data.mapper.BookMapper
import com.example.dz.data.remote.api.GutendexApi
import com.example.dz.data.remote.api.KtorGutendexApi
import com.example.dz.data.remote.api.KtorOpenLibraryApi
import com.example.dz.data.remote.api.OpenLibraryApi
import com.example.dz.data.remote.api.createRemoteHttpClient
import com.example.dz.domain.model.Book
import com.example.dz.domain.model.Category
import com.example.dz.domain.repository.BookRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.http.isSuccess
import io.ktor.http.takeFrom

class RemoteBookRepository(
    private val openLibraryApi: OpenLibraryApi,
    private val gutendexApi: GutendexApi,
    private val httpClient: HttpClient
) : BookRepository {
    override suspend fun searchBooks(query: String): AppResult<List<Book>> =
        runRemote {
            val openLibraryBooks = openLibraryApi.searchBooks(query).docs
                .map(BookMapper::fromOpenLibraryBook)
                .filter { it.title.isNotBlank() }

            if (openLibraryBooks.isNotEmpty()) {
                openLibraryBooks
            } else {
                gutendexApi.searchBooks(query).results
                    .map(BookMapper::fromGutendexBook)
                    .filter { it.title.isNotBlank() }
            }
        }

    override suspend fun getHomeBooks(): AppResult<List<Book>> =
        runRemote {
            gutendexApi.getBooks().results
                .map(BookMapper::fromGutendexBook)
                .filter { it.title.isNotBlank() }
        }

    override suspend fun getBooksByCategory(categoryId: String): AppResult<List<Book>> =
        runRemote {
            openLibraryApi.searchBooksBySubject(categoryId).docs
                .map(BookMapper::fromOpenLibraryBook)
                .filter { it.title.isNotBlank() }
        }

    override suspend fun getBookDetails(bookId: String): AppResult<Book> =
        runRemote {
            if (bookId.startsWith(GUTENDEX_PREFIX)) {
                BookMapper.fromGutendexBook(gutendexApi.getBook(bookId))
            } else {
                BookMapper.fromOpenLibraryWork(
                    openLibraryApi.getWork(BookMapper.openLibraryWorkIdFromDomainId(bookId))
                )
            }
        }

    override suspend fun getCategories(): AppResult<List<Category>> =
        AppResult.Success(defaultCategories)

    // Gutenberg text URLs 302-redirect to a plain http:// mirror. Ktor won't follow an
    // https -> http downgrade (and both platforms block cleartext requests anyway), so the 302
    // page itself would be served as the book text. Follow redirects by hand, forcing https.
    override suspend fun getBookText(textUrl: String): AppResult<String> =
        runRemote {
            var url = httpsUrl(textUrl)
            var body: String? = null
            var hops = 0
            while (body == null) {
                val response = httpClient.get(url)
                val location = response.headers[HttpHeaders.Location]
                if (response.status.value in 300..399 && location != null) {
                    check(++hops <= MAX_TEXT_REDIRECTS) {
                        "Too many redirects fetching book text from $textUrl"
                    }
                    url = nextRedirectUrl(current = url, location = location)
                } else {
                    check(response.status.isSuccess()) {
                        "HTTP ${response.status.value} fetching book text from $url"
                    }
                    body = response.bodyAsText()
                }
            }
            body
        }

    private fun httpsUrl(url: String): String =
        if (url.startsWith("http://")) "https://" + url.removePrefix("http://") else url

    private fun nextRedirectUrl(current: String, location: String): String =
        URLBuilder(current).apply {
            takeFrom(location)
            if (protocol == URLProtocol.HTTP) protocol = URLProtocol.HTTPS
        }.buildString()

    private suspend fun <T> runRemote(block: suspend () -> T): AppResult<T> =
        try {
            AppResult.Success(block())
        } catch (error: ResponseException) {
            AppResult.Error(
                when (error.response.status.value) {
                    401, 403 -> AppError.Unauthorized
                    404 -> AppError.NotFound
                    else -> AppError.Network
                }
            )
        } catch (error: Throwable) {
            AppResult.Error(AppError.Network)
        }

    companion object {
        private const val GUTENDEX_PREFIX = "gutenberg-"
        private const val MAX_TEXT_REDIRECTS = 5

        private val defaultCategories = listOf(
            Category(id = "fiction", name = "Fiction"),
            Category(id = "fantasy", name = "Fantasy"),
            Category(id = "science-fiction", name = "Science Fiction"),
            Category(id = "history", name = "History"),
            Category(id = "biography", name = "Biography"),
            Category(id = "self-help", name = "Self Help"),
            Category(id = "business", name = "Business")
        )

        fun create(httpClient: HttpClient = createRemoteHttpClient()): RemoteBookRepository =
            RemoteBookRepository(
                openLibraryApi = KtorOpenLibraryApi(httpClient),
                gutendexApi = KtorGutendexApi(httpClient),
                httpClient = httpClient
            )
    }
}
