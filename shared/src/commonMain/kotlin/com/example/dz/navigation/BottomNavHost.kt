package com.example.dz.navigation

import dz.shared.generated.resources.Res
import dz.shared.generated.resources.ic_book
import dz.shared.generated.resources.ic_home
import dz.shared.generated.resources.ic_profile
import dz.shared.generated.resources.ic_search
import dz.shared.generated.resources.ic_shop

val bottomNavItems = listOf(
    BottomNavItem(Routes.HOME, Res.drawable.ic_home),
    BottomNavItem(Routes.LIBRARY, Res.drawable.ic_book),
    BottomNavItem(Routes.STORE, Res.drawable.ic_shop),
    BottomNavItem(Routes.SEARCH, Res.drawable.ic_search),
    BottomNavItem(Routes.PROFILE_TAB, Res.drawable.ic_profile)
)
