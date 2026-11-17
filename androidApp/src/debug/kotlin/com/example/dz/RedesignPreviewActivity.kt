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

/** Debug-only visual fixtures. Launch with --es screen author|methods|details|confirm|success|failed|receipt. */
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
                    }
                }
            }
        }
    }
}
