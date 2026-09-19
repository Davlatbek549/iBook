package com.example.dz.data.repository

import com.example.dz.data.local.LocalDataSource
import com.example.dz.domain.model.PageTheme
import com.example.dz.domain.model.PageTurn
import com.example.dz.domain.model.ReaderPreferences
import com.example.dz.domain.repository.ReaderPreferencesRepository

/**
 * Reader preferences in the device's key-value store, beside the session.
 *
 * Reads are synchronous on purpose: the reader asks for them while laying out a page, and a page
 * that has to wait for its own type size would flash at its own size first.
 */
class LocalReaderPreferencesRepository(
    private val local: LocalDataSource,
) : ReaderPreferencesRepository {

    override fun get(): ReaderPreferences {
        val scale = local.getSetting(KEY_FONT_SCALE).toFloatOrNull()
            ?: ReaderPreferences.DEFAULT_FONT_SCALE
        val theme = local.getSetting(KEY_PAGE_THEME)
            .takeIf { it.isNotBlank() }
            // An unknown name means the palette moved on since this was written; the default is a
            // better answer than a crash.
            ?.let { name -> PageTheme.entries.firstOrNull { it.name == name } }
            ?: PageTheme.CREAM
        return ReaderPreferences(
            fontScale = scale.coerceIn(ReaderPreferences.MIN_FONT_SCALE, ReaderPreferences.MAX_FONT_SCALE),
            pageTheme = theme,
            useSerif = local.getSetting(KEY_SERIF) == true.toString(),
            pageTurn = local.getSetting(KEY_PAGE_TURN)
                .takeIf { it.isNotBlank() }
                ?.let { name -> PageTurn.entries.firstOrNull { it.name == name } }
                ?: PageTurn.SLIDE,
        )
    }

    override fun save(preferences: ReaderPreferences) {
        local.saveSetting(KEY_FONT_SCALE, preferences.fontScale.toString())
        local.saveSetting(KEY_PAGE_THEME, preferences.pageTheme.name)
        local.saveSetting(KEY_SERIF, preferences.useSerif.toString())
        local.saveSetting(KEY_PAGE_TURN, preferences.pageTurn.name)
    }

    private companion object {
        const val KEY_FONT_SCALE = "reader_font_scale"
        const val KEY_PAGE_THEME = "reader_page_theme"
        const val KEY_SERIF = "reader_serif"
        const val KEY_PAGE_TURN = "reader_page_turn"
    }
}
