package com.example.ibook.screens.temp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun HomeScreen() {
    ScreenContent("Home Screen")
}

@Composable
fun BooksScreen() {
    ScreenContent("Books Screen")
}

@Composable
fun StoreScreen() {
    ScreenContent("Store Screen")
}

@Composable
fun ScreenContent(title: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}