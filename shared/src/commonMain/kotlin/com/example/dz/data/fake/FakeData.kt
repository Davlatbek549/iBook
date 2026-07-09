package com.example.dz.data.fake

import com.example.dz.domain.model.AppNotification
import com.example.dz.domain.model.Author
import com.example.dz.domain.model.Book
import com.example.dz.domain.model.Category
import com.example.dz.domain.model.Collection
import com.example.dz.domain.model.Friend
import com.example.dz.domain.model.LibraryBook
import com.example.dz.domain.model.MembershipPlan
import com.example.dz.domain.model.Message
import com.example.dz.domain.model.PaymentBrand
import com.example.dz.domain.model.PaymentMethod
import com.example.dz.domain.model.Purchase
import com.example.dz.domain.model.PurchaseStatus
import com.example.dz.domain.model.User
import com.example.dz.domain.model.UserProfile

object FakeData {
    /** Placeholder book body so fake/offline builds can still exercise the reading flow. */
    val sampleBookText: String = buildString {
        repeat(6) { chapter ->
            append("Chapter ${chapter + 1}\n\n")
            repeat(4) {
                append(
                    "This is placeholder reading content used while real book text is unavailable. " +
                        "It provides enough paragraphs for the paginator to produce multiple pages.\n\n"
                )
            }
        }
    }.trim()

    val authors = listOf(
        Author(id = "patricia", name = "Patricia Smith", booksCount = 8),
        Author(id = "neil", name = "Neil Alvin", booksCount = 5)
    )

    val categories = listOf(
        Category(id = "fiction", name = "Fiction"),
        Category(id = "self-help", name = "Self Help"),
        Category(id = "business", name = "Business")
    )

    val books = listOf(
        Book(
            id = "olive-again",
            title = "Olive Again",
            authors = listOf(authors[0]),
            categories = listOf(categories[0]),
            rating = 4.7,
            reviewCount = 120,
            pageCount = 320,
            price = "\$12.99"
        ),
        Book(
            id = "quiet-growth",
            title = "Quiet Growth",
            authors = listOf(authors[1]),
            categories = listOf(categories[1]),
            rating = 4.5,
            reviewCount = 84,
            pageCount = 240,
            isFree = true
        )
    )

    val libraryBooks = books.mapIndexed { index, book ->
        LibraryBook(
            book = book,
            progressPercent = if (index == 0) 62 else 18,
            isDownloaded = index == 0,
            isFavorite = index == 1
        )
    }

    val collections = listOf(
        Collection(id = "favorites", title = "Favorites", books = books),
        Collection(id = "weekend", title = "Weekend Reads", books = books.take(1))
    )

    val user = User(
        id = "current-user",
        name = "Davlatbek",
        email = "reader@example.com"
    )

    val profile = UserProfile(
        user = user,
        booksRead = 24,
        friendsCount = 12,
        collectionsCount = collections.size,
        currentGoalMinutes = 30
    )

    val friends = listOf(
        Friend(id = "maria", name = "Maria Renzy", isOnline = true, currentBook = books.first()),
        Friend(id = "raunak", name = "Raunak Purohit", currentBook = books.last())
    )

    val messages = listOf(
        Message(id = "message-1", senderId = "maria", text = "Have you read this chapter?", sentAt = "Today"),
        Message(id = "message-2", senderId = user.id, text = "Starting it tonight.", sentAt = "Today", isMine = true)
    )

    val notifications = listOf(
        AppNotification(
            id = "goal",
            title = "Reading goal",
            message = "You are close to today's goal.",
            date = "Today"
        )
    )

    val paymentMethods = listOf(
        PaymentMethod(id = "paypal", brand = PaymentBrand.Paypal, title = "PayPal", isSelected = true),
        PaymentMethod(id = "visa", brand = PaymentBrand.Visa, title = "Visa", subtitle = "**** 4242")
    )

    val membershipPlans = listOf(
        MembershipPlan(
            id = "premium",
            title = "Premium",
            price = "\$9.99/month",
            benefits = listOf("Unlimited reading", "Offline downloads"),
            isCurrentPlan = true
        )
    )

    fun purchase(bookId: String, paymentMethodId: String? = null) = Purchase(
        id = "purchase-$bookId",
        book = books.firstOrNull { it.id == bookId } ?: books.first(),
        paymentMethod = paymentMethods.firstOrNull { it.id == paymentMethodId } ?: paymentMethods.first(),
        bookPrice = "\$12.99",
        discount = "\$0.00",
        tax = "\$1.00",
        total = "\$13.99",
        status = PurchaseStatus.Pending
    )
}
