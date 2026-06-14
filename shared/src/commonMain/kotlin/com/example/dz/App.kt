package com.example.dz

import androidx.compose.runtime.Composable
import com.example.dz.navigation.DZNavGraph
import com.example.dz.theme.DZTheme

@Composable
fun App() {
    DZTheme {
        DZNavGraph()
    }
}
