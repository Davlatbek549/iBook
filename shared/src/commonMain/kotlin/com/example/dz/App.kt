package com.example.dz

import androidx.compose.runtime.Composable
import com.example.dz.presentation.navigation.DZNavGraph
import com.example.dz.designsystem.theme.DZTheme

@Composable
fun App() {
    DZTheme {
        DZNavGraph()
    }
}
