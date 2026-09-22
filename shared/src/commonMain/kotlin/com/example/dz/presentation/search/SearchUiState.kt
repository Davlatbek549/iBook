package com.example.dz.presentation.search

import com.example.dz.domain.model.Book

data class SearchUiState(
    val query: String = "",
    val books: List<Book> = emptyList(),
    /**
     * The query [books] is the answer to. The box moves on as soon as a key is pressed, and the
     * results only when the next search lands, so this is what "3 results" and "Nothing matches"
     * are about — not whatever happens to be typed right now.
     */
    val resultsFor: String? = null,
    val recentSearches: List<String> = emptyList(),
    /** Every book on the reader's shelf, as [librarySignatures] — see [isInLibrary]. */
    val librarySignatures: Set<String> = emptySet(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val hasQuery: Boolean
        get() = query.isNotBlank()

    /**
     * Whether a result is already on the reader's shelf — the "In your library" line.
     *
     * By id where the ids agree, and by title and author where they cannot: search answers from
     * Open Library, but a shelf is filled from Gutenberg, so the same novel arrives as
     * `openlibrary-OL…` in one place and `gutenberg-2701` in the other.
     */
    fun isInLibrary(book: Book): Boolean =
        book.librarySignatures().any { it in librarySignatures }
}

/**
 * The keys a book is recognised by on a shelf: its id, and its title with its first author.
 *
 * The title keeps only what comes before a subtitle ("Moby Dick; Or, The Whale" is "Moby Dick"),
 * and the author's words are sorted, because Gutenberg files names surname-first ("Melville,
 * Herman") where Open Library writes them as said. A book with no author gets no title key — two
 * different "Poems" are not the same book.
 */
internal fun Book.librarySignatures(): List<String> = buildList {
    if (id.isNotBlank()) add("id:$id")
    val titleKey = title.substringBefore(';').substringBefore(':').matchWords(sorted = false)
    val authorKey = authors.firstOrNull()?.name?.matchWords(sorted = true).orEmpty()
    if (titleKey.isNotEmpty() && authorKey.isNotEmpty()) add("work:$titleKey|$authorKey")
}

private fun String.matchWords(sorted: Boolean): String {
    val words = lowercase()
        .map { if (it.isLetterOrDigit()) it else ' ' }
        .joinToString("")
        .split(' ')
        .filter { it.isNotEmpty() }
    return (if (sorted) words.sorted() else words).joinToString(" ")
}
