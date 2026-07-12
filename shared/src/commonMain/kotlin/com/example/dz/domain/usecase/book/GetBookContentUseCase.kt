package com.example.dz.domain.usecase.book

import com.example.dz.core.error.AppError
import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.BookContent
import com.example.dz.domain.repository.BookRepository
import com.example.dz.domain.repository.DownloadRepository

/**
 * Loads a readable, paginated book body for [bookId], local-first.
 *
 * If the book is downloaded, its text is read from local storage (works offline). Otherwise it
 * resolves the book's [textUrl][com.example.dz.domain.model.Book.textUrl] via book details, fetches
 * the raw remote text, strips license boilerplate, and paginates it. Returns [AppError.NotFound]
 * when the book has no readable text, and propagates any network error from the underlying calls.
 *
 * [downloadRepository] is optional: when absent, content is always loaded from the network.
 */
class GetBookContentUseCase(
    private val repository: BookRepository,
    private val downloadRepository: DownloadRepository? = null,
    private val paginator: BookPaginator = BookPaginator()
) {
    suspend operator fun invoke(bookId: String): AppResult<BookContent> {
        // Local-first: serve downloaded content without touching the network.
        downloadRepository?.getDownloadedContent(bookId)?.let { downloaded ->
            val localPages = paginator.paginate(cleanBookText(downloaded.text))
            if (localPages.isNotEmpty()) {
                return AppResult.Success(
                    BookContent(bookId = bookId, title = downloaded.title, pages = localPages)
                )
            }
        }

        val book = when (val details = repository.getBookDetails(bookId)) {
            is AppResult.Success -> details.data
            is AppResult.Error -> return details
        }

        val textUrl = book.textUrl ?: return AppResult.Error(AppError.NotFound)

        val rawText = when (val text = repository.getBookText(textUrl)) {
            is AppResult.Success -> text.data
            is AppResult.Error -> return text
        }

        val pages = paginator.paginate(cleanBookText(rawText))
        if (pages.isEmpty()) return AppResult.Error(AppError.NotFound)

        return AppResult.Success(
            BookContent(bookId = book.id, title = book.title, pages = pages)
        )
    }
}

/**
 * Strips Project Gutenberg license boilerplate and normalizes newlines so only the book body is
 * paginated. Falls back to the trimmed input when the start/end markers are absent.
 */
internal fun cleanBookText(raw: String): String {
    val normalized = raw.replace("\r\n", "\n").replace('\r', '\n')
    val start = START_MARKER.find(normalized)?.range?.last?.plus(1) ?: 0
    val end = END_MARKER.find(normalized, start)?.range?.first ?: normalized.length
    return normalized.substring(start, end)
        .replace(EXTRA_BLANK_LINES, "\n\n")
        .trim()
}

private val START_MARKER = Regex("""\*\*\* ?START OF TH[EIS].*?\*\*\*""", RegexOption.IGNORE_CASE)
private val END_MARKER = Regex("""\*\*\* ?END OF TH[EIS].*?\*\*\*""", RegexOption.IGNORE_CASE)
private val EXTRA_BLANK_LINES = Regex("\\n{3,}")
