package com.example.dz.domain.model

/**
 * A book's whole readable body, cleaned of everything that is not the book.
 *
 * It is not cut into pages here, and cannot be: a page is however much text fits on the screen at
 * the size the reader has chosen, which nothing below the UI knows. The reader measures the text
 * against its own viewport and cuts it there, and re-cuts it whenever the type size changes.
 */
data class BookContent(
    val bookId: String,
    val title: String,
    val text: String,
)
