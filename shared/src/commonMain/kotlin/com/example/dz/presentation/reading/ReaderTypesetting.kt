package com.example.dz.presentation.reading

import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.text.style.LineBreak

/**
 * How hard the platform is asked to work on a line of the page.
 *
 * Justified text wants breaks chosen for the paragraph as a whole and words broken with hyphens,
 * or the spaces stretch and the page grows rivers. Android does both cheaply. The same request on
 * iOS is laid out by Skia, where it costs seconds a page — long enough that cutting a novel never
 * finished — so there it asks for neither, and takes looser spacing over a reader that never opens.
 */
expect fun readerHyphens(): Hyphens

expect fun readerLineBreak(): LineBreak
