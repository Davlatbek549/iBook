package com.example.dz.domain.model

data class Collection(
    val id: String,
    val title: String,
    val description: String? = null,
    /**
     * Which of the design's five shelf swatches this collection wears. An index rather than a
     * colour: the palette belongs to the design system, and a stored hex would outlive it.
     */
    val colorIndex: Int = 0,
    /** Shared shelves can be added to by friends. */
    val isShared: Boolean = false,
    val books: List<Book> = emptyList()
)
