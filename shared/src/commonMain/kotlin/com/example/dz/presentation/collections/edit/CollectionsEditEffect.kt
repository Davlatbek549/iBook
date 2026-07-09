package com.example.dz.presentation.collections.edit

sealed interface CollectionsEditEffect {
    data object NavigateBack : CollectionsEditEffect
}
