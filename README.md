# DZ

DZ is a Kotlin Multiplatform ebook app built with Compose Multiplatform. Screens, business logic,
networking and the local database all live in the `shared` module, so Android and iOS run the same
code; each platform contributes only a shell and a handful of `expect`/`actual` implementations.

The entry point sets up the image loader, then hands off to the theme and the navigation graph:

```kotlin
@Composable
fun App() {
    setSingletonImageLoaderFactory { /* Coil + Ktor fetcher */ }

    DZTheme {
        DZNavGraph()
    }
}
```

## Project Status

The Android-to-Kotlin-Multiplatform migration is done. What is shared is no longer just the UI:
the domain layer, repositories, Ktor client, SQLDelight database and DI graph are all in
`commonMain`, and `androidMain`/`iosMain` hold only what genuinely needs a platform API — database
drivers, file storage, Google sign-in, status-bar appearance, reader typesetting.

What is built:

- Five tabs behind a floating glass bar: Home, Library, Store, Friends, Profile.
- A paginated reader with text size, page colour, font and page-turn style (slide, paper, scroll),
  bookmarks, shared notes and offline downloads.
- Full auth against our own server: sign-in, sign-up, email verification, password reset, Google
  sign-in on both platforms, account deletion.
- Store and a payment flow, collections, reading goals, notifications, membership tiers, and a
  social area with friends, chat and invites.

