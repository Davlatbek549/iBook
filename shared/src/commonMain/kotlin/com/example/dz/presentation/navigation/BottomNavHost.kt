package com.example.dz.presentation.navigation

import com.example.dz.designsystem.components.icons.OrganicIcons
import com.example.dz.designsystem.components.organic.OrganicTab

/**
 * Five tabs — Home · Library · Store · Friends · Profile.
 *
 * The handoff contradicts itself about slot four: its layout table and its rendered frames draw a
 * magnifier, while the navigation section and Home's own part table both name Friends. Friends wins
 * on two counts. The written spec says it twice, and it removes a duplicate — search already has a
 * circle on Home and on Library, so a tab for it was a third way to reach the same screen, while
 * Friends, a four-screen area, had no permanent home at all. Home's part table also documents every
 * element on that screen except the search circle, which suggests the frames and the spec were
 * drawn at different moments.
 *
 * Profile keeps both of its routes: the tab, and the Home avatar. The handoff calls that pair
 * intentional — the tab is persistent navigation, the avatar is identity in context — and unlike
 * search they answer different questions.
 */
val bottomNavItems = listOf(
    OrganicTab(Routes.HOME, OrganicIcons.Home, label = "Home"),
    OrganicTab(Routes.LIBRARY, OrganicIcons.Library, label = "Library"),
    OrganicTab(Routes.STORE, OrganicIcons.Store, label = "Store"),
    OrganicTab(Routes.FRIEND_LIST, OrganicIcons.Friends, label = "Friends"),
    OrganicTab(Routes.PROFILE_TAB, OrganicIcons.Profile, label = "Profile"),
)
