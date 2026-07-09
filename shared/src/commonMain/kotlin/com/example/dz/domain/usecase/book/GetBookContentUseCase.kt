package com.example.dz.domain.usecase.book

import com.example.dz.core.error.AppError
import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.BookContent
import com.example.dz.domain.repository.BookRepository

/**
 * Loads a readable, paginated book body for [bookId].
 *
 * Resolves the book's [textUrl][com.example.dz.domain.model.Book.textUrl] via book details, fetches
 * the raw remote text, strips license boilerplate, and paginates it. Returns [AppError.NotFound]
 * when the book has no readable text (e.g. sources without a full-text format), and propagates any
 * network error from the underlying calls.
 */
class GetBookContentUseCase(
    private val repository: BookRepository,
    private val paginator: BookPaginator = BookPaginator()
) {
    suspend operator fun invoke(bookId: String): AppResult<BookContent> {
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
