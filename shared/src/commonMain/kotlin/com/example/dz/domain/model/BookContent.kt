package com.example.dz.domain.model

/**
 * Fully loaded, paginated body of a book, ready for the reader UI to page through.
 *
 * [pages] is ordered; page 1 is `pages[0]`. [pageCount] is the total number of pages.
 */
data class BookContent(
    val bookId: String,
    val title: String,
    val pages: List<String>
) {
    val pageCount: Int get() = pages.size
}
