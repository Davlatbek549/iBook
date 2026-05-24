# iBook

iBook is an Android reading app prototype built with Kotlin and Jetpack Compose. The project is shaping into a social ebook experience where users can discover books, read samples, track reading progress, review titles, save books to collections, and connect with friends around what they are reading.

At the current stage, the project is primarily a polished Compose UI prototype. Many screens and reusable components are already designed and implemented with mocked content, while production navigation, authentication, persistence, API integration, and real book data still need to be connected.

## What We Are Building

iBook is intended to become a mobile reading platform with three main product ideas:

- A book discovery and purchase flow for browsing book details, viewing samples, and buying books.
- A reading experience that shows reading progress, reading streaks, categories, and quick access to comments.
- A social layer where readers can invite friends, chat, share books, and see friend activity.

The app already has the visual foundation for these flows. The next big stage is turning the UI prototype into a connected app with real state, routing, and data.

## Current Stage

### Implemented So Far

- Jetpack Compose Android app scaffold.
- Material 3 theme with custom brand, category, light, and dark colors.
- Main activity wired to a Compose `MainScreen`.
- Bottom navigation with Home, Books, Store, and Search tabs.
- Placeholder tab screens for the main shell.
- Reusable UI components:
  - Primary app button.
  - Circular icon button.
  - Universal input field with email, phone, number, text, and password modes.
  - Download progress popup.
  - Download success screens.
- Authentication UI:
  - Splash screen.
  - Login screen.
  - Sign-up screen.
  - Forgot password screen.
  - Verification / OTP screen.
- Book experience UI:
  - Book review screen for an owned/readable book.
  - Pre-purchase book detail screen.
  - Reading progress screen.
  - Review/comments/write-review states inside the book review flow.
  - Save-to-collection and share overlays.
- Social UI:
  - Friend list and friend search.
  - Empty friends state.
  - Invite friends screen.
  - Chat screen.
- Local drawable assets for icons, book cover, clock, and profile avatars.
- Basic unit and instrumentation test placeholders from the Android template.

### Not Yet Implemented

- Real app-level navigation between all screens.
- Authentication logic and backend integration.
- User accounts, sessions, and secure storage.
- Real book catalog data.
- Real purchase/download logic.
- Persistent reading progress.
- Real comments, ratings, reviews, and collections.
- Friend graph, chat backend, invite links, or social provider integration.
- ViewModels, repositories, domain models, and dependency injection.
- Meaningful automated tests for current UI behavior.

## Tech Stack

- Kotlin
- Android Gradle Plugin
- Jetpack Compose
- Material 3
- Navigation Compose
- Coil / Coil Compose
- JUnit
- AndroidX Test
- Espresso

## Project Structure

```text
iBook/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/ibook/
│   │   │   ├── app_components/      # Shared buttons, inputs, popups, result UI
│   │   │   ├── navigation/          # Bottom navigation models and custom bottom bar
│   │   │   ├── screens/             # Compose screens grouped by feature
│   │   │   ├── ui/theme/            # Colors, typography, Material theme helpers
│   │   │   └── MainActivity.kt      # App entry point
│   │   └── res/
│   │       ├── drawable/            # Icons, book/profile images, visual assets
│   │       └── values/              # Strings, colors, themes
│   ├── src/test/                    # Local unit tests
│   └── src/androidTest/             # Instrumented Android tests
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

## Important Screens

| Area | Files | Status |
| --- | --- | --- |
| App shell | `MainActivity.kt`, `MainScreen.kt`, `navigation/` | Basic bottom-tab shell is active |
| Temporary tabs | `screens/temp/MockScreens.kt` | Placeholder Home, Books, Store, Search screens |
| Authentication | `screens/login/`, `screens/sign_up/`, `screens/forgot_password/`, `screens/verification/`, `screens/splash/` | UI implemented, logic pending |
| Book detail | `screens/book_review/BookReview.kt` | Rich interactive UI with review, comment, share, and collection states |
| Purchase flow | `screens/pre_purchase/PrePurchaseBookReview.kt` | UI implemented with purchase/sample/tag actions |
| Reading | `screens/reading/Reading.kt` | UI implemented with reading stats and keep-reading action |
| Friends | `screens/first_friend_list/`, `screens/no_friends/`, `screens/invite_friend_list_2/` | Social UI implemented with mocked data |
| Chat | `screens/chat/ChatScreen.kt` | Chat UI implemented with mocked messages |
| Downloading | `app_components/downloading/`, `app_components/results/` | Download popup and success states implemented |

## Getting Started

### Requirements

- Android Studio
- JDK 11 or newer
- Android SDK with compile SDK 36 support

### Run the App

Open the project in Android Studio and run the `app` configuration on an emulator or physical device.

You can also build from the terminal:

```bash
./gradlew assembleDebug
```

### Run Tests

```bash
./gradlew test
```

For instrumented tests:

```bash
./gradlew connectedAndroidTest
```

## Development Notes

- The current entry point is `MainActivity`, which renders `MainScreen`.
- `MainScreen` currently exposes only the bottom navigation tabs and placeholder screens.
- Many feature screens exist as standalone composables and previews, but are not yet registered in a full app navigation graph.
- Most screen data is hardcoded or loaded from `strings.xml`.
- The codebase currently focuses on UI composition and responsive layout metrics rather than app architecture.
- Before adding backend behavior, it would be helpful to introduce feature-level ViewModels and state models for books, auth, reading progress, friends, and chat.

## Recommended Next Milestones

1. Create a full navigation graph for auth, onboarding, main tabs, book detail, reading, purchase, friends, and chat.
2. Replace placeholder tab screens with real Home, Library/Books, Store, and Search screens.
3. Add ViewModels and UI state classes for each major feature.
4. Move mocked screen data into sample repositories or fake data providers.
5. Add real authentication flow.
6. Add persistent local storage for user settings, saved books, collections, and reading progress.
7. Connect book catalog, comments, ratings, friends, and chat to backend services.
8. Add UI tests for important user flows.

## Product Vision

iBook is becoming more than a simple ebook reader. The strongest direction for the product is a reader-centered social library: a place where people can discover books, see what friends are reading, talk about stories, and keep momentum through progress tracking and beautiful reading rituals.

The current codebase already has the visual personality for that product. The next stage is to connect the screens into a real app experience.
