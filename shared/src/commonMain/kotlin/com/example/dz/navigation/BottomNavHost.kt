package com.example.dz.navigation

import com.example.dz.app_components.icons.InkIcons

// Spec: four tabs — Home · Library · Store · Search.
// Profile is NOT a tab; it is pushed from the Home header avatar.
val bottomNavItems = listOf(
    BottomNavItem(Routes.HOME, InkIcons.Home),
    BottomNavItem(Routes.LIBRARY, InkIcons.Book),
    BottomNavItem(Routes.STORE, InkIcons.Shop),
    BottomNavItem(Routes.SEARCH, InkIcons.Search)
)
