package com.example.dz

import com.example.dz.presentation.reading.ReaderPagination
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * The bookkeeping around a cut book.
 *
 * Where the cuts fall is decided by measuring real type against a real screen, which needs a
 * device; what a cut book can then answer — which page an offset is on, what is printed on a page,
 * whether the pin is on this one — is arithmetic, and is what puts a reader back in their place
 * after the type size changes.
 */
class ReaderPaginationTest {

    private val text = "Chapter one begins.\n\nAnd carries on.\n\nAnd then it ends."
    private val pagination = ReaderPagination(text = text, starts = listOf(0, 21, 38))

    @Test
    fun aPageIsTheTextBetweenItsStartAndTheNext() {
        assertEquals("Chapter one begins.", pagination.pageText(0))
        assertEquals("And carries on.", pagination.pageText(1))
        assertEquals("And then it ends.", pagination.pageText(2))
    }

    @Test
    fun theLastPageRunsToTheEndOfTheBook() {
        assertEquals(3, pagination.pageCount)
        assertTrue(pagination.pageText(2).endsWith("ends."))
        assertEquals("", pagination.pageText(3), "there is no page after the last one")
    }

    @Test
    fun anOffsetLandsOnThePageThatContainsIt() {
        assertEquals(0, pagination.pageOf(0))
        assertEquals(0, pagination.pageOf(18), "the last character of page one is still page one")
        assertEquals(1, pagination.pageOf(21), "a page start belongs to its own page")
        assertEquals(1, pagination.pageOf(30))
        assertEquals(2, pagination.pageOf(38))
        assertEquals(2, pagination.pageOf(text.length), "the end of the book is the last page")
    }

    @Test
    fun theSameOffsetSurvivesTheBookBeingCutAgain() {
        // What page 2 of the old cut started on has to still be findable in the new one — this is
        // the whole reason a reader's place is an offset and not a page number.
        val larger = ReaderPagination(text = text, starts = listOf(0, 12, 24, 36, 48))

        assertEquals(1, pagination.pageOf(21))
        assertEquals(1, larger.pageOf(21), "21 is inside the second page of the bigger type too")
    }

    @Test
    fun thePinLightsUpOnlyOnThePageItIsOn() {
        assertTrue(pagination.holds(offset = 25, index = 1))
        assertFalse(pagination.holds(offset = 25, index = 0))
        assertFalse(pagination.holds(offset = 25, index = 2))
        assertFalse(pagination.holds(offset = null, index = 1), "nothing pinned lights nothing")
    }
}
