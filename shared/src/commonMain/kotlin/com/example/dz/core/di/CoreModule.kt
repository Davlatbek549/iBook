package com.example.dz.core.di

import com.example.dz.data.local.LocalDataSource
import com.example.dz.data.local.LocalDataSourceImpl
import com.example.dz.data.repository.AuthRepositoryImpl
import com.example.dz.data.repository.ChatRepositoryImpl
import com.example.dz.data.repository.FakeBookRepository
import com.example.dz.data.repository.FakeCollectionRepository
import com.example.dz.data.repository.FakeLibraryRepository
import com.example.dz.data.repository.MembershipRepositoryImpl
import com.example.dz.data.repository.NotificationRepositoryImpl
import com.example.dz.data.repository.PaymentRepositoryImpl
import com.example.dz.data.repository.SocialRepositoryImpl
import com.example.dz.data.repository.UserRepositoryImpl
import com.example.dz.domain.repository.AuthRepository
import com.example.dz.domain.repository.BookRepository
import com.example.dz.domain.repository.ChatRepository
import com.example.dz.domain.repository.CollectionRepository
import com.example.dz.domain.repository.LibraryRepository
import com.example.dz.domain.repository.MembershipRepository
import com.example.dz.domain.repository.NotificationRepository
import com.example.dz.domain.repository.PaymentRepository
import com.example.dz.domain.repository.SocialRepository
import com.example.dz.domain.repository.UserRepository
import com.russhwolf.settings.Settings
import org.koin.dsl.module

val coreModule = module {

    // ── Local storage ────────────────────────────────────────────────────────
    single { Settings() }
    single<LocalDataSource> { LocalDataSourceImpl(get()) }

    // ── Person B — User & Commerce (local / session-backed) ─────────────────
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<UserRepository> { UserRepositoryImpl(get()) }
    single<SocialRepository> { SocialRepositoryImpl(get()) }
    single<ChatRepository> { ChatRepositoryImpl(get()) }
    single<NotificationRepository> { NotificationRepositoryImpl(get()) }
    single<MembershipRepository> { MembershipRepositoryImpl(get()) }
    single<PaymentRepository> { PaymentRepositoryImpl(get()) }

    // ── Person A — Book content (fakes until A's real impl lands) ────────────
    single<BookRepository> { FakeBookRepository() }
    single<LibraryRepository> { FakeLibraryRepository() }
    single<CollectionRepository> { FakeCollectionRepository() }

    // Person A: replace this comment with HttpClient + GutendexApi + OpenLibraryApi bindings
}
