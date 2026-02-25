package com.example.ibook.ui.theme

import androidx.compose.ui.graphics.Color

object CategoryPalette {
    private val map = mapOf(
        "comic" to Comic,
        "arts" to Arts,
        "biographies" to Biographies,
        "business" to Business,
        "cooking" to Cooking,
        "edu" to Edu,
        "health" to Health,
        "history" to History,
        "kids" to Kids,
        "medical" to Medical,
        "fantasy" to Fantasy,
        "self_help" to Self_Help,
        "sport" to Sport,
        "travel" to Travel,
        "romantic" to Romantic,
        "horror" to Horror
    )

    fun colorFor(category: String?): Color =
        map[category?.lowercase()?.replace(" ", "_")] ?: MainColor
}