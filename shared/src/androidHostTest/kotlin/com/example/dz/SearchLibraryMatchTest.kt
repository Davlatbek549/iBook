package com.example.dz

import com.example.dz.domain.model.Author
import com.example.dz.domain.model.Book
import com.example.dz.presentation.search.SearchUiState
import com.example.dz.presentation.search.librarySignatures
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * "In your library" on a search result. Search answers from Open Library while a shelf is filled
 * from Gutenberg, so the same book reaches the two with different ids and differently written names.
 */
class SearchLibraryMatchTest {

    private fun stateWithShelf(vararg shelf: Book) =
        SearchUiState(librarySignatures = shelf.flatMap { it.librarySignatures() }.toSet())

    private val gutenbergMobyDick = Book(
        id = "gutenberg-2701",
        title = "Moby Dick; Or, The Whale",
        authors = listOf(Author(id = "melville", name = "Melville, Herman")),
    )

    @Test
    fun `the same id is the same book`() {
        val state = stateWithShelf(gutenbergMobyDick)

        assertTrue(state.isInLibrary(gutenbergMobyDick.copy(title = "Anything")))
    }

    @Test
    fun `the same work from another catalogue is recognised by title and author`() {
        val state = stateWithShelf(gutenbergMobyDick)
        val openLibraryMobyDick = Book(
            id = "openlibrary-OL102749W",
            title = "Moby Dick",
            authors = listOf(Author(id = "OL1A", name = "Herman Melville")),
        )

        assertTrue(state.isInLibrary(openLibraryMobyDick))
    }

    @Test
    fun `a different author's book of the same name is not on the shelf`() {
        val state = stateWithShelf(gutenbergMobyDick)
        val namesake = Book(
            id = "openlibrary-OL1W",
            title = "Moby Dick",
            authors = listOf(Author(id = "OL2A", name = "Somebody Else")),
        )

        assertFalse(state.isInLibrary(namesake))
    }

    @Test
    fun `a title with no author is never matched on title alone`() {
        val poems = Book(id = "gutenberg-1", title = "Poems")
        val state = stateWithShelf(poems)

        assertFalse(state.isInLibrary(Book(id = "openlibrary-OL9W", title = "Poems")))
    }
}
