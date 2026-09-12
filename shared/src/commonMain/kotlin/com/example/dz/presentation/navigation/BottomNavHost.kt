package com.example.dz.presentation.navigation

import com.example.dz.designsystem.components.icons.OrganicIcons
import com.example.dz.designsystem.components.organic.OrganicTab

/**
 * Five tabs — Home · Library · Store · Search · Profile.
 *
 * The redesign adds Profile as a destination; it used to be reachable only by pushing it from the
 * Home avatar. Both paths now land on the same screen, which the handoff calls intentional: the tab
 * is persistent navigation, the avatar is contextual identity.
 *
 * Slot four is Search. The handoff contradicts itself here — its navigation notes call the fourth
 * destination Friends, while its layout table and every screen image draw a magnifier — so this
 * follows the images, which the bundle names as the source of truth for what a screen looks like.
 * Friends stays reachable from Home's presence card and the Profile friends row.
 */
val bottomNavItems = listOf(
    OrganicTab(Routes.HOME, OrganicIcons.Home, label = "Home"),
    OrganicTab(Routes.LIBRARY, OrganicIcons.Library, label = "Library"),
    OrganicTab(Routes.STORE, OrganicIcons.Store, label = "Store"),
    OrganicTab(Routes.SEARCH, OrganicIcons.Search, label = "Search"),
    OrganicTab(Routes.PROFILE_TAB, OrganicIcons.Profile, label = "Profile"),
)
