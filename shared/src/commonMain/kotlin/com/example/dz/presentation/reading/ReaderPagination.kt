package com.example.dz.presentation.reading

import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import kotlinx.coroutines.yield

/**
 * Where each page of a book starts, for one type size on one screen.
 *
 * A page here is not a fixed number of characters — it is exactly what fits, measured against the
 * real viewport with the real face at the real size. Change the size and the whole thing is cut
 * again, which is why a reader's place is kept as a character offset rather than a page number:
 * page 40 means nothing after the type grows, but the word someone stopped on still does.
 */
class ReaderPagination(
    private val text: String,
    /** The character each page begins at. Always starts with 0 and is strictly increasing. */
    val starts: List<Int>,
    /** False while the rest of the book is still being cut. */
    val isComplete: Boolean = true,
) {
    val pageCount: Int get() = starts.size

    /**
     * Whether the page holding [offset] is known yet.
     *
     * Cutting a novel takes long enough to be worth watching on some devices, so the reader is
     * shown their page the moment it exists rather than when the last one does.
     */
    fun covers(offset: Int): Boolean = isComplete || starts.last() > offset

    fun pageText(index: Int): String {
        val from = starts.getOrNull(index) ?: return ""
        val to = starts.getOrNull(index + 1) ?: text.length
        return text.substring(from, to).trimEnd()
    }

    fun startOf(index: Int): Int = starts.getOrNull(index) ?: 0

    /** Whether [offset] falls on the page at [index] — what makes the pin light up. */
    fun holds(offset: Int?, index: Int): Boolean {
        if (offset == null) return false
        val from = starts.getOrNull(index) ?: return false
        val to = starts.getOrNull(index + 1) ?: (text.length + 1)
        return offset in from until to
    }

    /** The page an offset falls on — how a reader is put back where they were. */
    fun pageOf(offset: Int): Int {
        val found = starts.binarySearch { it.compareTo(offset) }
        return if (found >= 0) found else (-found - 2).coerceAtLeast(0)
    }

    companion object {
        val Empty = ReaderPagination(text = "", starts = listOf(0), isComplete = false)
    }
}

/**
 * Cuts [text] into pages that fit [constraints].
 *
 * Each pass measures a window of text a little longer than a page can hold, then asks the layout
 * which lines actually fit the height and where the last of them ends. That offset is the next
 * page's start, so a page break always lands on a line boundary and never mid-word.
 *
 * The whole book is cut in one go because the reader has to be told how many pages there are —
 * a progress bar cannot be a fraction of an unknown. It is linear in the length of the book and
 * belongs on a background thread; [ensureActive] lets a size change abandon a run in progress
 * rather than finishing a pagination nobody will read.
 */
