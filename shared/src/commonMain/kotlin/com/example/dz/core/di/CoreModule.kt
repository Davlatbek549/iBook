package com.example.dz.core.di

import com.example.dz.data.local.CollectionLocalDataSource
import com.example.dz.data.local.LibraryLocalDataSource
import com.example.dz.data.local.LocalDataSource
import com.example.dz.data.local.LocalDataSourceImpl
import com.example.dz.data.local.db.createDatabase
import com.example.dz.data.local.file.FileStorage
import com.example.dz.data.remote.api.ApiConfig
import com.example.dz.data.remote.api.AuthApi
import com.example.dz.data.remote.api.GutendexApi
import com.example.dz.data.remote.api.KtorAuthApi
import com.example.dz.data.remote.api.KtorGutendexApi
import com.example.dz.data.remote.api.KtorOpenLibraryApi
import com.example.dz.data.remote.api.OpenLibraryApi
import com.example.dz.data.remote.api.createAuthHttpClient
import com.example.dz.data.remote.api.createRemoteHttpClient
import com.example.dz.data.repository.ChatRepositoryImpl
import com.example.dz.data.repository.DownloadRepositoryImpl
import com.example.dz.data.repository.LocalCollectionRepository
import com.example.dz.data.repository.LocalLibraryRepository
import com.example.dz.data.repository.MembershipRepositoryImpl
import com.example.dz.data.repository.NotificationRepositoryImpl
import com.example.dz.data.repository.PaymentRepositoryImpl
import com.example.dz.data.repository.RemoteAuthRepository
import com.example.dz.data.repository.RemoteBookRepository
import com.example.dz.data.repository.SocialRepositoryImpl
import com.example.dz.data.repository.UserRepositoryImpl
import com.example.dz.domain.repository.AuthRepository
import com.example.dz.domain.repository.BookRepository
import com.example.dz.domain.repository.ChatRepository
import com.example.dz.domain.repository.CollectionRepository
import com.example.dz.domain.repository.DownloadRepository
import com.example.dz.domain.repository.LibraryRepository
import com.example.dz.domain.repository.MembershipRepository
import com.example.dz.domain.repository.NotificationRepository
import com.example.dz.domain.repository.PaymentRepository
import com.example.dz.domain.repository.SocialRepository
import com.example.dz.domain.repository.UserRepository
import com.example.dz.domain.usecase.auth.GetCurrentUserUseCase
import com.example.dz.domain.usecase.auth.LoginUseCase
import com.example.dz.domain.usecase.auth.LogoutUseCase
import com.example.dz.domain.usecase.auth.SignUpUseCase
import com.example.dz.domain.usecase.book.BookPaginator
import com.example.dz.domain.usecase.book.GetBookContentUseCase
import com.example.dz.domain.usecase.book.GetBookDetailsUseCase
import com.example.dz.domain.usecase.book.GetBooksByCategoryUseCase
import com.example.dz.domain.usecase.book.GetCategoriesUseCase
import com.example.dz.domain.usecase.book.GetHomeBooksUseCase
import com.example.dz.domain.usecase.book.SearchBooksUseCase
import com.example.dz.domain.usecase.book.DeleteDownloadUseCase
import com.example.dz.domain.usecase.book.DownloadBookUseCase
import com.example.dz.domain.usecase.chat.GetChatsUseCase
import com.example.dz.domain.usecase.chat.GetMessagesUseCase
import com.example.dz.domain.usecase.chat.SendMessageUseCase
import com.example.dz.domain.usecase.collection.CreateCollectionUseCase
import com.example.dz.domain.usecase.collection.DeleteCollectionUseCase
import com.example.dz.domain.usecase.collection.GetCollectionDetailsUseCase
import com.example.dz.domain.usecase.collection.GetCollectionsUseCase
import com.example.dz.domain.usecase.collection.UpdateCollectionUseCase
import com.example.dz.domain.usecase.library.GetContinueReadingUseCase
import com.example.dz.domain.usecase.library.GetLibraryBooksUseCase
import com.example.dz.domain.usecase.library.UpdateReadingProgressUseCase
import com.example.dz.domain.usecase.membership.GetCurrentMembershipUseCase
import com.example.dz.domain.usecase.membership.GetMembershipPlansUseCase
import com.example.dz.domain.usecase.notification.GetNotificationsUseCase
import com.example.dz.domain.usecase.notification.MarkNotificationAsReadUseCase
import com.example.dz.domain.usecase.payment.GetPaymentMethodsUseCase
import com.example.dz.domain.usecase.payment.GetPurchaseDetailsUseCase
import com.example.dz.domain.usecase.payment.PurchaseBookUseCase
import com.example.dz.domain.usecase.social.GetFriendDetailsUseCase
import com.example.dz.domain.usecase.social.GetFriendsUseCase
import com.example.dz.domain.usecase.social.InviteFriendUseCase
import com.example.dz.domain.usecase.user.GetProfileUseCase
import com.example.dz.domain.usecase.user.UpdateProfileUseCase
import com.example.dz.presentation.auth.forgot_password.ForgotPasswordViewModel
import com.example.dz.presentation.auth.login.LoginViewModel
import com.example.dz.presentation.auth.sign_up.SignUpViewModel
import com.example.dz.presentation.auth.verification.VerificationViewModel
import com.example.dz.presentation.book.author_detail.AuthorDetailViewModel
import com.example.dz.presentation.book.category_detail.CategoryDetailViewModel
import com.example.dz.presentation.book.pre_purchase.PrePurchaseViewModel
import com.example.dz.presentation.book.review.BookReviewViewModel
import com.example.dz.presentation.collections.details.CollectionDetailsViewModel
import com.example.dz.presentation.collections.edit.CollectionsEditViewModel
import com.example.dz.presentation.collections.list.CollectionsViewModel
import com.example.dz.presentation.goal.GoalViewModel
import com.example.dz.presentation.home.HomeViewModel
import com.example.dz.presentation.membership.MembershipViewModel
import com.example.dz.presentation.notifications.NotificationsViewModel
import com.example.dz.presentation.onboarding.onboarding_one.OnboardingOneViewModel
import com.example.dz.presentation.onboarding.onboarding_three.OnboardingThreeViewModel
import com.example.dz.presentation.onboarding.onboarding_two.OnboardingTwoViewModel
import com.example.dz.presentation.payment.payment_failed.PaymentFailedViewModel
import com.example.dz.presentation.payment.payment_methods.PaymentMethodsViewModel
import com.example.dz.presentation.payment.payment_success.PaymentSuccessViewModel
import com.example.dz.presentation.payment.purchase_confirmation.PurchaseConfirmationViewModel
import com.example.dz.presentation.payment.purchase_details.PurchaseDetailsViewModel
import com.example.dz.presentation.payment.purchase_receipt.PurchaseReceiptViewModel
import com.example.dz.presentation.premium_membership.PremiumMembershipViewModel
import com.example.dz.presentation.profile.ProfileViewModel
import com.example.dz.presentation.reading.ReadingViewModel
import com.example.dz.presentation.settings.SettingsViewModel
import com.example.dz.presentation.social.chat.ChatViewModel
import com.example.dz.presentation.social.friend_detail.FriendDetailViewModel
import com.example.dz.presentation.social.friends.FriendListViewModel
import com.example.dz.presentation.social.invite_friends.InviteFriendsViewModel
import com.example.dz.presentation.social.no_friends.NoFriendsViewModel
import com.example.dz.presentation.library.LibraryViewModel
import com.example.dz.presentation.search.SearchViewModel
import com.example.dz.presentation.store.StoreViewModel
import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.mp.KoinPlatform

