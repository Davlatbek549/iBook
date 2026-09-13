package com.example.dz

import com.example.dz.data.mapper.BookMapper
import com.example.dz.data.remote.dto.gutendex.GutendexBookDto
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