suspend fun paginateForViewport(
    text: String,
    measurer: TextMeasurer,
    style: TextStyle,
    constraints: Constraints,
    /** The height of one line at this size, which is what says how many stand on a page. */
    lineHeightPx: Float,
    /** Called as the cut goes, so the reader can start reading before the last page is found. */
    onProgress: suspend (ReaderPagination) -> Unit = {},
): ReaderPagination {
    if (text.isEmpty() || constraints.maxWidth <= 0 || constraints.maxHeight <= 0 || lineHeightPx <= 0f) {
        return ReaderPagination.Empty
    }

    val starts = mutableListOf(0)
    var cursor = 0
    // Both of these are guesses that correct themselves on the first page and then hold.
    var window = INITIAL_WINDOW
    var maxLines = ((constraints.maxHeight / lineHeightPx).toInt() + 1).coerceAtLeast(1)

    while (cursor < text.length) {
        // Yield rather than only checking for cancellation: this runs on the main dispatcher, so
        // the page between two yields is the longest the UI is ever held up by the cut.
        yield()

        var next = -1
        while (next <= cursor) {
            val windowEnd = minOf(text.length, cursor + window)
            val layout = measurer.measure(
                text = text.substring(cursor, windowEnd),
                style = style,
                // The line cap is the whole cost of this. Laying out the window unbounded means
                // setting every line of it — a hundred, hyphenated — to find the dozen that stand
                // on the page, once per page, which is the book laid out many times over to cut
                // it once. Capped, a page costs a page.
                maxLines = maxLines,
                // Width is the page's; the height is not given, so the lines past the bottom are
                // still there to be asked about rather than silently dropped.
                constraints = Constraints(maxWidth = constraints.maxWidth),
                overflow = TextOverflow.Clip,
                softWrap = true,
            )

            val lastFitting = (layout.lineCount - 1 downTo 0).firstOrNull { line ->
                layout.getLineBottom(line) <= constraints.maxHeight
            }

            next = when {
                // Not even one line fits: take a line anyway rather than loop forever on a
                // viewport too short for the type in it.
                lastFitting == null -> cursor + layout.getLineEnd(0, visibleEnd = false)

                // A line was laid out that does not fit, so the page ends at the last one that
                // does. This is the ordinary case.
                lastFitting < layout.lineCount - 1 ->
                    cursor + layout.getLineEnd(lastFitting, visibleEnd = false)

                // Everything laid out fits, so the page may not be full yet. Either the window
                // ran out of text or the cap ran out of lines; whichever it was is raised, and
                // the page is measured again.
                layout.lineCount == maxLines -> {
                    maxLines += MORE_LINES
                    -1
                }

                windowEnd < text.length -> {
                    window *= 2
                    -1
                }

                // Nothing ran out — this is the end of the book.
                else -> cursor + layout.getLineEnd(lastFitting, visibleEnd = false)
            }

            if (next == -1) continue
            if (next <= cursor) next = minOf(text.length, cursor + 1)
            next = wholeWord(text, end = next, floor = cursor)
        }

        // Measure the next window against the page this one turned out to be, so the layout above
        // is given about a page of text rather than a constant somebody guessed.
        window = ((next - cursor) * WINDOW_SLACK).toInt().coerceAtLeast(MIN_WINDOW)

        // A page never opens on a blank line: the break above often lands just before the one that
        // separates two paragraphs, and a page that starts with a gap looks like a mistake.
        cursor = next
        while (cursor < text.length && text[cursor].isWhitespace()) cursor++
        if (cursor < text.length) starts += cursor

        // Hand back what has been cut so far. Finding the last page of a novel is not instant,
        // and there is no reason to hold someone at a blank screen once the page they are on
        // has been found.
        if (starts.size % PAGES_PER_REPORT == 0) {
            onProgress(ReaderPagination(text, starts.toList(), isComplete = false))
        }
    }

    return ReaderPagination(text = text, starts = starts)
}

/**
 * Pulls a page break back off the middle of a word.
 *
 * Hyphenation breaks lines inside words, and the hyphen it draws is the layout's, not the text's.
 * When such a break is also a page break there is nothing to draw the hyphen on — the page would
 * simply end in "im" and the next would open on "proved" — so the whole word goes over instead.
 *
 * [floor] is where the page started: a single word longer than a whole page has nowhere to go, and
 * is left broken rather than made into a page that holds nothing.
 */
internal fun wholeWord(text: String, end: Int, floor: Int): Int {
    if (end <= floor || end >= text.length) return end
    if (text[end - 1].isWhitespace() || text[end].isWhitespace()) return end

    var start = end
    while (start > floor && !text[start - 1].isWhitespace()) start--
    return if (start > floor) start else end
}

/**
 * How much text to lay out for the first measurement, before any page has been cut to learn from.
 *
 * It only has to exceed one page; when it does not, the loop doubles it and measures again, so a
 * low guess costs one extra layout at the start and a high one costs a little on every page until
 * the first is cut. Erring low is the cheaper mistake.
 */
private const val INITIAL_WINDOW = 1_200

/** Enough over the last page's length to be sure the next one fits inside the window. */
private const val WINDOW_SLACK = 1.6

/** A floor, so a page of two words does not make the next window too small to hold a sentence. */
private const val MIN_WINDOW = 400

/** How much headroom to add when the line cap turns out to be short of what the page holds. */
private const val MORE_LINES = 4

/** How often the cut so far is handed back — often enough to be read from, rarely enough to be cheap. */
private const val PAGES_PER_REPORT = 10
