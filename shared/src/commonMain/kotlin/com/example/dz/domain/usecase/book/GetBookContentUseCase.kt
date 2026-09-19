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
 * Loads a readable book body for [bookId], local-first.
 *
 * If the book is downloaded, its text is read from local storage (works offline). Otherwise it
 * resolves the book's [textUrl][com.example.dz.domain.model.Book.textUrl] via book details, fetches
 * the raw remote text and strips license boilerplate. Returns [AppError.NotFound] when the book has
 * no readable text, and propagates any network error from the underlying calls.
 *
 * Nothing here cuts the text into pages. A page is however much fits on the screen at the size the
 * reader has picked, so only the reader can say where one ends.
 *
 * [downloadRepository] is optional: when absent, content is always loaded from the network.
 *
 * Cleaning runs on [textWork], never on the caller's thread. A Gutenberg novel is hundreds of
 * kilobytes to a megabyte of text scanned by three regexes and re-joined paragraph by paragraph —
 * done on the main thread, that was the screen freezing between tapping "Read" and seeing a page.
 */
class GetBookContentUseCase(
    private val repository: BookRepository,
    private val downloadRepository: DownloadRepository? = null,
    private val textWork: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend operator fun invoke(bookId: String): AppResult<BookContent> {
        // Local-first: serve downloaded content without touching the network.
        downloadRepository?.getDownloadedContent(bookId)?.let { downloaded ->
            val localText = clean(downloaded.text)
            if (localText.isNotEmpty()) {
                return AppResult.Success(
                    BookContent(bookId = bookId, title = downloaded.title, text = localText)
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

        val text = clean(rawText)
        if (text.isEmpty()) return AppResult.Error(AppError.NotFound)

        return AppResult.Success(
            BookContent(bookId = book.id, title = book.title, text = text)
        )
    }

    private suspend fun clean(raw: String): String = withContext(textWork) { cleanBookText(raw) }
}

/**
 * Turns a raw download into a book body: license boilerplate stripped, newlines normalised, the
 * source file's hard wrapping undone, and paragraphs marked the way a printed book marks them.
 *
 * Falls back to the trimmed input when the start/end markers are absent.
 */
internal fun cleanBookText(raw: String): String {
    val normalized = raw.replace("\r\n", "\n").replace('\r', '\n')
    val start = START_MARKER.find(normalized)?.range?.last?.plus(1) ?: 0
    // Searched from the tail. The end marker is the last thing in the file, and looking for it
    // from the front means running a pattern over the whole book to find something at the back.
    val tail = maxOf(start, normalized.length - MARKER_WINDOW)
    val end = END_MARKER.find(normalized, tail)?.range?.first ?: normalized.length

    return stripEmphasisUnderscores(normalized.substring(start, end))
        .split(PARAGRAPH_SPLIT)
        // A run of blank lines is still one paragraph break, and splitting on a pair of newlines
        // leaves the extras behind as empty pieces.
        .map { it.trim('\n') }
        .filter { it.isNotEmpty() }
        .joinToString("\n") { indentParagraph(unwrapParagraph(it)) }
}

/**
 * Drops the underscores Gutenberg sets italics with, in one pass over the text.
 *
 * This was a regex with a lookbehind and a lookahead, which is a fine way to say it and a
 * catastrophic way to run it: applied to the seven hundred thousand characters of a novel it took
 * minutes on iOS, where Kotlin/Native brings its own regex engine — long enough that the reader
 * never opened at all. The rule is the same as it was: an underscore between two letters or digits
 * belongs to whatever it is spelling, and every other one goes.
 */
private fun stripEmphasisUnderscores(text: String): String {
    if (!text.contains('_')) return text

    val out = StringBuilder(text.length)
    for (i in text.indices) {
        val char = text[i]
        val insideAWord = char == '_' &&
            isWordCharacter(text.getOrNull(i - 1)) &&
            isWordCharacter(text.getOrNull(i + 1))
        if (char != '_' || insideAWord) out.append(char)
    }
    return out.toString()
}

private fun isWordCharacter(char: Char?): Boolean = char != null && (char.isLetter() || char.isDigit())

/**
 * Marks a new paragraph the way a book does: by indenting its first line, not by leaving a blank
 * line above it.
 *
 * A blank line costs a whole line of the page and, at reading size on a phone, three or four of
 * them are most of what is on screen. An indent costs nothing and says the same thing — which is
 * why every printed book does it this way.
 *
 * Only prose is indented. The first line of a chapter heading, a contents entry or a line of verse
 * is short because the book meant it to be, and pushing it inwards would only look like a mistake.
 * A paragraph carried over from the previous page needs no indent either, and gets none for free:
 * the indent belongs to the paragraph's first line, which is back on the page before.
 */
private fun indentParagraph(paragraph: String): String =
    if (startsAParagraph(paragraph)) PARAGRAPH_INDENT + paragraph else paragraph

/**
 * Whether a block of the book is prose starting a new paragraph, or something a book sets apart.
 *
 * Length alone is not enough: "Mr. Bennet replied that he had not." is a whole paragraph and is
 * shorter than a chapter heading. So the question is asked the other way round — everything is a
 * paragraph unless it looks like one of the things that are not:
 *
 * - too short to be a sentence, which is what a heading or a contents entry usually is;
 * - set in capitals, which is the other thing a heading usually is;
 * - carrying a run of spaces, which is how a plain-text file draws a column — a contents entry
 *   padded out to the page number at the right of it;
 * - several lines none of which ever reached the wrap width, which is verse: lines broken where
 *   the poet broke them, and pushing the first one inwards would be pushing a poem sideways.
 */
private fun startsAParagraph(block: String): Boolean {
    val lines = block.split('\n')
    val first = lines.first()
    return when {
        first.length < SHORTEST_SENTENCE -> false
        first.none { it.isLowerCase() } -> false
        COLUMN_GAP in first -> false
        lines.size > 1 && lines.none { it.length >= WRAPPED_LINE_LENGTH } -> false
        else -> true
    }
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

/** Below this, a line is a title or a label rather than something somebody said or wrote. */
private const val SHORTEST_SENTENCE = 20

/** Three spaces in a row are not prose; they are a plain-text file drawing a column. */
private const val COLUMN_GAP = "   "

/**
 * The indent itself, held in the text rather than applied as a style.
 *
 * A style would have to be re-applied per paragraph, which means cutting the text into styled
 * blocks, which means the offsets the reader keeps its place with no longer line up with the
 * string they were measured against. Characters cost nothing and keep one text throughout.
 *
 * Two em spaces rather than a run of ordinary ones, because iOS trims the leading spaces off a
 * line and the indent simply disappears. An em space is a character in its own right and survives.
 */
private const val PARAGRAPH_INDENT = "\u2003\u2003"

private const val PARAGRAPH_SPLIT = "\n\n"

private val START_MARKER = Regex("""\*\*\* ?START OF TH[EIS].*?\*\*\*""", RegexOption.IGNORE_CASE)
private val END_MARKER = Regex("""\*\*\* ?END OF TH[EIS].*?\*\*\*""", RegexOption.IGNORE_CASE)
/** How far into each end of the file the Gutenberg markers are worth looking for. */
private const val MARKER_WINDOW = 100_000

