package com.example.dz.domain.model

/** Offline book content read from local storage: enough to render the reader without a network. */
data class DownloadedContent(
    val bookId: String,
    val title: String,
    val text: String
)
