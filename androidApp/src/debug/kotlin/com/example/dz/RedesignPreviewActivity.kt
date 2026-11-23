package com.example.dz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import com.example.dz.designsystem.theme.DZTheme
import com.example.dz.presentation.book.author_detail.*
import com.example.dz.presentation.payment.payment_methods.*
import com.example.dz.presentation.payment.purchase_details.*
import com.example.dz.presentation.payment.purchase_confirmation.*
import com.example.dz.presentation.payment.payment_success.*
import com.example.dz.presentation.payment.payment_failed.*
import com.example.dz.presentation.payment.purchase_receipt.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.EditableProfile
import com.example.dz.domain.model.UsernameAvailability
import com.example.dz.domain.repository.ProfileRepository
import com.example.dz.domain.usecase.user.CheckUsernameUseCase
import com.example.dz.domain.usecase.user.GetEditableProfileUseCase
import com.example.dz.domain.usecase.user.SaveEditableProfileUseCase
import com.example.dz.domain.usecase.user.SaveProfileImageUseCase
import com.example.dz.domain.model.ProfileImage
import com.example.dz.presentation.profile.profile_edit.EditProfileScreen
import com.example.dz.presentation.profile.profile_edit.EditProfileViewModel

/** Debug-only visual fixtures. Launch with --es screen author|methods|details|confirm|success|failed|receipt|edit-profile. */
class RedesignPreviewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val screen = intent.getStringExtra("screen") ?: "author"
        val scale = intent.getFloatExtra("fontScale", 1f)
        setContent {
            CompositionLocalProvider(LocalDensity provides Density(LocalDensity.current.density, scale)) {
                DZTheme {
                    when (screen) {
                        "author" -> AuthorsDetailsScreen(AuthorDetailViewModel("preview").uiState.value)
                        "author-empty" -> AuthorsDetailsScreen(AuthorDetailUiState(name = "Jacqueline Woodson", tagline = "Novelist · National Book Award winner",
                            booksCount = "34", readersCount = "212k", averageRating = "4.5", about = "Jacqueline Woodson writes spare, luminous novels about family, memory and belonging. Her work moves between generations with a poet’s ear for what’s left unsaid."))
                        "methods" -> PaymentMethodsScreen(PaymentMethodsUiState(methods = listOf(
                            PaymentMethodItem("visa", PaymentBrand.Visa, "•••• 4417", "Amelia Hart · exp 04/29"),
                            PaymentMethodItem("mc", PaymentBrand.Mastercard, "•••• 8802", "Amelia Hart · exp 11/27"),
                            PaymentMethodItem("pp", PaymentBrand.Paypal, "PayPal", "amelia@hart.co")), selectedId = "visa"))
                        "empty" -> PaymentMethodsScreen()
                        "loading" -> PaymentMethodsScreen(PaymentMethodsUiState(isLoading = true))
                        "error" -> PaymentMethodsScreen(PaymentMethodsUiState(errorMessage = "Unable to load payment methods. Please try again later."))
                        "details" -> PurchaseDetailsScreen(PurchaseDetailsUiState(bookId = "preview", title = "Bestiary", author = "K-Ming Chang", bookPrice = "$6.40", coinsDiscount = "−$1.40", tax = "$1.00", total = "$6.00"))
                        "confirm" -> PurchaseConfirmationScreen(PurchaseConfirmationUiState(bookId = "preview", title = "Bestiary", author = "K-Ming Chang", bookPrice = "$6.40", discount = "−$1.40", tax = "$1.00", total = "$6.00"))
                        "success" -> PurchaseSuccessScreen(PaymentSuccessUiState(bookId = "preview", title = "Bestiary", author = "K-Ming Chang", total = "$6.00"))
                        "failed" -> PurchaseFailedScreen()
                        "receipt" -> PurchaseReceiptScreen(PurchaseReceiptUiState(bookId = "preview", title = "Bestiary", author = "K-Ming Chang", orderId = "DZ-40912-BST", dateLabel = "15 Aug 2026 · 9:38", paymentLabel = "VISA •••• 4417", paymentEmail = "amelia@hart.co", bookPrice = "$6.40", coinsDiscount = "−$1.40", tax = "$1.00", totalCharged = "$6.00", isPaid = true))
                        "edit-profile" -> {
                            val vm = remember {
                                val profiles = InMemoryProfiles()
                                EditProfileViewModel(GetEditableProfileUseCase(profiles), SaveEditableProfileUseCase(profiles), CheckUsernameUseCase(profiles), SaveProfileImageUseCase(profiles))
                            }
                            val state by vm.uiState.collectAsState()
                            EditProfileScreen(uiState = state, onEvent = vm::onEvent)
                        }
                    }
                }
            }
        }
    }
}

/** Edit Profile against a profile held in memory, so the form works without an account or server. */
private class InMemoryProfiles : ProfileRepository {
    private var profile = EditableProfile(name = "Amelia Hart", username = "amelia.reads", email = "amelia@hart.co", emailVerified = true, bio = "Slow mornings, long novels")
    override suspend fun getEditableProfile() = AppResult.Success(profile)
    override suspend fun saveEditableProfile(profile: EditableProfile) = AppResult.Success(profile.also { this.profile = it })
    override suspend fun checkUsername(username: String) = AppResult.Success(UsernameAvailability(username, available = true))

    /** Stored as a data URI so Coil can show it back, as it would a server URL. */
    @OptIn(kotlin.io.encoding.ExperimentalEncodingApi::class)
    override suspend fun uploadImage(image: ProfileImage, jpeg: ByteArray): AppResult<EditableProfile> {
        val url = "data:image/jpeg;base64," + kotlin.io.encoding.Base64.encode(jpeg)
        profile = if (image == ProfileImage.Photo) profile.copy(avatarUrl = url) else profile.copy(bannerUrl = url)
        return AppResult.Success(profile)
    }

    override suspend fun removeImage(image: ProfileImage): AppResult<EditableProfile> {
        profile = if (image == ProfileImage.Photo) profile.copy(avatarUrl = null) else profile.copy(bannerUrl = null)
        return AppResult.Success(profile)
    }
}
