package com.example.dz.presentation.store.browse

import com.example.dz.domain.model.Category

/**
 * The moods Browse offers, and the genres each one narrows the grid to.
 *
 * Nothing in the catalogue is tagged cosy or unsettling, so this is a reading of the genres rather
 * than a query: it lives here, beside the screen that makes the claim, and not in the data layer
 * where it would pass for something the catalogue knows. A genre can suit more than one mood —
 * poetry is both cosy and short.
 */
enum class BrowseMood(val categoryIds: Set<String>) {
    COSY(setOf("romance", "cooking", "nature", "poetry")),
    UNSETTLING(setOf("gothic-fiction", "horror", "mystery")),
    SHORT(setOf("short-stories", "poetry")),
    EPIC(setOf("fantasy", "adventure", "history", "science-fiction")),
}

/** The order the grid is in: the catalogue's own, or alphabetical once A–Z is on. */
enum class BrowseSort { CATALOGUE, ALPHABETICAL }

data class BrowseUiState(
    val categories: List<Category> = emptyList(),
    /**
     * How many books each genre holds, by category id, filled in as the counts arrive. A genre with
     * no entry has not answered yet — or could not — and its tile simply goes without the line.
     */
    val bookCounts: Map<String, Int> = emptyMap(),
    val mood: BrowseMood? = null,
    val sort: BrowseSort = BrowseSort.CATALOGUE,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    /** The tiles to draw: narrowed to the chosen mood, then put in the chosen order. */
    val shownCategories: List<Category>
        get() {
            val narrowed = mood?.let { chosen -> categories.filter { it.id in chosen.categoryIds } }
                ?: categories
            return when (sort) {
                BrowseSort.CATALOGUE -> narrowed
                BrowseSort.ALPHABETICAL -> narrowed.sortedBy { it.name.lowercase() }
            }
        }
}
