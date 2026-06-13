package com.example.dz.navigation


import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import com.example.dz.theme.inkColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.savedstate.read
import com.example.dz.screens.authors_details.AuthorsDetailsScreen
import com.example.dz.screens.book_detail.BookDetailScreen
import com.example.dz.screens.book_review.BookReviewScreen
import com.example.dz.screens.category_detail.CategoryDetailScreen
import com.example.dz.screens.chat.ChatScreen
import com.example.dz.screens.collection_details.CollectionDetails
import com.example.dz.screens.collections.Collections
import com.example.dz.screens.collections_edit.CollectionsEdit
import com.example.dz.screens.first_friend_list.FriendListScreen
import com.example.dz.screens.forgot_password.ForgotPasswordScreen
import com.example.dz.screens.friend.FriendScreen
import com.example.dz.screens.goal.Goal
import com.example.dz.screens.home.HomeScreen
import com.example.dz.screens.invite_friend_list_2.InviteFriendList2Screen
import com.example.dz.screens.library.Library
import com.example.dz.screens.login.LoginScreen
import com.example.dz.screens.membership.MembershipScreen
import com.example.dz.screens.noMembership.NoMembershipScreen
import com.example.dz.screens.no_friends.NoFriendsScreen
import com.example.dz.screens.notification.Notifications
import com.example.dz.screens.onboarding.OnboardingScreenOne
import com.example.dz.screens.onboarding.OnboardingScreenThree
import com.example.dz.screens.onboarding.OnboardingScreenTwo
import com.example.dz.screens.payment_failed.PurchaseFailedScreen
import com.example.dz.screens.payment_methods.PaymentBrand
import com.example.dz.screens.payment_methods.PaymentMethodsScreen
import com.example.dz.screens.payment_success.PurchaseSuccessScreen
import com.example.dz.screens.pre_purchase.PrePurchaseScreen
import com.example.dz.screens.premium_membership.PremiumMembership
import com.example.dz.screens.profile.ProfileScreen
import com.example.dz.screens.purchase_confirmation.PurchaseConfirmationScreen
import com.example.dz.screens.purchase_details.PurchaseDetailsScreen
import com.example.dz.screens.purchase_details.PurchaseReceiptScreen
import com.example.dz.screens.reading.ReadingScreen
import com.example.dz.screens.search.SearchScreen
import com.example.dz.screens.settings.Settings
import com.example.dz.screens.sign_up.SignUpScreen
import com.example.dz.screens.splash.SplashScreen
import com.example.dz.screens.store.StoreScreen
import com.example.dz.screens.verification.VerificationScreen
import org.jetbrains.compose.resources.painterResource

@Composable
fun CustomBottomBar(
    currentRoute: String,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = inkColors()

    Column(modifier = modifier.fillMaxWidth().background(colors.paper)) {
        HorizontalDivider(thickness = 1.dp, color = colors.line)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(bottom = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            bottomNavItems.forEach { item ->
                val isSelected = currentRoute == item.route

                val tint by animateColorAsState(
                    targetValue = if (isSelected) colors.accent else colors.muted,
                    label = "iconColor"
                )

                Column(
                    modifier = Modifier.clickable { onItemClick(item.route) },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.route,
                        tint = tint,
                        modifier = Modifier.size(21.dp)
                    )
                    Box(
                        modifier = Modifier
                            .size(4.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) colors.accent else Color.Transparent)
                    )
                }
            }
        }
    }
}

