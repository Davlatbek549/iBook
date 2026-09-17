package com.example.dz

import com.example.dz.data.mapper.BookMapper
import com.example.dz.data.remote.dto.gutendex.GutendexBookDto
import com.example.dz.data.remote.dto.openlibrary.OpenLibraryRatingsDto
import com.example.dz.data.remote.dto.openlibrary.OpenLibraryRatingsSummaryDto
import com.example.dz.domain.usecase.book.cleanBookText
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class BookContentMappingTest {

    @Test
    fun mapsPlainTextUrlPreferringUtf8() {
        val dto = GutendexBookDto(
            id = 1342,
            title = "Pride and Prejudice",
            formats = mapOf(
                "text/plain; charset=us-ascii" to "https://example.org/ascii.txt",
                "text/plain; charset=utf-8" to "https://example.org/utf8.txt",
                "image/jpeg" to "https://example.org/cover.jpg"
            )
        )

        assertEquals("https://example.org/utf8.txt", BookMapper.fromGutendexBook(dto).textUrl)
    }

    @Test
    fun skipsZippedPlainTextAndFallsBackToARealTextFile() {
        val dto = GutendexBookDto(
            id = 11,
            title = "Alice",
            formats = mapOf(
                // Preferred by charset, but archived — must be skipped.
                "text/plain; charset=utf-8" to "https://example.org/alice.txt.zip",
                "text/plain" to "https://example.org/alice.txt"
            )
        )

        assertEquals("https://example.org/alice.txt", BookMapper.fromGutendexBook(dto).textUrl)
    }

    @Test
    fun textUrlIsNullWhenNoPlainTextFormatExists() {
        val dto = GutendexBookDto(
            id = 99,
            title = "Cover only",
            formats = mapOf(
                "image/jpeg" to "https://example.org/cover.jpg",
                "application/epub+zip" to "https://example.org/book.epub"
            )
        )

        assertNull(BookMapper.fromGutendexBook(dto).textUrl)
    }

    @Test
    fun ratingsWithNobodyBehindThemAreNothing() {
        // OpenLibrary answers for an unrated work with zeroes rather than a 404, and a histogram
        // of zeroes drawn on screen would read as "rated badly" rather than "not rated".
        val dto = OpenLibraryRatingsDto(
            summary = OpenLibraryRatingsSummaryDto(average = 0.0, count = 0),
            counts = mapOf("1" to 0, "2" to 0, "3" to 0, "4" to 0, "5" to 0),
        )

        assertNull(BookMapper.fromOpenLibraryRatings(dto))
    }

    @Test
    fun ratingsKeepEveryScoreTheyWereGiven() {
        val dto = OpenLibraryRatingsDto(
            summary = OpenLibraryRatingsSummaryDto(average = 4.339622641509434, count = 106),
            counts = mapOf("1" to 3, "2" to 3, "3" to 8, "4" to 33, "5" to 59, "rubbish" to 9),
        )

        val ratings = BookMapper.fromOpenLibraryRatings(dto)

        assertEquals(106, ratings?.count)
        assertEquals(59, ratings?.byStar?.get(5))
        assertEquals(3, ratings?.byStar?.get(1))
        assertEquals(5, ratings?.byStar?.size, "a key that is not a score is not a score")
        // Bars are drawn against the most-given score, so the tallest is always full and the
        // rest are read against it rather than against the total.
        assertEquals(1f, ratings?.share(5))
        assertEquals(33f / 59f, ratings?.share(4))
        assertEquals(0f, ratings?.share(0), "a score nobody could give fills nothing")
    }

    @Test
    fun cleanBookTextStripsGutenbergBoilerplate() {
        val raw = buildString {
            append("The Project Gutenberg eBook of Something\r\n")
            append("This header should be removed.\r\n")
            append("*** START OF THE PROJECT GUTENBERG EBOOK SOMETHING ***\r\n\r\n")
            append("Real body line one.\r\n\r\n")
            append("Real body line two.\r\n\r\n")
            append("*** END OF THE PROJECT GUTENBERG EBOOK SOMETHING ***\r\n")
            append("License footer that should be removed.")
        }

        val cleaned = cleanBookText(raw)

        assertTrue(cleaned.startsWith("Real body line one."), "got: $cleaned")
        assertTrue(cleaned.endsWith("Real body line two."), "got: $cleaned")
        assertTrue("header" !in cleaned && "footer" !in cleaned)
    }

    @Test
    fun cleanBookTextFallsBackWhenNoMarkersPresent() {
        val raw = "Just plain text\r\n\r\n\r\n\r\nwith extra blank lines."
        val cleaned = cleanBookText(raw)
        assertEquals("Just plain text\n\nwith extra blank lines.", cleaned)
    }

    @Test
    fun cleanBookTextRejoinsHardWrappedProse() {
        val raw = "On the pleasant banks of the Garonne, in the province of Gascony,\n" +
            "stood, in the year 1584, the chateau of Monsieur St. Aubert."

        assertEquals(
            "On the pleasant banks of the Garonne, in the province of Gascony, " +
                "stood, in the year 1584, the chateau of Monsieur St. Aubert.",
            cleanBookText(raw)
        )
    }

    @Test
    fun cleanBookTextKeepsBreaksTheBookMeant() {
        // A contents list: every line is short, so every break is the book's own.
        val raw = "VOLUME I\nCHAPTER I\nCHAPTER II\nCHAPTER III"
        assertEquals(raw, cleanBookText(raw))
    }
}
