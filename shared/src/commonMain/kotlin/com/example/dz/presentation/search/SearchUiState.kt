package com.example.dz.presentation.search

data class SearchUiState(
    val query: String = "",
    val recentSearches: List<String> = emptyList(),
    val results: List<SearchResultUi> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class SearchResultUi(
    val id: String,
    val title: String,
    val author: String,
    val coverUrl: String?,
    val inLibrary: Boolean,
    val isFree: Boolean,
    val price: String?
)
