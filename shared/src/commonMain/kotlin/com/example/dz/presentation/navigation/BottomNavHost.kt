package com.example.dz.presentation.navigation

import com.example.dz.designsystem.components.icons.OrganicIcons
import com.example.dz.designsystem.components.organic.OrganicTab

/**
 * Four tabs — Home · Library · Friends · Profile. Store is gone (its browse content moved to
 * Browse, pushed from a search circle rather than anchored as a tab) and Search was never a tab
 * either — a reader who can already name what they want reaches it from Browse, one step in.
 */
val bottomNavItems = listOf(
    OrganicTab(Routes.HOME, OrganicIcons.Home, label = "Home"),
    OrganicTab(Routes.LIBRARY, OrganicIcons.Library, label = "Library"),
    OrganicTab(Routes.FRIEND_LIST, OrganicIcons.Friends, label = "Friends"),
    OrganicTab(Routes.PROFILE_TAB, OrganicIcons.Profile, label = "Profile"),
)
