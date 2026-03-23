package com.example.ibook.screens.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.example.ibook.navigation.CustomBottomBar
import com.example.ibook.screens.temp.BooksScreen
import com.example.ibook.screens.temp.HomeScreen
import com.example.ibook.screens.temp.SearchScreen
import com.example.ibook.screens.temp.StoreScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val currentRoute = navController
        .currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        bottomBar = {
            CustomBottomBar(
                currentRoute = currentRoute ?: "home",
                onItemClick = { route ->
                    navController.navigate(route) {
                        popUpTo("home")
                        launchSingleTop = true
                    }
                }
            )
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(padding)
        ) {
            composable("home") { HomeScreen() }
            composable("books") { BooksScreen() }
            composable("store") { StoreScreen() }
            composable("search") { SearchScreen() }
        }
    }
}