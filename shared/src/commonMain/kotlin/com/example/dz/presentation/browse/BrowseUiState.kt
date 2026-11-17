package com.example.dz.presentation.browse

data class BrowseUiState(
    val categories: List<BrowseCategoryUi> = emptyList(),
    val selectedMoodIndex: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class BrowseCategoryUi(
    val id: String,
    val name: String,
    val bookCount: Int
)