The design system is mid-migration from the older **Ink** look to **Organic** (see [Theme](#theme)).

Tests cover reader pagination, book-content mapping, list keys and the server's error contract.
Coverage is real but thin — it grows where behaviour has proven easy to get wrong, not uniformly.

## Tech Stack

- Kotlin Multiplatform + Compose Multiplatform, Material 3
- **Koin** for dependency injection
- **Ktor** for HTTP, with `kotlinx.serialization`
- **SQLDelight** for the local database
- **Navigation Compose** for routing
- **Coil** for image loading
- **Haze** for the glass material used by the Organic design system
- **Multiplatform Settings** for key-value storage
- JetBrains Compose Resources for shared drawables, fonts and strings
- Baseline profiles for Android startup
- SwiftUI wrapper for iOS hosting

Versions are managed in `gradle/libs.versions.toml`:

- Kotlin: `2.4.20`
- Compose Multiplatform: `1.12.0`
- Android Gradle Plugin: `9.2.1`
- Android compile SDK: `37.2`
- Android min SDK: `24`
- Android target SDK: `36`

## Project Structure

```text
.
├── androidApp/
│   └── src/main/
│       ├── kotlin/com/example/dz/MainActivity.kt
│       └── res/
├── iosApp/
│   ├── iosApp.xcodeproj/
│   └── iosApp/
│       ├── ContentView.swift
│       └── iOSApp.swift
├── baselineprofile/
├── shared/
│   └── src/
│       ├── commonMain/
│       │   ├── composeResources/
│       │   │   ├── drawable/
│       │   │   ├── font/
│       │   │   └── values/strings.xml
│       │   ├── sqldelight/
│       │   └── kotlin/com/example/dz/
│       │       ├── App.kt
│       │       ├── core/          di, auth, network, platform, legal, error
│       │       ├── data/          repository, remote (api + dto), local (db + file), mapper
│       │       ├── domain/        model, repository interfaces, usecase
│       │       ├── designsystem/  theme + components (organic, ink, inputs, ...)
│       │       └── presentation/  one package per screen area, plus navigation
│       ├── androidMain/
│       ├── iosMain/
│       ├── commonTest/
│       ├── androidHostTest/
│       └── iosTest/
├── gradle/libs.versions.toml
├── settings.gradle.kts
└── build.gradle.kts
```

## Modules 

### `androidApp`

The Android application module. It contains the Android launcher activity and Android app configuration.

Important files:

- `androidApp/src/main/kotlin/com/example/dz/MainActivity.kt`
- `androidApp/src/main/AndroidManifest.xml`
- `androidApp/build.gradle.kts`

`MainActivity` calls the shared `App()` composable:

```kotlin
setContent {
    App()
}
```

### `iosApp`

The iOS application shell. It uses SwiftUI to host the shared Compose UI.

Important files:

- `iosApp/iosApp/ContentView.swift`
- `iosApp/iosApp/iOSApp.swift`
- `iosApp/iosApp.xcodeproj`

`ContentView.swift` wraps the shared Kotlin `MainViewController()`:

```swift
struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Self.Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }
}
```

### `shared`

The shared Kotlin Multiplatform module, and where nearly all of the app lives.

Important areas:

- `shared/src/commonMain/kotlin/com/example/dz/App.kt`
- `shared/src/commonMain/kotlin/com/example/dz/presentation` — screens, one package per area
- `shared/src/commonMain/kotlin/com/example/dz/presentation/navigation` — routes and `DZNavGraph`
- `shared/src/commonMain/kotlin/com/example/dz/domain` — models and use cases
- `shared/src/commonMain/kotlin/com/example/dz/data` — repositories, Ktor APIs, SQLDelight
- `shared/src/commonMain/kotlin/com/example/dz/designsystem` — theme and components
- `shared/src/commonMain/composeResources`
- `shared/src/commonMain/sqldelight`
- `shared/src/iosMain/kotlin/com/example/dz/MainViewController.kt`

The shared module builds:

- An Android library consumed by `androidApp`.
- A static iOS framework named `Shared`.

### `baselineprofile`

Generates the Android baseline profile that `:androidApp` ships, so the hot paths are compiled
ahead of first run instead of being interpreted. `BaselineProfileGenerator` drives the app through
its startup path with UI Automator.

## Shared Screens

Screen packages live in:

```text
shared/src/commonMain/kotlin/com/example/dz/presentation
```

Current areas: `splash`, `onboarding`, `auth` (login, sign_up, verification, forgot_password,
new_password), `home`, `library`, `store`, `search`, `book` (pre_purchase, review, author_detail,
category_detail), `reading`, `collections` (list, details, edit), `goal`, `social` (friends,
friend_detail, chat, invite_friends, no_friends), `payment`, `membership`,
`premium_membership`, `profile`, `notifications`, `settings`, plus `common`, `mvi` and
`navigation`.

Screens follow MVI: a `ViewModel` exposes a single state to the screen and takes events back from
it, and one-shot navigation is emitted as effects that `DZNavGraph` collects.

When adding or fixing a screen, prefer keeping it in `commonMain` unless it truly needs
platform-specific APIs.

## Resources

Shared UI should use Compose Multiplatform resources from:

```text
shared/src/commonMain/composeResources
```

Use generated resources like this:

```kotlin
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

Icon(
    painter = painterResource(Res.drawable.some_icon),
    contentDescription = stringResource(Res.string.some_description)
)
```

Important rule: resources inside `androidApp/src/main/res` are Android-only. If a shared screen needs an image, icon, or string on both Android and iOS, place it under `shared/src/commonMain/composeResources`.

## Theme

Shared theme code lives in:

```text
shared/src/commonMain/kotlin/com/example/dz/designsystem/theme
```

Main theme entry point:

```kotlin
DZTheme {  }
```

There are two design systems in the tree, and this is deliberate rather than leftover:

- **Organic** (`Organic.kt`, `designsystem/components/organic`) is the current look — a warm cream
  and terracotta palette with real refracting glass panels, built on Haze. New and redesigned
  screens use it.
- **Ink** (`Ink.kt`, `designsystem/components/ink`) is the older system. Screens still on it are
  being migrated across, so prefer Organic for anything new.

Shared colours live in `Color.kt`, typography in `Type.kt`, and the per-system tokens in
`Organic.kt` and `Ink.kt`.

## Navigation

Routing lives in:

```text
shared/src/commonMain/kotlin/com/example/dz/presentation/navigation
```

- `Routes.kt` — every route string in the app.
- `NavGraph.kt` — `DZNavGraph`, which owns the `NavHost`, wires each screen to its ViewModel and
  collects navigation effects.
- `BottomNavHost.kt` — the five bottom-bar destinations.

Bottom navigation routes:

- `home`
- `library`
- `store`
- `friend_list`
- `profile_tab`

Search is reachable from Home and Library rather than being a tab of its own. The bar hides itself
on the auth flow, the reader and pushed detail screens; `bottomBarHiddenRoutes` in `NavGraph.kt` is
the list. Switching tabs pops back to Home rather than stacking, so each tab keeps its own scroll
position and back stack through save/restore.

## Backend & Auth

The app talks to **our own server**, `dz-server` — a Kotlin/Spring Boot service backed by
PostgreSQL, kept in a separate repository alongside this one. It owns accounts, profiles, each
user's library and their collections, so there is no third-party backend in between.

`KtorAuthApi` calls it through the shared Ktor client using `ApiConfig.baseUrl`, which already
includes the `/api/v1` prefix.

### Sessions

Login and sign-up return a short-lived access token (15 minutes) alongside a refresh token that
lasts 30 days, and both are stored through `LocalDataSource`.

Renewal is **reactive**: the client retries a 401 once against `/auth/refresh` and replays the
original request, so callers never see the expired token — see `createAuthHttpClient`. The server
rotates the refresh token on every use, so concurrent 401s are funnelled through a single refresh
rather than each spending the stored one. A refusal from `/auth/refresh` clears the session; a
timeout or 5xx leaves it alone, because neither says the token is invalid.

The splash screen restores a stored session through `GetCurrentUserUseCase` and opens straight on
Home, so signing in persists across launches. Settings → Sign out revokes that session server-side
and clears the back stack.

### Password reset

`Forgot password` → code → `New password`, against `/auth/password/forgot` and
`/auth/password/reset`. Both are public: a reset is the flow for someone who cannot sign in, so it
can never require having signed in.

The code is spent by `/auth/password/reset`, not by the code screen before it. The server allows a
fixed number of guesses against a code and the reset itself needs one of them, so a separate "is
this code right?" step would cost a guess for nothing — a wrong or expired code therefore surfaces
on the New password screen.

A completed reset ends at **sign-in, not Home**. The server issues no session for one and revokes
every existing refresh token, so `RemoteAuthRepository` clears the local session too. Asking for a
code answers the same way whether or not the address is registered, so nothing in this flow can be
used to discover who has an account.

### The deployed server (default)

`ApiConfig.baseUrl` points at <https://dz-server.onrender.com/api/v1>, so **the app runs with
nothing started locally** — no Docker, no Gradle, no database. That is what a fresh clone gets.

It sleeps after inactivity, so the first request after a pause can take up to a minute while the
server wakes. Screens that call the network on load should show a loading state rather than appear
frozen.

### Running against a local server instead

Set `baseUrl = ApiConfig.localBaseUrl`, then from the `dz-server` repository:

```bash
docker compose up -d && ./gradlew bootRun
```

`localBaseUrl` builds on `devServerHost`, which is per-platform because the simulators reach the
host machine differently: `10.0.2.2` on the Android emulator, `127.0.0.1` on the iOS simulator. A
**physical device** needs your machine's LAN address instead.

Both platforms block plain HTTP by default, so each carries a narrow exception for loopback only —
`androidApp/src/main/res/xml/network_security_config.xml` and `NSAppTransportSecurity` in
`iosApp/iosApp/Info.plist`. Those matter only for local work; the deployed server is HTTPS. Every
other host still requires HTTPS, so both exceptions are safe to ship.

### Errors

The server returns `{"code": "...", "message": "..."}`, where auth codes match `AppError.AuthReason`
exactly, so the UI can tell a duplicate email from a wrong password. Unrecognised codes fall back to
status-code mapping. `DzServerAuthApiTest` pins this contract against real captured responses.

## Prerequisites

Recommended tools:

- Android Studio with Kotlin Multiplatform and Compose support.
- Xcode for building and running the iOS app.
- JDK compatible with the Android Gradle Plugin used by the project. The Android Studio bundled JDK is recommended.
- Android SDK installed locally.

`local.properties` is intentionally ignored by Git and should contain your local Android SDK path. Android Studio usually creates it automatically.

Example:

```properties
sdk.dir=/Users/your-name/Library/Android/sdk
```

## Setup

Clone the project and open it in Android Studio:

```bash
git clone <repository-url>
cd DZ
```

Then let Android Studio sync Gradle.

If you are setting up manually, make sure `local.properties` exists with the correct Android SDK path.

## Running Android

From Android Studio:

1. Open the project root.
2. Wait for Gradle sync.
3. Select the `androidApp` run configuration.
4. Run on an emulator or physical Android device.

From the command line:

```bash
./gradlew :androidApp:assembleDebug
```

The generated APK will be under:

```text
androidApp/build/outputs/apk/debug/
```

## Running iOS

Open the Xcode project:

```text
iosApp/iosApp.xcodeproj
```

Then:

1. Select an iOS simulator.
2. Configure signing if needed.
3. Build and run from Xcode.

The iOS app hosts the shared Compose UI through `MainViewController()` from the `shared` module.

## Useful Gradle Commands

Compile shared Android and iOS targets:

```bash
./gradlew :shared:compileAndroidMain :shared:compileKotlinIosSimulatorArm64
```

Build Android debug APK:

```bash
./gradlew :androidApp:assembleDebug
```

Run common tests:

```bash
./gradlew :shared:allTests
```

Run Android host tests:

```bash
./gradlew :shared:testAndroidHostTest
```

Run iOS simulator tests:

```bash
./gradlew :shared:iosSimulatorArm64Test
```

## Development Notes

- Keep cross-platform UI in `shared/src/commonMain`.
- Avoid Android-only APIs in shared screens.
- Use `painterResource` and `stringResource` from Compose Resources for shared assets and text.
- Put shared drawables in `shared/src/commonMain/composeResources/drawable`.
- Put shared strings in `shared/src/commonMain/composeResources/values/strings.xml`.
- Keep Android-only code in `androidMain` or `androidApp`.
- Keep iOS-only code in `iosMain` or `iosApp`.
- Prefer the existing theme colors, typography, and screen patterns before introducing new design helpers.
- Reach for the Organic components before writing a new one, and before falling back to Ink.
- New screens follow the MVI pattern already in `presentation`: state in, events out, navigation as
  effects.

## Current Verification

The shared screens have been verified with:

```bash
./gradlew :shared:compileAndroidMain :shared:compileKotlinIosSimulatorArm64
```

Run this command after shared UI changes to catch Android and iOS compile issues early.

## Git Notes

The `.gitignore` excludes local build outputs, IDE state, Kotlin/Gradle caches, Xcode user data, and `local.properties`.

Do not commit:

- `local.properties`
- `.gradle/`
- `.idea/`
- `build/`
- generated Android or Xcode build output

## License

No license file is currently included. Add one before publishing the project publicly if you want clear reuse terms.
