package com.example.dz.presentation.navigation

import com.example.dz.designsystem.components.icons.OrganicIcons
import com.example.dz.designsystem.components.organic.OrganicTab

/**
 * Four tabs — Home · Library · Friends · Profile.
 *
 * The handoff draws five, with a store in the middle. There is no store: the catalogue is Project
 * Gutenberg's public domain, nothing in it is sold, and its screen has been removed rather than
 * left as a shopfront for books that are already free. What that screen offered — the genres — is
 * Browse, and every search circle opens it.
 *
 * The handoff also contradicts itself about slot four: its layout table and its rendered frames
 * draw a magnifier, while the navigation section and Home's own part table both name Friends.
 * Friends wins on two counts. The written spec says it twice, and it removes a duplicate — search
 * already has a circle on Home and on Library, so a tab for it was a third way to reach the same
 * screen, while Friends, a four-screen area, had no permanent home at all.
 *
 * Profile keeps both of its routes: the tab, and the Home avatar. The handoff calls that pair
 * intentional — the tab is persistent navigation, the avatar is identity in context — and unlike
 * search they answer different questions.
 */
val bottomNavItems = listOf(
    OrganicTab(Routes.HOME, OrganicIcons.Home, label = "Home"),
    OrganicTab(Routes.LIBRARY, OrganicIcons.Library, label = "Library"),
    OrganicTab(Routes.FRIEND_LIST, OrganicIcons.Friends, label = "Friends"),
    OrganicTab(Routes.PROFILE_TAB, OrganicIcons.Profile, label = "Profile"),
)