@Composable
fun DZNavGraph() {
    val navController = rememberNavController()
    val currentRoute by navController.currentBackStackEntryAsState()
    val route = currentRoute?.destination?.route
    var isSearchFocused by remember { mutableStateOf(false) }

    LaunchedEffect(route) {
        if (route != Routes.SEARCH) {
            isSearchFocused = false
        }
    }

    val bottomBarHiddenRoutes = setOf(
        Routes.SPLASH,
        Routes.ONBOARDING_1,
        Routes.ONBOARDING_2,
        Routes.ONBOARDING_3,
        Routes.LOGIN,
        Routes.SIGN_UP,
        Routes.FORGOT_PASSWORD,
        Routes.VERIFICATION,
        Routes.BOOK_DETAIL,
        Routes.PRE_PURCHASE,
        Routes.BOOK_REVIEW,
        Routes.AUTHOR_DETAIL,
        Routes.CATEGORY_DETAIL,
        Routes.COLLECTION_DETAIL,
        Routes.COLLECTIONS_EDIT,
        Routes.GOAL,
        Routes.FRIEND_LIST,
        Routes.FRIEND_DETAIL,
        Routes.CHAT,
        Routes.NOTIFICATIONS,
        Routes.INVITE_FRIENDS,
        Routes.NO_FRIENDS,
        Routes.PROFILE_TAB,
        Routes.SETTINGS,
        Routes.MEMBERSHIP,
        Routes.PREMIUM_MEMBERSHIP,
        Routes.NO_MEMBERSHIP,
        Routes.PROFILE_EDIT,
        Routes.READING,
        Routes.PURCHASE_DETAILS,
        Routes.PURCHASE_RECEIPT,
        Routes.PURCHASE_CONFIRMATION,
        Routes.PAYMENT_METHODS,
        Routes.PAYMENT_SUCCESS,
        Routes.PAYMENT_FAILED
    )

    val showBottomBar = route != null && route !in bottomBarHiddenRoutes && !isSearchFocused

    fun navigateBottomTab(selectedRoute: String) {
        navController.navigate(selectedRoute) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = Routes.SPLASH,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(Routes.SPLASH) {
                SplashScreen(
                    onSplashFinished = {
                        navController.navigate(Routes.ONBOARDING_1) {
                            popUpTo(Routes.SPLASH) { inclusive = true }
                        }
                    }
                )
            }

            composable(Routes.ONBOARDING_1) {
                OnboardingScreenOne(
                    onNextClick = { navController.navigate(Routes.ONBOARDING_2) },
                    onSkipClick = {
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(Routes.ONBOARDING_1) { inclusive = true }
                        }
                    }
                )
            }

            composable(Routes.ONBOARDING_2) {
                OnboardingScreenTwo(
                    onNextClick = { navController.navigate(Routes.ONBOARDING_3) },
                    onSkipClick = {
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(Routes.ONBOARDING_1) { inclusive = true }
                        }
                    }
                )
            }

            composable(Routes.ONBOARDING_3) {
                OnboardingScreenThree(
                    onGetStartedClick = {
                        navController.navigate(Routes.SIGN_UP) {
                            popUpTo(Routes.ONBOARDING_1) { inclusive = true }
                        }
                    },
                    onLoginClick = {
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(Routes.ONBOARDING_1) { inclusive = true }
                        }
                    }
                )
            }

            composable(Routes.LOGIN) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    },
                    onForgotPasswordClick = { navController.navigate(Routes.FORGOT_PASSWORD) },
                    onSignUpClick = {
                        navController.navigate(Routes.SIGN_UP) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    }
                )
            }

            composable(Routes.SIGN_UP) {
                SignUpScreen(
                    onSignInClick = {
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(Routes.SIGN_UP) { inclusive = true }
                        }
                    },
                    onSignUpSuccess = { navController.navigate(Routes.VERIFICATION) }
                )
            }

            composable(Routes.FORGOT_PASSWORD) {
                ForgotPasswordScreen(
                    onBack = { navController.popBackStack() },
                    onSendLink = { navController.navigate(Routes.VERIFICATION) }
                )
            }

            composable(Routes.VERIFICATION) {
                VerificationScreen(
                    onVerified = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Routes.HOME) {
                HomeScreen(
                    onKeepReadingClick = {
                        navController.navigate(Routes.reading("current-book"))
                    },
                    onViewAllCategoriesClick = { navigateBottomTab(Routes.SEARCH) },
                    onBookClick = { book ->
                        navController.navigate(Routes.prePurchase(routeKey(book.title)))
                    },
                    onAuthorClick = { author ->
                        navController.navigate(Routes.authorDetail(routeKey(author.name)))
                    },
                    onGoalsKeepReadingClick = {
                        navController.navigate(Routes.reading("current-book"))
                    },
                    onNotificationsClick = { navController.navigate(Routes.NOTIFICATIONS) },
                    onProfileClick = { navController.navigate(Routes.PROFILE_TAB) }
                )
            }

            composable(Routes.LIBRARY) {
                Library(
                    onSettingsClick = { navController.navigate(Routes.COLLECTIONS) },
                    onSortClick = {},
                    onBookClick = { book ->
                        navController.navigate(Routes.reading(routeKey(book.title)))
                    },
                    onGoalClick = { navController.navigate(Routes.GOAL) }
                )
            }

            composable(Routes.STORE) {
                StoreScreen(
                    onViewMoreClick = { navController.navigate(Routes.MEMBERSHIP) },
                    onCategoryClick = { categoryName ->
                        navController.navigate(Routes.categoryDetail(routeKey(categoryName)))
                    },
                    onBookClick = { book ->
                        navController.navigate(Routes.prePurchase(routeKey(book.title)))
                    }
                )
            }

            composable(Routes.SEARCH) {
                SearchScreen(
                    onSearchFocusChange = { isSearchFocused = it },
                    onCategoryClick = { categoryName ->
                        navController.navigate(Routes.categoryDetail(routeKey(categoryName)))
                    },
                    onBookClick = { bookId ->
                        navController.navigate(Routes.prePurchase(routeKey(bookId)))
                    },
                    onAuthorClick = { authorId ->
                        navController.navigate(Routes.authorDetail(routeKey(authorId)))
                    }
                )
            }

            composable(Routes.PROFILE_TAB) {
                ProfileScreen(
                    onBackClick = { navController.popBackStack() },
                    onEditClick = {},
                    onNotificationsClick = { navController.navigate(Routes.NOTIFICATIONS) },
                    onFriendsClick = { navController.navigate(Routes.FRIEND_LIST) },
                    onGoalsClick = { navController.navigate(Routes.GOAL) },
                    onCollectionsClick = { navController.navigate(Routes.COLLECTIONS) },
                    onPurchasesClick = { navController.navigate(Routes.purchaseReceipt("history")) },
                    onMembershipClick = { navController.navigate(Routes.PREMIUM_MEMBERSHIP) },
                    onSettingsClick = { navController.navigate(Routes.SETTINGS) }
                )
            }

            composable(Routes.PRE_PURCHASE) { backStackEntry ->
                val bookId = backStackEntry.stringArgument("bookId", "book")
                PrePurchaseScreen(
                    onBackClick = { navController.popBackStack() },
                    onShareClick = {},
                    onFavoriteClick = {},
                    onViewSampleClick = {
                        navController.navigate(Routes.bookDetail(bookId))
                    },
                    onPurchaseClick = {
                        navController.navigate(Routes.purchaseDetails(bookId))
                    },
                    onTagsBookClick = {
                        navController.navigate(Routes.prePurchase("related-book"))
                    },
                    onAuthorClick = {
                        navController.navigate(Routes.authorDetail("author"))
                    }
                )
            }

            composable(Routes.BOOK_DETAIL) {
                BookDetailScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(Routes.READING) { backStackEntry ->
                val bookId = backStackEntry.stringArgument("bookId", "book")
                ReadingScreen(
                    onBackClick = { navController.popBackStack() },
                    onMenuClick = { navController.navigate(Routes.SETTINGS) },
                    onCommentsClick = {
                        navController.navigate(Routes.bookReview(bookId))
                    },
                    onKeepReadingClick = {
                        navController.navigate(Routes.bookDetail(bookId))
                    }
                )
            }

            composable(Routes.BOOK_REVIEW) { backStackEntry ->
                val bookId = backStackEntry.stringArgument("bookId", "book")
                BookReviewScreen(
                    onBackClick = { navController.popBackStack() },
                    onShareClick = {},
                    onReviewsClick = {},
                    onStartReadingClick = {
                        navController.navigate(Routes.bookDetail(bookId))
                    }
                )
            }

            composable(Routes.PURCHASE_DETAILS) {
                PurchaseDetailsScreen(
                    onBackClick = { navController.popBackStack() },
                    onDiscountCodeClick = {},
                    onChangePaymentClick = { navController.navigate(Routes.PAYMENT_METHODS) },
                    onPayNowClick = { navController.navigate(Routes.PURCHASE_CONFIRMATION) }
                )
            }

            composable(Routes.PURCHASE_RECEIPT) { backStackEntry ->
                val bookId = backStackEntry.stringArgument("bookId", "book")
                PurchaseReceiptScreen(
                    onBackClick = { navController.popBackStack() },
                    onReadNowClick = {
                        navController.navigate(Routes.reading(bookId))
                    },
                    onReceiptClick = {}
                )
            }

            composable(Routes.PAYMENT_METHODS) {
                PaymentMethodsScreen(
                    onBackClick = { navController.popBackStack() },
                    // A declined method (Apple Pay) demonstrates the failure branch;
                    // any other method completes the purchase. (No backend in the demo.)
                    onPaymentMethodSelected = { method ->
                        if (method.brand == PaymentBrand.ApplePay) {
                            navController.navigate(Routes.PAYMENT_FAILED)
                        } else {
                            navController.navigate(Routes.PAYMENT_SUCCESS) {
                                popUpTo(Routes.PRE_PURCHASE) { inclusive = true }
                            }
                        }
                    },
                    onAddPaymentMethodClick = {},
                    onConfirmClick = {}
                )
            }

            composable(Routes.PURCHASE_CONFIRMATION) {
                PurchaseConfirmationScreen(
                    onConfirm = { navController.navigate(Routes.PAYMENT_METHODS) },
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(Routes.PAYMENT_SUCCESS) {
                PurchaseSuccessScreen(
                    onBackClick = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.HOME) { inclusive = false }
                        }
                    },
                    onDiscountCodeClick = {},
                    onChangePaymentClick = { navController.popBackStack() },
                    onReadNowClick = {
                        navController.navigate(Routes.reading("purchased-book")) {
                            popUpTo(Routes.HOME) { inclusive = false }
                        }
                    }
                )
            }

            composable(Routes.PAYMENT_FAILED) {
                PurchaseFailedScreen(
                    onBackClick = { navController.popBackStack() },
                    onDiscountCodeClick = {},
                    // Change payment → back to the methods picker
                    onChangePaymentClick = {
                        navController.popBackStack(Routes.PAYMENT_METHODS, inclusive = false)
                    },
                    // Retry → back to Review order
                    onGetBackClick = {
                        navController.popBackStack(Routes.PURCHASE_DETAILS, inclusive = false)
                    }
                )
            }

            composable(Routes.CATEGORY_DETAIL) { backStackEntry ->
                val categoryName = backStackEntry.stringArgument("categoryName", "Horror")
                CategoryDetailScreen(
                    title = categoryName.replace('-', ' ').replaceFirstChar { it.uppercase() },
                    onBackClick = { navController.popBackStack() },
                    onBookClick = { book ->
                        navController.navigate(Routes.prePurchase(routeKey(book.title)))
                    },
                    onOptionsClick = { _ -> }
                )
            }

            composable(Routes.AUTHOR_DETAIL) {
                AuthorsDetailsScreen(
                    onBackClick = { navController.popBackStack() },
                    onBookClick = { bookTitle ->
                        navController.navigate(Routes.prePurchase(routeKey(bookTitle)))
                    }
                )
            }

            composable(Routes.COLLECTIONS) {
                Collections(
                    onBackClick = { navController.popBackStack() },
                    onSettingsClick = {
                        navController.navigate(Routes.collectionsEdit("all"))
                    },
                    onCollectionClick = { collectionId ->
                        navController.navigate(Routes.collectionDetail(routeKey(collectionId)))
                    }
                )
            }

            composable(Routes.COLLECTION_DETAIL) { backStackEntry ->
                val collectionId = backStackEntry.stringArgument("collectionId", "collection")
                CollectionDetails(
                    onBackClick = { navController.popBackStack() },
                    onAddClick = {},
                    onSettingsClick = {
                        navController.navigate(Routes.collectionsEdit(collectionId))
                    },
                    onRemoveEverywhereClick = { _ -> },
                    onBookOptionsClick = { _ -> },
                    onBookClick = { bookTitle ->
                        navController.navigate(Routes.prePurchase(routeKey(bookTitle)))
                    }
                )
            }

            composable(Routes.COLLECTIONS_EDIT) {
                CollectionsEdit(
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(Routes.SETTINGS) {
                Settings(
                    onBackClick = { navController.popBackStack() },
                    onNotificationsEnabledChange = { _ -> },
                    onAppearanceClick = {},
                    onTextSizeClick = {},
                    onPageBackgroundClick = {},
                    onTextFontClick = {},
                    onTermsClick = {},
                    onPrivacyPolicyClick = {},
                    onPurchasedClick = {
                        navController.navigate(Routes.purchaseDetails("history"))
                    }
                )
            }

            composable(Routes.NOTIFICATIONS) {
                Notifications(
                    onBackClick = { navController.popBackStack() },
                    onReadFilterClick = {},
                    onChatClick = { navController.navigate(Routes.FRIEND_LIST) }
                )
            }

            composable(Routes.GOAL) {
                Goal(
                    onBackClick = { navController.popBackStack() },
                    onSettingsClick = { navController.navigate(Routes.SETTINGS) },
                    onShareClick = {}
                )
            }

            composable(Routes.MEMBERSHIP) {
                MembershipScreen(
                    onBackClick = { navController.popBackStack() },
                    onStartTrialClick = { navController.navigate(Routes.PREMIUM_MEMBERSHIP) }
                )
            }

            composable(Routes.PREMIUM_MEMBERSHIP) {
                PremiumMembership(
                    onBackClick = { navController.popBackStack() },
                    onNotificationClick = { navController.navigate(Routes.NOTIFICATIONS) },
                    onMenuClick = { navController.navigate(Routes.SETTINGS) }
                )
            }

            composable(Routes.NO_MEMBERSHIP) {
                NoMembershipScreen(
                    onBackClick = { navController.popBackStack() },
                    onNotificationClick = { navController.navigate(Routes.NOTIFICATIONS) },
                    onMenuClick = { navController.navigate(Routes.SETTINGS) }
                )
            }

            composable(Routes.FRIEND_LIST) {
                FriendListScreen(
                    onBackClick = { navController.popBackStack() },
                    onEditClick = {},
                    onAddFriendClick = { navController.navigate(Routes.INVITE_FRIENDS) },
                    onFriendClick = { friendId ->
                        navController.navigate(Routes.friendDetail(routeKey(friendId)))
                    }
                )
            }

            composable(Routes.FRIEND_DETAIL) { backStackEntry ->
                val friendId = backStackEntry.stringArgument("friendId", "friend")
                FriendScreen(
                    onBackClick = { navController.popBackStack() },
                    onMessageClick = {
                        navController.navigate(Routes.chat(friendId))
                    },
                    onSettingsClick = {}
                )
            }

            composable(Routes.CHAT) {
                ChatScreen(
                    onBackClick = { navController.popBackStack() },
                    onAttachClick = {},
                    onSendClick = {}
                )
            }

            composable(Routes.INVITE_FRIENDS) {
                InviteFriendList2Screen(
                    onBackClick = { navController.popBackStack() },
                    onMessageClick = {},
                    onDiscoverPeopleClick = { navigateBottomTab(Routes.SEARCH) }
                )
            }

            composable(Routes.NO_FRIENDS) {
                NoFriendsScreen(
                    onBackClick = { navController.popBackStack() },
                    onMessageClick = { navController.navigate(Routes.INVITE_FRIENDS) },
                    onFacebookInviteClick = { navController.navigate(Routes.INVITE_FRIENDS) },
                    onInstagramInviteClick = { navController.navigate(Routes.INVITE_FRIENDS) }
                )
            }
        }

        if (showBottomBar) {
            CustomBottomBar(
                currentRoute = route,
                onItemClick = { selectedRoute ->
                    navigateBottomTab(selectedRoute)
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .zIndex(2f)
            )
        }
    }
}

private val UnsafeRouteCharacters = Regex("[^A-Za-z0-9_-]+")

private fun routeKey(value: String): String =
    value.trim()
        .replace(UnsafeRouteCharacters, "-")
        .trim('-')
        .lowercase()
        .ifBlank { "item" }

private fun NavBackStackEntry.stringArgument(key: String, defaultValue: String): String =
    arguments?.read { getStringOrNull(key) } ?: defaultValue
