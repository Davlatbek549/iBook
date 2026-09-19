package com.example.dz.presentation.reading

import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive

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
) {
    val pageCount: Int get() = starts.size

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
        val Empty = ReaderPagination(text = "", starts = listOf(0))
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
): ReaderPagination {
    if (text.isEmpty() || constraints.maxWidth <= 0 || constraints.maxHeight <= 0) {
        return ReaderPagination.Empty
    }

    val starts = mutableListOf(0)
    var cursor = 0
    var window = INITIAL_WINDOW

    while (cursor < text.length) {
        currentCoroutineContext().ensureActive()

        var next = -1
        while (next <= cursor) {
            val windowEnd = minOf(text.length, cursor + window)
            val layout = measurer.measure(
                text = text.substring(cursor, windowEnd),
                style = style,
                // Width is the page's; height is left open so every line the window produced can
                // be asked about, and the ones past the bottom are simply not used.
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

                // The window ran out before the page did, so the page is longer than what was
                // measured — measure again with more of the book in hand.
                lastFitting == layout.lineCount - 1 && windowEnd < text.length -> {
                    window *= 2
                    -1
                }

                else -> cursor + layout.getLineEnd(lastFitting, visibleEnd = false)
            }

            if (next == -1) continue
            if (next <= cursor) next = minOf(text.length, cursor + 1)
            next = wholeWord(text, end = next, floor = cursor)
        }

        // A page never opens on a blank line: the break above often lands just before the one that
        // separates two paragraphs, and a page that starts with a gap looks like a mistake.
        cursor = next
        while (cursor < text.length && text[cursor].isWhitespace()) cursor++
        if (cursor < text.length) starts += cursor
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
 * How much text to lay out per measurement. It only has to exceed one page; when it does not, the
 * loop above doubles it and measures again, so the starting guess costs nothing if it is wrong.
 */
private const val INITIAL_WINDOW = 4_000