fun startKoinIfNeeded() {
    if (KoinPlatform.getKoinOrNull() == null) {
        startKoin {
            modules(coreModule, platformModule)
        }
    }
}

/** Qualifier for the authenticated app-API client, distinct from the public book client. */
private const val AUTH_HTTP_CLIENT = "authHttpClient"

val coreModule = module {

    // ── Local storage ────────────────────────────────────────────────────────
    single { Settings() }
    single<LocalDataSource> { LocalDataSourceImpl(get()) }

    // ── SQLDelight database (DatabaseDriverFactory + FileStorage come from platformModule) ──
    single { createDatabase(get()) }
    single { LibraryLocalDataSource(get()) }
    single { CollectionLocalDataSource(get()) }

    // ── App backend API (mock engine until a real server exists) ────────────
    single { ApiConfig() }
    single(named(AUTH_HTTP_CLIENT)) { createAuthHttpClient(local = get(), config = get()) }
    single<AuthApi> { KtorAuthApi(client = get(named(AUTH_HTTP_CLIENT)), baseUrl = get<ApiConfig>().baseUrl) }

    // ── Person B — User & Commerce (local / session-backed) ─────────────────
    single<AuthRepository> { RemoteAuthRepository(get(), get()) }
    single<UserRepository> { UserRepositoryImpl(get()) }
    single<SocialRepository> { SocialRepositoryImpl(get()) }
    single<ChatRepository> { ChatRepositoryImpl(get()) }
    single<NotificationRepository> { NotificationRepositoryImpl(get()) }
    single<MembershipRepository> { MembershipRepositoryImpl(get()) }
    single<PaymentRepository> { PaymentRepositoryImpl(get()) }

    // ── Person A — Book content (remote Gutendex/OpenLibrary) ───────────────
    single<HttpClient> { createRemoteHttpClient() }
    single<GutendexApi> { KtorGutendexApi(get()) }
    single<OpenLibraryApi> { KtorOpenLibraryApi(get()) }
    single<BookRepository> { RemoteBookRepository(get(), get(), get()) }
    single<LibraryRepository> { LocalLibraryRepository(get()) }
    single<CollectionRepository> { LocalCollectionRepository(get()) }

    // ── Offline downloads (Phase 3) ──────────────────────────────────────────
    single<DownloadRepository> { DownloadRepositoryImpl(get(), get(), get()) }

    // Domain use cases
    factory { LoginUseCase(get()) }
    factory { SignUpUseCase(get()) }
    factory { LogoutUseCase(get()) }
    factory { GetCurrentUserUseCase(get()) }
    factory { GetProfileUseCase(get()) }
    factory { UpdateProfileUseCase(get()) }

    factory { SearchBooksUseCase(get()) }
    factory { GetHomeBooksUseCase(get()) }
    factory { GetBooksByCategoryUseCase(get()) }
    factory { GetBookDetailsUseCase(get()) }
    factory { GetCategoriesUseCase(get()) }
    factory { BookPaginator() }
    factory { GetBookContentUseCase(repository = get(), downloadRepository = get()) }
    factory { DownloadBookUseCase(get()) }
    factory { DeleteDownloadUseCase(get()) }

    factory { GetLibraryBooksUseCase(get()) }
    factory { GetContinueReadingUseCase(get()) }
    factory { UpdateReadingProgressUseCase(get()) }

    factory { GetCollectionsUseCase(get()) }
    factory { GetCollectionDetailsUseCase(get()) }
    factory { CreateCollectionUseCase(get()) }
    factory { UpdateCollectionUseCase(get()) }
    factory { DeleteCollectionUseCase(get()) }

    factory { GetFriendsUseCase(get()) }
    factory { GetFriendDetailsUseCase(get()) }
    factory { InviteFriendUseCase(get()) }

    factory { GetChatsUseCase(get()) }
    factory { GetMessagesUseCase(get()) }
    factory { SendMessageUseCase(get()) }

    factory { GetNotificationsUseCase(get()) }
    factory { MarkNotificationAsReadUseCase(get()) }

    factory { GetMembershipPlansUseCase(get()) }
    factory { GetCurrentMembershipUseCase(get()) }

    factory { GetPaymentMethodsUseCase(get()) }
    factory { PurchaseBookUseCase(get()) }
    factory { GetPurchaseDetailsUseCase(get()) }

    // Presentation MVI stores
    factory { LoginViewModel(get()) }
    factory { SignUpViewModel(get()) }
    factory { ForgotPasswordViewModel() }
    factory { VerificationViewModel() }
    factory { HomeViewModel(get(), get()) }
    factory { LibraryViewModel(get(), get(), get()) }
    factory { SearchViewModel(get(), get()) }
    factory { StoreViewModel(get(), get()) }
    factory { (authorId: String) -> AuthorDetailViewModel(authorId) }
    factory { (bookId: String) -> PrePurchaseViewModel(bookId, get(), get(), get()) }
    factory { (bookId: String) -> BookReviewViewModel(bookId, get()) }
    factory { (categoryId: String) -> CategoryDetailViewModel(categoryId, get(), get()) }

    factory { CollectionsViewModel(get()) }
    factory { (collectionId: String) -> CollectionDetailsViewModel(collectionId, get()) }
    factory { (collectionId: String) -> CollectionsEditViewModel(collectionId, get(), get(), get(), get()) }

    factory { GoalViewModel(get()) }

    factory { MembershipViewModel(get()) }
    factory { PremiumMembershipViewModel(get(), get()) }

    factory { NotificationsViewModel(get(), get()) }

    factory { OnboardingOneViewModel() }
    factory { OnboardingTwoViewModel() }
    factory { OnboardingThreeViewModel() }

    factory { (bookId: String) -> PurchaseDetailsViewModel(bookId, get()) }
    factory { (bookId: String) -> PurchaseReceiptViewModel(bookId, get()) }
    factory { PaymentMethodsViewModel(get()) }
    factory { PurchaseConfirmationViewModel() }
    factory { PaymentSuccessViewModel() }
    factory { PaymentFailedViewModel() }

    factory { ProfileViewModel(get()) }
    factory { (bookId: String) -> ReadingViewModel(bookId, get(), get(), get(), get(), get()) }
    factory { SettingsViewModel() }

    factory { FriendListViewModel(get()) }
    factory { (friendId: String) -> FriendDetailViewModel(friendId, get()) }
    factory { (friendId: String) -> ChatViewModel(friendId, get(), get()) }
    factory { InviteFriendsViewModel(get()) }
    factory { NoFriendsViewModel() }
}
