package com.example.dz.designsystem.theme

import androidx.compose.ui.graphics.Color

object CategoryPalette {
    private val map = mapOf(
        "comic" to ColorCategoryComic,
        "arts" to ColorCategoryArts,
        "biographies" to ColorCategoryBiographies,
        "business" to ColorCategoryBusiness,
        "cooking" to ColorCategoryCooking,
        "edu" to ColorCategoryEducation,
        "health" to ColorCategoryHealth,
        "history" to ColorCategoryHistory,
        "kids" to ColorCategoryKids,
        "medical" to ColorCategoryMedical,
        "fantasy" to ColorCategoryFantasy,
        "self_help" to ColorCategorySelfHelp,
        "sport" to ColorCategorySport,
        "travel" to ColorCategoryTravel,
        "romantic" to ColorSecondary,
        "horror" to ColorTertiary
    )

    fun colorFor(category: String?): Color =
        map[category?.lowercase()?.replace(" ", "_")] ?: ColorPrimary
}