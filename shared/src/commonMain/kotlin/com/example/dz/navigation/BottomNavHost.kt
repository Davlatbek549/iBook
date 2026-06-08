package com.example.dz.navigation

import dz.shared.generated.resources.Res
import dz.shared.generated.resources.ic_book
import dz.shared.generated.resources.ic_home
import dz.shared.generated.resources.ic_search
import dz.shared.generated.resources.ic_shop

val bottomNavItems = listOf(
    BottomNavItem("home", Res.drawable.ic_home),
    BottomNavItem("library", Res.drawable.ic_book),
    BottomNavItem("store", Res.drawable.ic_shop),
    BottomNavItem("search", Res.drawable.ic_search)
)
