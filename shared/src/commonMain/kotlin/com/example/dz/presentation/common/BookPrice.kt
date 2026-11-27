package com.example.dz.presentation.common

import androidx.compose.runtime.Composable
import com.example.dz.domain.model.Book
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.book_free
import org.jetbrains.compose.resources.stringResource

/**
 * What a book costs, as the handoff writes it — the meta line under a search result.
 *
 * The handoff prices every title, but nothing the app reads sells one: the catalogue is Project
 * Gutenberg's public domain, so a real price is the exception and the rule is free. That is why
 * there is no store screen; what it offered, the genres, is Browse. "Free" is only
 * claimed for a book the reader can actually open here — one with text to read. A title with neither
 * a price nor text (most of what Open Library returns) says nothing, rather than promising a book
 * the reader then cannot get.
 */
@Composable
fun Book.priceLabel(): String? =
    price ?: if (textUrl != null) stringResource(Res.string.book_free) else null
