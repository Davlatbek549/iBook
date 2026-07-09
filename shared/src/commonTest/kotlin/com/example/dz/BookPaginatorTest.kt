package com.example.dz

import com.example.dz.domain.usecase.book.BookPaginator
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BookPaginatorTest {

    private fun String.stripWhitespace(): String = filterNot { it.isWhitespace() }

    @Test
    fun emptyText_returnsNoPages() {
        assertEquals(emptyList(), BookPaginator().paginate(""))
    }

    @Test
    fun blankText_returnsNoPages() {
        assertEquals(emptyList(), BookPaginator().paginate("   \n\n   \t  "))
    }

    @Test
    fun shortText_fitsOnASinglePage() {
        val pages = BookPaginator(charsPerPage = 100).paginate("A short line.")
        assertEquals(listOf("A short line."), pages)
    }

    @Test
    fun groupsWholeParagraphsUpToTheLimit() {
        // Three 4-char paragraphs joined by blank lines = "AAAA\n\nBBBB\n\nCCCC" (16 chars) ≤ 30.
        val pages = BookPaginator(charsPerPage = 30).paginate("AAAA\n\nBBBB\n\nCCCC")
        assertEquals(1, pages.size)
        assertEquals("AAAA\n\nBBBB\n\nCCCC", pages.single())
    }

    @Test
    fun startsANewPageWhenTheNextParagraphWouldOverflow() {
        // Each paragraph is 20 chars; two won't fit in 30 together, so each gets its own page.
        val p = "x".repeat(20)
        val pages = BookPaginator(charsPerPage = 30).paginate("$p\n\n$p\n\n$p")
        assertEquals(3, pages.size)
        pages.forEach { assertEquals(p, it) }
    }

    @Test
    fun everyPageStaysWithinTheLimit() {
        val limit = 40
        val text = (1..25).joinToString("\n\n") { "Paragraph number $it has some words in it." }
        val pages = BookPaginator(charsPerPage = limit).paginate(text)
        assertTrue(pages.isNotEmpty())
        pages.forEach { assertTrue(it.length <= limit, "page too long (${it.length}): '$it'") }
    }

    @Test
    fun oversizedParagraphWithoutSpacesIsHardSplit() {
        val pages = BookPaginator(charsPerPage = 50).paginate("z".repeat(120))
        assertEquals(listOf(50, 50, 20), pages.map { it.length })
        assertEquals("z".repeat(120), pages.joinToString(""))
    }

    @Test
    fun oversizedParagraphBreaksOnWhitespace() {
        val word = "word "            // 5 chars incl. trailing space
        val paragraph = word.repeat(40).trim() // 199 chars, spaced
        val pages = BookPaginator(charsPerPage = 50).paginate(paragraph)
        pages.forEach { assertTrue(it.length <= 50) }
        // No word is broken across a page boundary.
        pages.forEach { page -> page.split(" ").forEach { assertEquals("word", it) } }
    }

    @Test
    fun paginationPreservesAllNonWhitespaceContent() {
        val text = (1..12).joinToString("\n\n") { "Sentence $it, with punctuation!" }
        val pages = BookPaginator(charsPerPage = 45).paginate(text)
        assertEquals(text.stripWhitespace(), pages.joinToString("").stripWhitespace())
    }

    @Test
    fun defaultPageSizeIsFifteenHundred() {
        val pages = BookPaginator().paginate("q".repeat(3000))
        assertEquals(listOf(1500, 1500), pages.map { it.length })
    }
}
