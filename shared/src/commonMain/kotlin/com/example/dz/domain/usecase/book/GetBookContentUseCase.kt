package com.example.dz.domain.usecase.book

import com.example.dz.core.error.AppError
import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.BookContent
import com.example.dz.domain.repository.BookRepository
import com.example.dz.domain.repository.DownloadRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Loads a readable, paginated book body for [bookId], local-first.
 *
 * If the book is downloaded, its text is read from local storage (works offline). Otherwise it
 * resolves the book's [textUrl][com.example.dz.domain.model.Book.textUrl] via book details, fetches
 * the raw remote text, strips license boilerplate, and paginates it. Returns [AppError.NotFound]
 * when the book has no readable text, and propagates any network error from the underlying calls.
 *
 * [downloadRepository] is optional: when absent, content is always loaded from the network.
 *
 * Cleaning and paginating run on [textWork], never on the caller's thread. A Gutenberg novel is
 * hundreds of kilobytes to a megabyte of text, scanned by three regexes and split into pages —
 * done on the main thread, that was the screen freezing between tapping "Read" and seeing a page.
 */
class GetBookContentUseCase(
    private val repository: BookRepository,
    private val downloadRepository: DownloadRepository? = null,
    private val paginator: BookPaginator = BookPaginator(),
    private val textWork: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend operator fun invoke(bookId: String): AppResult<BookContent> {
        // Local-first: serve downloaded content without touching the network.
        downloadRepository?.getDownloadedContent(bookId)?.let { downloaded ->
            val localPages = paginate(downloaded.text)
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

        val pages = paginate(rawText)
        if (pages.isEmpty()) return AppResult.Error(AppError.NotFound)

        return AppResult.Success(
            BookContent(bookId = book.id, title = book.title, pages = pages)
        )
    }

    private suspend fun paginate(raw: String): List<String> =
        withContext(textWork) { paginator.paginate(cleanBookText(raw)) }
}

/**
 * Strips Project Gutenberg license boilerplate, normalizes newlines and undoes the source file's
 * hard wrapping so only the book body is paginated. Falls back to the trimmed input when the
 * start/end markers are absent.
 */
internal fun cleanBookText(raw: String): String {
    val normalized = raw.replace("\r\n", "\n").replace('\r', '\n')
    val start = START_MARKER.find(normalized)?.range?.last?.plus(1) ?: 0
    val end = END_MARKER.find(normalized, start)?.range?.first ?: normalized.length
    return normalized.substring(start, end)
        .replace(EXTRA_BLANK_LINES, "\n\n")
        .trim()
        .split(PARAGRAPH_SPLIT)
        .joinToString(PARAGRAPH_SPLIT) { unwrapParagraph(it) }
}

/**
 * Rejoins the lines a paragraph was only broken across because the file is hard-wrapped.
 *
 * Gutenberg wraps prose at around seventy characters. Rendered verbatim those breaks land in the
 * middle of sentences at a different place on every line, which on a reflowing page looks like the
 * text is damaged. A line is read as wrapped when it runs close to that width; anything shorter —
 * a chapter heading, a contents entry, a line of verse — is a break the book meant, and is kept.
 */
private fun unwrapParagraph(paragraph: String): String {
    val out = StringBuilder()
    var continuesPrevious = false
    for (raw in paragraph.split('\n')) {
        val line = if (continuesPrevious) raw.trim() else raw.trimEnd()
        if (out.isNotEmpty()) out.append(if (continuesPrevious) ' ' else '\n')
        out.append(line)
        continuesPrevious = line.length >= WRAPPED_LINE_LENGTH
    }
    return out.toString()
}

/** Shortest line still long enough to have been broken by the wrapper rather than by the book. */
private const val WRAPPED_LINE_LENGTH = 45

private const val PARAGRAPH_SPLIT = "\n\n"

private val START_MARKER = Regex("""\*\*\* ?START OF TH[EIS].*?\*\*\*""", RegexOption.IGNORE_CASE)
private val END_MARKER = Regex("""\*\*\* ?END OF TH[EIS].*?\*\*\*""", RegexOption.IGNORE_CASE)
private val EXTRA_BLANK_LINES = Regex("\\n{3,}")
