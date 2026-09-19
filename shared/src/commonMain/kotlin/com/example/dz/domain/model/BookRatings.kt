package com.example.dz.domain.model

/**
 * What other readers made of a book: an average, how many gave it, and how those scores fell.
 *
 * Only OpenLibrary titles have any of this. Project Gutenberg records downloads, not opinions, so
 * for most of the catalogue there is nothing here and the screen has to say so.
 */
data class BookRatings(
    val average: Double,
    val count: Int,
    /** How many readers gave each score, keyed 1..5. Missing keys mean nobody. */
    val byStar: Map<Int, Int>,
) {
    /** How much of the bar a score fills, against the most-given score rather than the total. */
    fun share(star: Int): Float {
        val most = byStar.values.maxOrNull() ?: 0
        if (most <= 0) return 0f
        return (byStar[star] ?: 0).toFloat() / most
    }

    companion object {
        val STARS = 5 downTo 1
    }
}
