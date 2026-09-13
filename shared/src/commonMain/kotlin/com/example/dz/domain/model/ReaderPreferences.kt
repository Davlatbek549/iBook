package com.example.dz.domain.model

/**
 * The ground a page is set on. The handoff offers four, from the app's own cream through to night.
 *
 * Stored by name rather than by colour so the palette stays the design system's to change.
 */
enum class PageTheme { CREAM, PAPER, SAGE, NIGHT }

/**
 * How the reader wants their pages set: how big, on what ground, in which face.
 *
 * Device-side, as the handoff specifies — this is about one person's eyes and one screen, not
 * about their account, so it does not follow them to another phone.
 */
data class ReaderPreferences(
    val fontScale: Float = DEFAULT_FONT_SCALE,
    val pageTheme: PageTheme = PageTheme.CREAM,
    /** The serif is the alternative voice for long reading; Figtree is the app's own. */
    val useSerif: Boolean = false,
) {
    /** Body size in sp, from the design's 17sp base. */
    val bodySizeSp: Float get() = BASE_BODY_SP * fontScale

    companion object {
        const val DEFAULT_FONT_SCALE = 1f
        const val MIN_FONT_SCALE = 0.85f
        const val MAX_FONT_SCALE = 1.5f
        const val BASE_BODY_SP = 17f
    }
}
