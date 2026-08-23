package com.example.dz.presentation.navigation

object Routes {
    // Auth
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val LOGIN = "login"
    const val SIGN_UP = "sign_up"
    const val FORGOT_PASSWORD = "forgot_password"
    const val VERIFICATION = "verification"

    // Bottom Nav Tabs
    const val HOME = "home"
    const val LIBRARY = "library"
    const val STORE = "store"
    const val SEARCH = "search"
    const val PROFILE_TAB = "profile_tab"

    // Book flows
    const val PRE_PURCHASE = "pre_purchase/{bookId}"
    const val READING = "reading/{bookId}"
    const val BOOK_REVIEW = "book_review/{bookId}"

    // Purchase flow
    const val PURCHASE_DETAILS = "purchase_details/{bookId}"
    const val PURCHASE_RECEIPT = "purchase_receipt/{bookId}"
    const val PURCHASE_CONFIRMATION = "purchase_confirmation"
    const val PAYMENT_METHODS = "payment_methods"
    const val PAYMENT_SUCCESS = "payment_success"
    const val PAYMENT_FAILED = "payment_failed"

    // Discovery
    const val CATEGORY_DETAIL = "category_detail/{categoryName}"
    const val AUTHOR_DETAIL = "author_detail/{authorId}"

    // Library management
    const val COLLECTIONS = "collections"
    const val COLLECTION_DETAIL = "collection_detail/{collectionId}"
    const val COLLECTIONS_EDIT = "collections_edit/{collectionId}"

    // Profile area
    const val PROFILE_EDIT = "profile_edit"
    const val SETTINGS = "settings"
    const val NOTIFICATIONS = "notifications"
    const val GOAL = "goal"
    const val MEMBERSHIP = "membership"
    const val PREMIUM_MEMBERSHIP = "premium_membership"

    // Social
    const val FRIEND_LIST = "friend_list"
    const val NO_FRIENDS = "no_friends"
    const val FRIEND_DETAIL = "friend_detail/{friendId}"
    const val CHAT = "chat/{friendId}"
    const val INVITE_FRIENDS = "invite_friends"

    // Helpers to build routes with arguments
    fun prePurchase(bookId: String) = "pre_purchase/$bookId"
    fun reading(bookId: String) = "reading/$bookId"
    fun bookReview(bookId: String) = "book_review/$bookId"
    fun purchaseDetails(bookId: String) = "purchase_details/$bookId"
    fun purchaseReceipt(bookId: String) = "purchase_receipt/$bookId"
    fun categoryDetail(categoryName: String) = "category_detail/$categoryName"
    fun authorDetail(authorId: String) = "author_detail/$authorId"
    fun collectionDetail(collectionId: String) = "collection_detail/$collectionId"
    fun collectionsEdit(collectionId: String) = "collections_edit/$collectionId"
    fun friendDetail(friendId: String) = "friend_detail/$friendId"
    fun chat(friendId: String) = "chat/$friendId"
}
