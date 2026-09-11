package com.example.dz.presentation.navigation

import com.example.dz.presentation.auth.verification.VerificationPurpose
import io.ktor.http.encodeURLParameter
import io.ktor.http.encodeURLPathPart

object Routes {
    // Auth
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    /**
     * Both arguments are optional, so that arriving with nothing to say still matches: signing
     * out, or a splash that found no session, navigates with [login] and no arguments at all.
     */
    const val LOGIN = "login?email={email}&reset={reset}"
    const val SIGN_UP = "sign_up"

    /** Carries whatever address the sign-in screen already had, so it is not typed twice. */
    const val FORGOT_PASSWORD = "forgot_password?email={email}"

    /**
     * Code entry is reached from sign-up and from a password reset, and ends somewhere different
     * in each — so the purpose travels in the route rather than being guessed at the far end.
     * `created` marks an account the sign-up form has just made; see [verification].
     */
    const val VERIFICATION = "verification/{purpose}/{email}?created={created}"
    /**
     * The reset code rides along: it is spent by the call this screen makes, not by the code
     * screen that collected it.
     */
    const val NEW_PASSWORD = "new_password/{email}/{code}"

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
    /**
     * [email] prefills the address box. [passwordJustReset] says the reader has arrived straight
     * from finishing a reset, which is the one case where landing on sign-in needs explaining.
     *
     * Query rather than path, because both are genuinely optional here — an address is only
     * known when a screen had one to pass on.
     */
    fun login(email: String = "", passwordJustReset: Boolean = false) =
        "login?email=${email.encodeURLParameter()}&reset=$passwordJustReset"

    fun forgotPassword(email: String = "") =
        "forgot_password?email=${email.encodeURLParameter()}"
    /**
     * [email] is percent-encoded: an address is user input, and Navigation splits the route on
     * the same characters an address is allowed to contain.
     *
     * [accountJustCreated] is for the sign-up form alone. It makes backing out of the code screen
     * delete the account, which is only safe for one that did not exist until the form made it.
     */
    fun verification(
        purpose: VerificationPurpose,
        email: String,
        accountJustCreated: Boolean = false,
    ) = "verification/${purpose.name}/${email.encodeURLPathPart()}?created=$accountJustCreated"

    fun newPassword(email: String, code: String) =
        "new_password/${email.encodeURLPathPart()}/${code.encodeURLPathPart()}"

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
