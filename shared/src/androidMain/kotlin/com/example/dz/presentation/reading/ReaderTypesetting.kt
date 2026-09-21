package com.example.dz.presentation.reading

import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.text.style.LineBreak

actual fun readerHyphens(): Hyphens = Hyphens.Auto

/** Android only hyphenates when breaks are chosen for the paragraph rather than line by line. */
actual fun readerLineBreak(): LineBreak = LineBreak.Paragraph
