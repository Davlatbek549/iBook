package com.example.dz.presentation.navigation


import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.savedstate.read
import com.example.dz.presentation.book.author_detail.AuthorDetailEffect
import com.example.dz.presentation.book.author_detail.AuthorDetailViewModel
import com.example.dz.presentation.book.author_detail.AuthorsDetailsScreen
import com.example.dz.presentation.book.category_detail.CategoryDetailEffect
import com.example.dz.presentation.book.category_detail.CategoryDetailViewModel
import com.example.dz.presentation.book.category_detail.CategoryDetailScreen
import com.example.dz.presentation.book.pre_purchase.PrePurchaseEffect
import com.example.dz.presentation.book.pre_purchase.PrePurchaseViewModel
import com.example.dz.presentation.book.pre_purchase.PrePurchaseScreen
import com.example.dz.presentation.book.review.BookReviewEffect
import com.example.dz.presentation.book.review.BookReviewViewModel
import com.example.dz.presentation.book.review.BookReviewScreen
import com.example.dz.presentation.social.chat.ChatEffect
import com.example.dz.presentation.social.chat.ChatScreen
import com.example.dz.presentation.social.chat.ChatViewModel
import com.example.dz.presentation.collections.details.CollectionDetails
import com.example.dz.presentation.collections.details.CollectionDetailsEffect
import com.example.dz.presentation.collections.details.CollectionDetailsViewModel
import com.example.dz.presentation.collections.list.CollectionsScreen
import com.example.dz.presentation.collections.list.CollectionsEffect
import com.example.dz.presentation.collections.list.CollectionsViewModel
import com.example.dz.presentation.collections.edit.CollectionsEdit
import com.example.dz.presentation.collections.edit.CollectionsEditEffect
import com.example.dz.presentation.collections.edit.CollectionsEditViewModel
import com.example.dz.presentation.social.friends.FriendListEffect
import com.example.dz.presentation.social.friends.FriendListScreen
import com.example.dz.presentation.social.friends.FriendListViewModel
import com.example.dz.presentation.auth.forgot_password.ForgotPasswordEffect
import com.example.dz.presentation.auth.forgot_password.ForgotPasswordScreen
import com.example.dz.presentation.auth.forgot_password.ForgotPasswordViewModel
import com.example.dz.presentation.social.friend_detail.FriendDetailEffect
import com.example.dz.presentation.social.friend_detail.FriendDetailViewModel
import com.example.dz.presentation.social.friend_detail.FriendScreen
import com.example.dz.presentation.goal.GoalEffect
import com.example.dz.presentation.goal.GoalScreen
import com.example.dz.presentation.goal.GoalViewModel
import com.example.dz.presentation.home.HomeEffect
import com.example.dz.presentation.home.HomeEvent
import com.example.dz.presentation.home.HomeScreen
import com.example.dz.presentation.home.HomeViewModel
import com.example.dz.presentation.social.invite_friends.InviteFriendList2Screen
import com.example.dz.presentation.social.invite_friends.InviteFriendsEffect
import com.example.dz.presentation.social.invite_friends.InviteFriendsViewModel
import com.example.dz.presentation.library.Library
import com.example.dz.presentation.library.LibraryEffect
import com.example.dz.presentation.library.LibraryEvent
import com.example.dz.presentation.library.LibraryViewModel
import com.example.dz.presentation.auth.login.LoginEffect
import com.example.dz.presentation.auth.login.LoginScreen
import com.example.dz.presentation.auth.login.LoginViewModel
import com.example.dz.presentation.membership.MembershipEffect
import com.example.dz.presentation.membership.MembershipScreen
import com.example.dz.presentation.membership.MembershipViewModel
import com.example.dz.presentation.social.no_friends.NoFriendsEffect
import com.example.dz.presentation.social.no_friends.NoFriendsScreen
import com.example.dz.presentation.social.no_friends.NoFriendsViewModel
import com.example.dz.presentation.notifications.NotificationsEffect
import com.example.dz.presentation.notifications.NotificationsScreen
import com.example.dz.presentation.notifications.NotificationsViewModel
import com.example.dz.presentation.common.SystemBackHandler
import com.example.dz.presentation.onboarding.OnboardingEffect
import com.example.dz.presentation.onboarding.OnboardingScreen
import com.example.dz.presentation.onboarding.OnboardingViewModel
import com.example.dz.presentation.payment.payment_failed.PaymentFailedEffect
import com.example.dz.presentation.payment.payment_failed.PaymentFailedViewModel
import com.example.dz.presentation.payment.payment_failed.PurchaseFailedScreen
import com.example.dz.presentation.payment.payment_methods.PaymentMethodsEffect
import com.example.dz.presentation.payment.payment_methods.PaymentMethodsScreen
import com.example.dz.presentation.payment.payment_methods.PaymentMethodsViewModel
import com.example.dz.presentation.payment.payment_success.PaymentSuccessEffect
import com.example.dz.presentation.payment.payment_success.PaymentSuccessViewModel
import com.example.dz.presentation.payment.payment_success.PurchaseSuccessScreen
import com.example.dz.presentation.premium_membership.PremiumMembershipEffect
import com.example.dz.presentation.premium_membership.PremiumMembershipScreen
import com.example.dz.presentation.premium_membership.PremiumMembershipViewModel
import com.example.dz.presentation.profile.ProfileEffect
import com.example.dz.presentation.profile.ProfileScreen
import com.example.dz.presentation.profile.ProfileViewModel
import com.example.dz.presentation.payment.purchase_confirmation.PurchaseConfirmationEffect
import com.example.dz.presentation.payment.purchase_confirmation.PurchaseConfirmationScreen
import com.example.dz.presentation.payment.purchase_confirmation.PurchaseConfirmationViewModel
import com.example.dz.presentation.payment.purchase_details.PurchaseDetailsEffect
import com.example.dz.presentation.payment.purchase_details.PurchaseDetailsScreen
import com.example.dz.presentation.payment.purchase_details.PurchaseDetailsViewModel
import com.example.dz.presentation.payment.purchase_receipt.PurchaseReceiptEffect
import com.example.dz.presentation.payment.purchase_receipt.PurchaseReceiptScreen
import com.example.dz.presentation.payment.purchase_receipt.PurchaseReceiptViewModel
import com.example.dz.presentation.reading.ReadingEffect
import com.example.dz.presentation.reading.ReadingScreen
import com.example.dz.presentation.reading.ReadingViewModel
import com.example.dz.presentation.search.SearchEffect
import com.example.dz.presentation.search.SearchScreen
import com.example.dz.presentation.search.SearchViewModel
import com.example.dz.presentation.settings.SettingsEffect
import com.example.dz.presentation.settings.SettingsScreen
import com.example.dz.presentation.settings.SettingsViewModel
import com.example.dz.presentation.auth.sign_up.SignUpEffect
import com.example.dz.presentation.auth.sign_up.SignUpScreen
import com.example.dz.presentation.auth.sign_up.SignUpViewModel
import com.example.dz.presentation.splash.SplashEffect
import com.example.dz.presentation.splash.SplashScreen
import com.example.dz.presentation.splash.SplashViewModel
import com.example.dz.presentation.store.StoreEffect
import com.example.dz.presentation.store.StoreEvent
import com.example.dz.presentation.store.StoreScreen
import com.example.dz.presentation.store.StoreViewModel
import com.example.dz.presentation.auth.new_password.NewPasswordEffect
import com.example.dz.presentation.auth.new_password.NewPasswordScreen
import com.example.dz.designsystem.components.organic.OrganicTabBar
import com.example.dz.designsystem.components.organic.organicBackdropSource
import com.example.dz.designsystem.components.organic.rememberOrganicBackdrop
import com.example.dz.presentation.auth.new_password.NewPasswordViewModel
import com.example.dz.presentation.auth.verification.VerificationEffect
import com.example.dz.presentation.auth.verification.VerificationEvent
import com.example.dz.presentation.auth.verification.VerificationPurpose
import com.example.dz.presentation.auth.verification.VerificationScreen
import com.example.dz.presentation.auth.verification.VerificationViewModel
import org.koin.mp.KoinPlatform
import org.koin.core.parameter.parametersOf

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
        Routes.ONBOARDING,
        Routes.LOGIN,
        Routes.SIGN_UP,
        Routes.FORGOT_PASSWORD,
        Routes.VERIFICATION,
        Routes.NEW_PASSWORD,
        Routes.PRE_PURCHASE,
        Routes.BOOK_REVIEW,
        Routes.AUTHOR_DETAIL,
        Routes.CATEGORY_DETAIL,
        Routes.COLLECTION_DETAIL,
        Routes.COLLECTIONS_EDIT,
        Routes.GOAL,
        Routes.FRIEND_DETAIL,
        Routes.CHAT,
        Routes.NOTIFICATIONS,
        Routes.INVITE_FRIENDS,
        Routes.NO_FRIENDS,
        Routes.SETTINGS,
        Routes.MEMBERSHIP,
        Routes.PREMIUM_MEMBERSHIP,
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

    /**
     * Switches tabs without growing the back stack.
     *
     * This used to pop up to the graph's start destination, which is Splash — and Splash is popped
     * inclusively the moment a session lands, so by the time anyone touches a tab that `popUpTo`
     * matched nothing and every switch pushed another entry. Four taps around the bar meant four
     * presses of back to leave.
     *
     * Home is the anchor instead: it is the first tab and what every route into the app lands on,
     * so back from any other tab returns to Home, and back from Home leaves. Each tab keeps its own
     * scroll position and stack through save/restore.
     */
    fun navigateBottomTab(selectedRoute: String) {
        navController.navigate(selectedRoute) {
            popUpTo(Routes.HOME) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    val tabBarBackdrop = rememberOrganicBackdrop()

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = Routes.SPLASH,
            modifier = Modifier
                .fillMaxSize()
                // Recording the screen into a layer costs something every frame, so only the
                // screens that actually carry the glass bar pay for it. Auth and pushed screens
                // draw straight through.
                .then(
                    if (showBottomBar) {
                        Modifier.organicBackdropSource(tabBarBackdrop)
                    } else {
                        Modifier
                    }
                )
        ) {
            composable(Routes.SPLASH) {
                val splashViewModel = koinViewModel<SplashViewModel>()

                LaunchedEffect(splashViewModel) {
                    splashViewModel.effects.collect { effect ->
                        when (effect) {
                            SplashEffect.NavigateToHome -> navController.navigate(Routes.HOME) {
                                popUpTo(Routes.SPLASH) { inclusive = true }
                            }
                            SplashEffect.NavigateToOnboarding -> navController.navigate(Routes.ONBOARDING) {
                                popUpTo(Routes.SPLASH) { inclusive = true }
                            }
                            SplashEffect.NavigateToLogin -> navController.navigate(Routes.login()) {
                                popUpTo(Routes.SPLASH) { inclusive = true }
                            }
                            is SplashEffect.NavigateToVerification -> navController.navigate(
                                Routes.verification(VerificationPurpose.VerifyEmail, effect.email)
                            ) {
                                popUpTo(Routes.SPLASH) { inclusive = true }
                            }
                        }
                    }
                }

                SplashScreen()
            }

            composable(Routes.ONBOARDING) {
                val onboardingViewModel = koinViewModel<OnboardingViewModel>()

                LaunchedEffect(onboardingViewModel) {
                    onboardingViewModel.effects.collect { effect ->
                        when (effect) {
                            OnboardingEffect.NavigateToSignUp -> navController.navigate(Routes.SIGN_UP) {
                                popUpTo(Routes.ONBOARDING) { inclusive = true }
                            }
                        }
                    }
                }

                OnboardingScreen(onEvent = onboardingViewModel::onEvent)
            }

            composable(
                Routes.LOGIN,
                // Declared with defaults so that navigating with neither still matches this
                // destination — signing out and a session-less splash both do.
                arguments = listOf(
                    navArgument("email") { type = NavType.StringType; defaultValue = "" },
                    navArgument("reset") { type = NavType.BoolType; defaultValue = false },
                ),
            ) { backStackEntry ->
                val email = backStackEntry.stringArgument("email", "")
                val passwordJustReset = backStackEntry.booleanArgument("reset", false)
                val loginViewModel = koinLoginViewModel(email, passwordJustReset)
                val uiState by loginViewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(loginViewModel) {
                    loginViewModel.effects.collect { effect ->
                        when (effect) {
                            LoginEffect.NavigateToHome -> navController.navigate(Routes.HOME) {
                                popUpTo(Routes.LOGIN) { inclusive = true }
                            }
                            // Sign-in stays underneath, so backing out leads somewhere useful:
                            // back to signing in, perhaps as someone else.
                            is LoginEffect.NavigateToVerification -> navController.navigate(
                                Routes.verification(VerificationPurpose.VerifyEmail, effect.email)
                            )
                            is LoginEffect.NavigateToForgotPassword ->
                                navController.navigate(Routes.forgotPassword(effect.email))
                            LoginEffect.NavigateToSignUp -> navController.navigate(Routes.SIGN_UP) {
                                popUpTo(Routes.LOGIN) { inclusive = true }
                            }
                        }
                    }
                }

                LoginScreen(
                    uiState = uiState,
                    onEvent = loginViewModel::onEvent
                )
            }

            composable(Routes.SIGN_UP) {
                val signUpViewModel = koinViewModel<SignUpViewModel>()
                val uiState by signUpViewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(signUpViewModel) {
                    signUpViewModel.effects.collect { effect ->
                        when (effect) {
                            SignUpEffect.NavigateToHome -> navController.navigate(Routes.HOME) {
                                popUpTo(0) { inclusive = true }
                            }
                            is SignUpEffect.NavigateToVerification ->
                                navController.navigate(
                                    Routes.verification(
                                        VerificationPurpose.VerifyEmail,
                                        effect.email,
                                        accountJustCreated = effect.accountJustCreated,
                                    )
                                )
                            SignUpEffect.NavigateToLogin -> navController.navigate(Routes.login()) {
                                popUpTo(Routes.SIGN_UP) { inclusive = true }
                            }
                        }
                    }
                }

                SignUpScreen(
                    uiState = uiState,
                    onEvent = signUpViewModel::onEvent
                )
            }

            composable(
                Routes.FORGOT_PASSWORD,
                arguments = listOf(
                    navArgument("email") { type = NavType.StringType; defaultValue = "" },
                ),
            ) { backStackEntry ->
                val forgotPasswordViewModel =
                    koinForgotPasswordViewModel(backStackEntry.stringArgument("email", ""))
                val uiState by forgotPasswordViewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(forgotPasswordViewModel) {
                    forgotPasswordViewModel.effects.collect { effect ->
                        when (effect) {
                            is ForgotPasswordEffect.NavigateToVerification ->
                                navController.navigate(
                                    Routes.verification(VerificationPurpose.ResetPassword, effect.email)
                                )
                            ForgotPasswordEffect.NavigateBack -> navController.popBackStack()
                        }
                    }
                }

                ForgotPasswordScreen(
                    uiState = uiState,
                    onEvent = forgotPasswordViewModel::onEvent
                )
            }

            composable(
                Routes.VERIFICATION,
                arguments = listOf(
                    navArgument("created") { type = NavType.BoolType; defaultValue = false },
                ),
            ) { backStackEntry ->
                val email = backStackEntry.stringArgument("email", "")
                val purpose = backStackEntry.stringArgument(
                    "purpose",
                    VerificationPurpose.VerifyEmail.name
                ).toVerificationPurpose()
                val accountJustCreated = backStackEntry.booleanArgument("created", false)
                val verificationViewModel =
                    koinVerificationViewModel(email, purpose, accountJustCreated)
                val uiState by verificationViewModel.uiState.collectAsStateWithLifecycle()

                // Backing out of a sign-up deletes the account the form just made, and the
                // system's back has to go the same way as the arrow. Left to the nav host, it
                // would pop the screen and leave the account behind.
                SystemBackHandler(enabled = accountJustCreated) {
                    verificationViewModel.onEvent(VerificationEvent.BackClicked)
                }

                LaunchedEffect(verificationViewModel) {
                    verificationViewModel.effects.collect { effect ->
                        when (effect) {
                            VerificationEffect.NavigateToHome -> navController.navigate(Routes.HOME) {
                                popUpTo(0) { inclusive = true }
                            }
                            is VerificationEffect.NavigateToNewPassword ->
                                navController.navigate(
                                    Routes.newPassword(effect.email, effect.code)
                                )
                            // Nothing to pop means the splash opened this screen directly, on a
                            // relaunch that found an unverified session. Backing out of that is
                            // giving the session up, which only the view model can do — so it is
                            // handed back rather than left as a button that does nothing.
                            VerificationEffect.NavigateBack ->
                                if (navController.previousBackStackEntry != null) {
                                    navController.popBackStack()
                                } else {
                                    verificationViewModel.onEvent(VerificationEvent.AbandonSession)
                                }
                            VerificationEffect.NavigateToLogin ->
                                navController.navigate(Routes.login()) {
                                    popUpTo(0) { inclusive = true }
                                }
                        }
                    }
                }

                VerificationScreen(
                    uiState = uiState,
                    onEvent = verificationViewModel::onEvent
                )
            }

            composable(Routes.NEW_PASSWORD) { backStackEntry ->
                val email = backStackEntry.stringArgument("email", "")
                val code = backStackEntry.stringArgument("code", "")
                val newPasswordViewModel = koinNewPasswordViewModel(email, code)
                val uiState by newPasswordViewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(newPasswordViewModel) {
                    newPasswordViewModel.effects.collect { effect ->
                        when (effect) {
                            // A reset issues no session, so this goes to sign-in, not Home. The
                            // whole reset stack goes with it: back into a spent code is a dead end.
                            // The address travels along, so the reader signs in without retyping
                            // the one they have just proved they own.
                            is NewPasswordEffect.NavigateToLogin ->
                                navController.navigate(
                                    Routes.login(effect.email, passwordJustReset = true)
                                ) {
                                    popUpTo(0) { inclusive = true }
                                }
                            NewPasswordEffect.NavigateBack -> navController.popBackStack()
                        }
                    }
                }

                NewPasswordScreen(
                    uiState = uiState,
                    onEvent = newPasswordViewModel::onEvent
                )
            }

            composable(Routes.HOME) {
                val homeViewModel = koinViewModel<HomeViewModel>()
                val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(homeViewModel) {
                    homeViewModel.effects.collect { effect ->
                        when (effect) {
                            is HomeEffect.NavigateToBook -> navController.navigate(Routes.prePurchase(effect.bookId))
                            is HomeEffect.NavigateToReading -> navController.navigate(Routes.reading(effect.bookId))
                            HomeEffect.NavigateToFriends -> navigateBottomTab(Routes.FRIEND_LIST)
                            HomeEffect.NavigateToGoal -> navController.navigate(Routes.GOAL)
                            is HomeEffect.NavigateToCategory ->
                                navController.navigate(Routes.categoryDetail(effect.categoryId))
                            // The handoff calls both routes to Profile intentional; they are the same
                            // destination, so the avatar switches tabs rather than pushing a second copy.
                            HomeEffect.NavigateToProfile -> navigateBottomTab(Routes.PROFILE_TAB)
                        }
                    }
                }

                LifecycleResumeEffect(homeViewModel) {
                    homeViewModel.onEvent(HomeEvent.Resumed)
                    onPauseOrDispose { }
                }

                HomeScreen(
                    uiState = uiState,
                    onKeepReadingClick = { homeViewModel.onEvent(HomeEvent.KeepReadingClicked) },
                    onSearchClick = { navigateBottomTab(Routes.SEARCH) },
                    onSeeAllClick = { navigateBottomTab(Routes.STORE) },
                    onBookClick = { bookId -> homeViewModel.onEvent(HomeEvent.BookClicked(bookId)) },
                    onPresenceClick = { homeViewModel.onEvent(HomeEvent.PresenceClicked) },
                    onGoalClick = { homeViewModel.onEvent(HomeEvent.GoalClicked) },
                    onCategoryClick = { id -> homeViewModel.onEvent(HomeEvent.CategoryClicked(id)) },
                    onProfileClick = { homeViewModel.onEvent(HomeEvent.ProfileClicked) }
                )
            }

            composable(Routes.LIBRARY) {
                val libraryViewModel = koinViewModel<LibraryViewModel>()
                val uiState by libraryViewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(libraryViewModel) {
                    libraryViewModel.effects.collect { effect ->
                        when (effect) {
                            is LibraryEffect.NavigateToBook -> navController.navigate(Routes.reading(effect.bookId))
                            LibraryEffect.NavigateToGoal -> navController.navigate(Routes.GOAL)
                            LibraryEffect.OpenSort -> Unit
                        }
                    }
                }

                Library(
                    uiState = uiState,
                    onSettingsClick = { navController.navigate(Routes.COLLECTIONS) },
                    onSortClick = { libraryViewModel.onEvent(LibraryEvent.SortClicked) },
                    onBookClick = { book ->
                        libraryViewModel.onEvent(LibraryEvent.BookClicked(book.id))
                    },
                    onGoalClick = { libraryViewModel.onEvent(LibraryEvent.GoalClicked) }
                )
            }

            composable(Routes.STORE) {
                val storeViewModel = koinViewModel<StoreViewModel>()
                val uiState by storeViewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(storeViewModel) {
                    storeViewModel.effects.collect { effect ->
                        when (effect) {
                            is StoreEffect.NavigateToBook -> navController.navigate(Routes.prePurchase(effect.bookId))
                            is StoreEffect.NavigateToCategory -> navController.navigate(Routes.categoryDetail(effect.categoryId))
                            StoreEffect.NavigateToMembership -> navController.navigate(Routes.MEMBERSHIP)
                        }
                    }
                }

                StoreScreen(
                    uiState = uiState,
                    onViewMoreClick = { storeViewModel.onEvent(StoreEvent.ViewMoreClicked) },
                    onCategoryClick = { categoryName ->
                        storeViewModel.onEvent(StoreEvent.CategoryClicked(categoryName))
                    },
                    onBookClick = { book ->
                        storeViewModel.onEvent(StoreEvent.BookClicked(book.id))
                    }
                )
            }

            composable(Routes.SEARCH) {
                val searchViewModel = koinViewModel<SearchViewModel>()
                val uiState by searchViewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(searchViewModel) {
                    searchViewModel.effects.collect { effect ->
                        when (effect) {
                            is SearchEffect.NavigateToBook -> navController.navigate(Routes.prePurchase(effect.bookId))
                            is SearchEffect.NavigateToAuthor -> navController.navigate(Routes.authorDetail(effect.authorId))
                            is SearchEffect.NavigateToCategory -> navController.navigate(Routes.categoryDetail(effect.categoryId))
                        }
                    }
                }

                SearchScreen(
                    uiState = uiState,
                    onEvent = searchViewModel::onEvent,
                    onSearchFocusChange = { isSearchFocused = it },
                    onCategoryClick = {},
                    onBookClick = {},
                    onAuthorClick = {}
                )
            }

            composable(Routes.PROFILE_TAB) {
                val profileViewModel = koinViewModel<ProfileViewModel>()
                val uiState by profileViewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(profileViewModel) {
                    profileViewModel.effects.collect { effect ->
                        when (effect) {
                            ProfileEffect.NavigateBack -> navController.popBackStack()
                            ProfileEffect.NavigateToNotifications -> navController.navigate(Routes.NOTIFICATIONS)
                            ProfileEffect.NavigateToFriends -> navigateBottomTab(Routes.FRIEND_LIST)
                            ProfileEffect.NavigateToGoals -> navController.navigate(Routes.GOAL)
                            ProfileEffect.NavigateToCollections -> navController.navigate(Routes.COLLECTIONS)
                            ProfileEffect.NavigateToPurchases -> navController.navigate(Routes.purchaseReceipt("history"))
                            ProfileEffect.NavigateToMembership -> navController.navigate(Routes.PREMIUM_MEMBERSHIP)
                            ProfileEffect.NavigateToSettings -> navController.navigate(Routes.SETTINGS)
                        }
                    }
                }

                ProfileScreen(
                    uiState = uiState,
                    onEvent = profileViewModel::onEvent
                )
            }

            composable(Routes.PRE_PURCHASE) { backStackEntry ->
                val bookId = backStackEntry.stringArgument("bookId", "book")
                val prePurchaseViewModel = koinPrePurchaseViewModel(bookId)
                val uiState by prePurchaseViewModel.uiState.collectAsStateWithLifecycle()

                // Refresh the offline badge when returning from the reader after a download/delete.
                LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
                    prePurchaseViewModel.refreshDownloadState()
                }

                LaunchedEffect(prePurchaseViewModel) {
                    prePurchaseViewModel.effects.collect { effect ->
                        when (effect) {
                            PrePurchaseEffect.NavigateBack -> navController.popBackStack()
                            is PrePurchaseEffect.NavigateToReading -> navController.navigate(Routes.reading(effect.bookId))
                            is PrePurchaseEffect.NavigateToPurchase -> navController.navigate(Routes.purchaseDetails(effect.bookId))
                            is PrePurchaseEffect.NavigateToAuthor -> navController.navigate(Routes.authorDetail(effect.authorId))
                            is PrePurchaseEffect.NavigateToBook -> navController.navigate(Routes.prePurchase(effect.bookId))
                        }
                    }
                }

                PrePurchaseScreen(
                    uiState = uiState,
                    onEvent = prePurchaseViewModel::onEvent
                )
            }

            composable(Routes.READING) { backStackEntry ->
                val bookId = backStackEntry.stringArgument("bookId", "book")
                val readingViewModel = koinReadingViewModel(bookId)
                val uiState by readingViewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(readingViewModel) {
                    readingViewModel.effects.collect { effect ->
                        when (effect) {
                            ReadingEffect.NavigateBack -> navController.popBackStack()
                            ReadingEffect.NavigateToSettings -> navController.navigate(Routes.SETTINGS)
                            is ReadingEffect.NavigateToComments -> navController.navigate(Routes.bookReview(effect.bookId))
                        }
                    }
                }

                ReadingScreen(
                    uiState = uiState,
                    onEvent = readingViewModel::onEvent
                )
            }

            composable(Routes.BOOK_REVIEW) { backStackEntry ->
                val bookId = backStackEntry.stringArgument("bookId", "book")
                val bookReviewViewModel = koinBookReviewViewModel(bookId)
                val uiState by bookReviewViewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(bookReviewViewModel) {
                    bookReviewViewModel.effects.collect { effect ->
                        when (effect) {
                            BookReviewEffect.NavigateBack -> navController.popBackStack()
                            is BookReviewEffect.NavigateToReading -> navController.navigate(Routes.reading(effect.bookId))
                        }
                    }
                }

                BookReviewScreen(
                    uiState = uiState,
                    onEvent = bookReviewViewModel::onEvent
                )
            }

            composable(Routes.PURCHASE_DETAILS) { backStackEntry ->
                val bookId = backStackEntry.stringArgument("bookId", "book")
                val purchaseDetailsViewModel = koinPurchaseDetailsViewModel(bookId)
                val uiState by purchaseDetailsViewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(purchaseDetailsViewModel) {
                    purchaseDetailsViewModel.effects.collect { effect ->
                        when (effect) {
                            PurchaseDetailsEffect.NavigateBack -> navController.popBackStack()
                            PurchaseDetailsEffect.NavigateToPaymentMethods -> navController.navigate(Routes.PAYMENT_METHODS)
                            PurchaseDetailsEffect.NavigateToConfirmation -> navController.navigate(Routes.PURCHASE_CONFIRMATION)
                        }
                    }
                }

                PurchaseDetailsScreen(
                    uiState = uiState,
                    onEvent = purchaseDetailsViewModel::onEvent
                )
            }

            composable(Routes.PURCHASE_RECEIPT) { backStackEntry ->
                val bookId = backStackEntry.stringArgument("bookId", "book")
                val purchaseReceiptViewModel = koinPurchaseReceiptViewModel(bookId)
                val uiState by purchaseReceiptViewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(purchaseReceiptViewModel) {
                    purchaseReceiptViewModel.effects.collect { effect ->
                        when (effect) {
                            PurchaseReceiptEffect.NavigateBack -> navController.popBackStack()
                            is PurchaseReceiptEffect.NavigateToReading -> navController.navigate(Routes.reading(effect.bookId))
                        }
                    }
                }

                PurchaseReceiptScreen(
                    uiState = uiState,
                    onEvent = purchaseReceiptViewModel::onEvent
                )
            }

            composable(Routes.PAYMENT_METHODS) {
                val paymentMethodsViewModel = koinViewModel<PaymentMethodsViewModel>()
                val uiState by paymentMethodsViewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(paymentMethodsViewModel) {
                    paymentMethodsViewModel.effects.collect { effect ->
                        when (effect) {
                            PaymentMethodsEffect.NavigateBack -> navController.popBackStack()
                            PaymentMethodsEffect.NavigateToFailure -> navController.navigate(Routes.PAYMENT_FAILED)
                            PaymentMethodsEffect.NavigateToSuccess -> navController.navigate(Routes.PAYMENT_SUCCESS) {
                                popUpTo(Routes.PRE_PURCHASE) { inclusive = true }
                            }
                        }
                    }
                }

                PaymentMethodsScreen(
                    uiState = uiState,
                    onEvent = paymentMethodsViewModel::onEvent
                )
            }

            composable(Routes.PURCHASE_CONFIRMATION) {
                val purchaseConfirmationViewModel = koinViewModel<PurchaseConfirmationViewModel>()
                val uiState by purchaseConfirmationViewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(purchaseConfirmationViewModel) {
                    purchaseConfirmationViewModel.effects.collect { effect ->
                        when (effect) {
                            PurchaseConfirmationEffect.NavigateToPaymentMethods -> navController.navigate(Routes.PAYMENT_METHODS)
                            PurchaseConfirmationEffect.NavigateBack -> navController.popBackStack()
                        }
                    }
                }

                PurchaseConfirmationScreen(
                    uiState = uiState,
                    onEvent = purchaseConfirmationViewModel::onEvent
                )
            }

            composable(Routes.PAYMENT_SUCCESS) {
                val paymentSuccessViewModel = koinViewModel<PaymentSuccessViewModel>()
                val uiState by paymentSuccessViewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(paymentSuccessViewModel) {
                    paymentSuccessViewModel.effects.collect { effect ->
                        when (effect) {
                            PaymentSuccessEffect.NavigateToHome -> navController.navigate(Routes.HOME) {
                                popUpTo(Routes.HOME) { inclusive = false }
                            }
                            PaymentSuccessEffect.NavigateToReading -> navController.navigate(Routes.reading("purchased-book")) {
                                popUpTo(Routes.HOME) { inclusive = false }
                            }
                        }
                    }
                }

                PurchaseSuccessScreen(
                    uiState = uiState,
                    onEvent = paymentSuccessViewModel::onEvent
                )
            }

            composable(Routes.PAYMENT_FAILED) {
                val paymentFailedViewModel = koinViewModel<PaymentFailedViewModel>()
                val uiState by paymentFailedViewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(paymentFailedViewModel) {
                    paymentFailedViewModel.effects.collect { effect ->
                        when (effect) {
                            PaymentFailedEffect.NavigateBack -> navController.popBackStack()
                            // Change payment → back to the methods picker
                            PaymentFailedEffect.NavigateToPaymentMethods ->
                                navController.popBackStack(Routes.PAYMENT_METHODS, inclusive = false)
                            // Retry → back to Review order
                            PaymentFailedEffect.NavigateToPurchaseDetails ->
                                navController.popBackStack(Routes.PURCHASE_DETAILS, inclusive = false)
                        }
                    }
                }

                PurchaseFailedScreen(
                    uiState = uiState,
                    onEvent = paymentFailedViewModel::onEvent
                )
            }

            composable(Routes.CATEGORY_DETAIL) { backStackEntry ->
                val categoryId = backStackEntry.stringArgument("categoryName", "horror")
                val categoryDetailViewModel = koinCategoryDetailViewModel(categoryId)
                val uiState by categoryDetailViewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(categoryDetailViewModel) {
                    categoryDetailViewModel.effects.collect { effect ->
                        when (effect) {
                            CategoryDetailEffect.NavigateBack -> navController.popBackStack()
                            is CategoryDetailEffect.NavigateToBook -> navController.navigate(Routes.prePurchase(effect.bookId))
                        }
                    }
                }

                CategoryDetailScreen(
                    uiState = uiState,
                    onEvent = categoryDetailViewModel::onEvent
                )
            }

            composable(Routes.AUTHOR_DETAIL) { backStackEntry ->
                val authorId = backStackEntry.stringArgument("authorId", "author")
                val authorViewModel = koinAuthorDetailViewModel(authorId)
                val uiState by authorViewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(authorViewModel) {
                    authorViewModel.effects.collect { effect ->
                        when (effect) {
                            AuthorDetailEffect.NavigateBack -> navController.popBackStack()
                            is AuthorDetailEffect.NavigateToBook ->
                                navController.navigate(Routes.prePurchase(effect.bookId))
                        }
                    }
                }

                AuthorsDetailsScreen(
                    uiState = uiState,
                    onEvent = authorViewModel::onEvent
                )
            }

            composable(Routes.COLLECTIONS) {
                val collectionsViewModel = koinViewModel<CollectionsViewModel>()
                val uiState by collectionsViewModel.uiState.collectAsStateWithLifecycle()

                // Refresh when this entry comes back to the foreground (e.g. after "New collection").
                LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
                    collectionsViewModel.refresh()
                }

                LaunchedEffect(collectionsViewModel) {
                    collectionsViewModel.effects.collect { effect ->
                        when (effect) {
                            CollectionsEffect.NavigateBack -> navController.popBackStack()
                            is CollectionsEffect.NavigateToDetail ->
                                navController.navigate(Routes.collectionDetail(routeKey(effect.collectionId)))
                            is CollectionsEffect.NavigateToEdit ->
                                navController.navigate(Routes.collectionsEdit(effect.collectionId))
                        }
                    }
                }

                CollectionsScreen(
                    uiState = uiState,
                    onEvent = collectionsViewModel::onEvent
                )
            }

            composable(Routes.COLLECTION_DETAIL) { backStackEntry ->
                val collectionId = backStackEntry.stringArgument("collectionId", "collection")
                val collectionDetailsViewModel = koinCollectionDetailsViewModel(collectionId)
                val uiState by collectionDetailsViewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(collectionDetailsViewModel) {
                    collectionDetailsViewModel.effects.collect { effect ->
                        when (effect) {
                            CollectionDetailsEffect.NavigateBack -> navController.popBackStack()
                            is CollectionDetailsEffect.NavigateToEdit ->
                                navController.navigate(Routes.collectionsEdit(effect.collectionId))
                            is CollectionDetailsEffect.NavigateToBook ->
                                navController.navigate(Routes.prePurchase(routeKey(effect.bookId)))
                        }
                    }
                }

                CollectionDetails(
                    uiState = uiState,
                    onEvent = collectionDetailsViewModel::onEvent
                )
            }

            composable(Routes.COLLECTIONS_EDIT) { backStackEntry ->
                val collectionId = backStackEntry.stringArgument("collectionId", "new")
                val collectionsEditViewModel = koinCollectionsEditViewModel(collectionId)
                val uiState by collectionsEditViewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(collectionsEditViewModel) {
                    collectionsEditViewModel.effects.collect { effect ->
                        when (effect) {
                            CollectionsEditEffect.NavigateBack -> navController.popBackStack()
                        }
                    }
                }

                CollectionsEdit(
                    uiState = uiState,
                    onEvent = collectionsEditViewModel::onEvent
                )
            }

            composable(Routes.SETTINGS) {
                val settingsViewModel = koinViewModel<SettingsViewModel>()
                val uiState by settingsViewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(settingsViewModel) {
                    settingsViewModel.effects.collect { effect ->
                        when (effect) {
                            SettingsEffect.NavigateBack -> navController.popBackStack()
                            // "Edit profile" reuses the purchase-history destination (existing behavior)
                            SettingsEffect.NavigateToEditProfile -> navController.navigate(Routes.purchaseDetails("history"))
                            // Clear the whole stack: every screen behind this one belongs to
                            // the session that was just signed out.
                            SettingsEffect.NavigateToLogin -> navController.navigate(Routes.login()) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    }
                }

                SettingsScreen(
                    uiState = uiState,
                    onEvent = settingsViewModel::onEvent
                )
            }

            composable(Routes.NOTIFICATIONS) {
                val notificationsViewModel = koinViewModel<NotificationsViewModel>()
                val uiState by notificationsViewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(notificationsViewModel) {
                    notificationsViewModel.effects.collect { effect ->
                        when (effect) {
                            NotificationsEffect.NavigateBack -> navController.popBackStack()
                            is NotificationsEffect.NavigateToChat -> navigateBottomTab(Routes.FRIEND_LIST)
                        }
                    }
                }

                NotificationsScreen(
                    uiState = uiState,
                    onEvent = notificationsViewModel::onEvent
                )
            }

            composable(Routes.GOAL) {
                val goalViewModel = koinViewModel<GoalViewModel>()
                val uiState by goalViewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(goalViewModel) {
                    goalViewModel.effects.collect { effect ->
                        when (effect) {
                            GoalEffect.NavigateBack -> navController.popBackStack()
                            GoalEffect.NavigateToSettings -> navController.navigate(Routes.SETTINGS)
                        }
                    }
                }

                GoalScreen(
                    uiState = uiState,
                    onEvent = goalViewModel::onEvent
                )
            }

            composable(Routes.MEMBERSHIP) {
                val membershipViewModel = koinViewModel<MembershipViewModel>()
                val uiState by membershipViewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(membershipViewModel) {
                    membershipViewModel.effects.collect { effect ->
                        when (effect) {
                            MembershipEffect.NavigateBack -> navController.popBackStack()
                            MembershipEffect.NavigateToPremium -> navController.navigate(Routes.PREMIUM_MEMBERSHIP)
                        }
                    }
                }

                MembershipScreen(
                    uiState = uiState,
                    onEvent = membershipViewModel::onEvent
                )
            }

            composable(Routes.PREMIUM_MEMBERSHIP) {
                val premiumMembershipViewModel = koinViewModel<PremiumMembershipViewModel>()
                val uiState by premiumMembershipViewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(premiumMembershipViewModel) {
                    premiumMembershipViewModel.effects.collect { effect ->
                        when (effect) {
                            PremiumMembershipEffect.NavigateBack -> navController.popBackStack()
                            PremiumMembershipEffect.NavigateToSettings -> navController.navigate(Routes.SETTINGS)
                        }
                    }
                }

                PremiumMembershipScreen(
                    uiState = uiState,
                    onEvent = premiumMembershipViewModel::onEvent
                )
            }

            composable(Routes.FRIEND_LIST) {
                val friendListViewModel = koinViewModel<FriendListViewModel>()
                val uiState by friendListViewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(friendListViewModel) {
                    friendListViewModel.effects.collect { effect ->
                        when (effect) {
                            FriendListEffect.NavigateBack -> navController.popBackStack()
                            FriendListEffect.NavigateToInvite -> navController.navigate(Routes.INVITE_FRIENDS)
                            is FriendListEffect.NavigateToFriendDetail ->
                                navController.navigate(Routes.friendDetail(routeKey(effect.friendId)))
                        }
                    }
                }

                FriendListScreen(
                    uiState = uiState,
                    onEvent = friendListViewModel::onEvent
                )
            }

            composable(Routes.FRIEND_DETAIL) { backStackEntry ->
                val friendId = backStackEntry.stringArgument("friendId", "friend")
                val friendDetailViewModel = koinFriendDetailViewModel(friendId)
                val uiState by friendDetailViewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(friendDetailViewModel) {
                    friendDetailViewModel.effects.collect { effect ->
                        when (effect) {
                            FriendDetailEffect.NavigateBack -> navController.popBackStack()
                            is FriendDetailEffect.NavigateToChat -> navController.navigate(Routes.chat(effect.friendId))
                        }
                    }
                }

                FriendScreen(
                    uiState = uiState,
                    onEvent = friendDetailViewModel::onEvent
                )
            }

            composable(Routes.CHAT) { backStackEntry ->
                val friendId = backStackEntry.stringArgument("friendId", "friend")
                val chatViewModel = koinChatViewModel(friendId)
                val uiState by chatViewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(chatViewModel) {
                    chatViewModel.effects.collect { effect ->
                        when (effect) {
                            ChatEffect.NavigateBack -> navController.popBackStack()
                        }
                    }
                }

                ChatScreen(
                    uiState = uiState,
                    onEvent = chatViewModel::onEvent
                )
            }

            composable(Routes.INVITE_FRIENDS) {
                val inviteFriendsViewModel = koinViewModel<InviteFriendsViewModel>()
                val uiState by inviteFriendsViewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(inviteFriendsViewModel) {
                    inviteFriendsViewModel.effects.collect { effect ->
                        when (effect) {
                            InviteFriendsEffect.NavigateBack -> navController.popBackStack()
                            InviteFriendsEffect.NavigateToDiscover -> navigateBottomTab(Routes.SEARCH)
                        }
                    }
                }

                InviteFriendList2Screen(
                    uiState = uiState,
                    onEvent = inviteFriendsViewModel::onEvent
                )
            }

            composable(Routes.NO_FRIENDS) {
                val noFriendsViewModel = koinViewModel<NoFriendsViewModel>()

                LaunchedEffect(noFriendsViewModel) {
                    noFriendsViewModel.effects.collect { effect ->
                        when (effect) {
                            NoFriendsEffect.NavigateBack -> navController.popBackStack()
                            NoFriendsEffect.NavigateToInvite -> navController.navigate(Routes.INVITE_FRIENDS)
                        }
                    }
                }

                NoFriendsScreen(
                    onEvent = noFriendsViewModel::onEvent
                )
            }
        }

        if (showBottomBar) {
            OrganicTabBar(
                tabs = bottomNavItems,
                currentRoute = route,
                backdrop = tabBarBackdrop,
                onTabClick = { selectedRoute ->
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

private fun NavBackStackEntry.booleanArgument(key: String, defaultValue: Boolean): Boolean =
    arguments?.read { if (contains(key)) getBoolean(key) else defaultValue } ?: defaultValue

@Composable
private inline fun <reified VM : ViewModel> koinViewModel(): VM {
    val koin = remember { KoinPlatform.getKoin() }
    return viewModel { koin.get<VM>() }
}

@Composable
private fun koinLoginViewModel(email: String, passwordJustReset: Boolean): LoginViewModel {
    val koin = remember { KoinPlatform.getKoin() }
    return viewModel(key = "login-$email-$passwordJustReset") {
        koin.get<LoginViewModel> { parametersOf(email, passwordJustReset) }
    }
}

@Composable
private fun koinForgotPasswordViewModel(email: String): ForgotPasswordViewModel {
    val koin = remember { KoinPlatform.getKoin() }
    return viewModel(key = "forgot-password-$email") {
        koin.get<ForgotPasswordViewModel> { parametersOf(email) }
    }
}

@Composable
private fun koinAuthorDetailViewModel(authorId: String): AuthorDetailViewModel {
    val koin = remember { KoinPlatform.getKoin() }
    return viewModel(key = "author-$authorId") {
        koin.get<AuthorDetailViewModel> { parametersOf(authorId) }
    }
}

@Composable
private fun koinPrePurchaseViewModel(bookId: String): PrePurchaseViewModel {
    val koin = remember { KoinPlatform.getKoin() }
    return viewModel(key = "pre-purchase-$bookId") {
        koin.get<PrePurchaseViewModel> { parametersOf(bookId) }
    }
}

@Composable
private fun koinVerificationViewModel(
    email: String,
    purpose: VerificationPurpose,
    accountJustCreated: Boolean,
): VerificationViewModel {
    val koin = remember { KoinPlatform.getKoin() }
    return viewModel(key = "verification-${purpose.name}-$email") {
        koin.get<VerificationViewModel> { parametersOf(email, purpose, accountJustCreated) }
    }
}

@Composable
private fun koinNewPasswordViewModel(email: String, code: String): NewPasswordViewModel {
    val koin = remember { KoinPlatform.getKoin() }
    return viewModel(key = "new-password-$email") {
        koin.get<NewPasswordViewModel> { parametersOf(email, code) }
    }
}

/** An unknown or missing value falls back to the safer of the two — verifying, not resetting. */
private fun String.toVerificationPurpose(): VerificationPurpose =
    VerificationPurpose.entries.firstOrNull { it.name == this } ?: VerificationPurpose.VerifyEmail

@Composable
private fun koinBookReviewViewModel(bookId: String): BookReviewViewModel {
    val koin = remember { KoinPlatform.getKoin() }
    return viewModel(key = "book-review-$bookId") {
        koin.get<BookReviewViewModel> { parametersOf(bookId) }
    }
}

@Composable
private fun koinCategoryDetailViewModel(categoryId: String): CategoryDetailViewModel {
    val koin = remember { KoinPlatform.getKoin() }
    return viewModel(key = "category-detail-$categoryId") {
        koin.get<CategoryDetailViewModel> { parametersOf(categoryId) }
    }
}

@Composable
private fun koinCollectionDetailsViewModel(collectionId: String): CollectionDetailsViewModel {
    val koin = remember { KoinPlatform.getKoin() }
    return viewModel(key = "collection-details-$collectionId") {
        koin.get<CollectionDetailsViewModel> { parametersOf(collectionId) }
    }
}

@Composable
private fun koinCollectionsEditViewModel(collectionId: String): CollectionsEditViewModel {
    val koin = remember { KoinPlatform.getKoin() }
    return viewModel(key = "collections-edit-$collectionId") {
        koin.get<CollectionsEditViewModel> { parametersOf(collectionId) }
    }
}

@Composable
private fun koinPurchaseDetailsViewModel(bookId: String): PurchaseDetailsViewModel {
    val koin = remember { KoinPlatform.getKoin() }
    return viewModel(key = "purchase-details-$bookId") {
        koin.get<PurchaseDetailsViewModel> { parametersOf(bookId) }
    }
}

@Composable
private fun koinPurchaseReceiptViewModel(bookId: String): PurchaseReceiptViewModel {
    val koin = remember { KoinPlatform.getKoin() }
    return viewModel(key = "purchase-receipt-$bookId") {
        koin.get<PurchaseReceiptViewModel> { parametersOf(bookId) }
    }
}

@Composable
private fun koinReadingViewModel(bookId: String): ReadingViewModel {
    val koin = remember { KoinPlatform.getKoin() }
    return viewModel(key = "reading-$bookId") {
        koin.get<ReadingViewModel> { parametersOf(bookId) }
    }
}

@Composable
private fun koinFriendDetailViewModel(friendId: String): FriendDetailViewModel {
    val koin = remember { KoinPlatform.getKoin() }
    return viewModel(key = "friend-detail-$friendId") {
        koin.get<FriendDetailViewModel> { parametersOf(friendId) }
    }
}

@Composable
private fun koinChatViewModel(friendId: String): ChatViewModel {
    val koin = remember { KoinPlatform.getKoin() }
    return viewModel(key = "chat-$friendId") {
        koin.get<ChatViewModel> { parametersOf(friendId) }
    }
}
