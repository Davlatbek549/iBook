# DZ

DZ is a Kotlin Multiplatform book app built with Compose Multiplatform. The project shares the app UI and resources from the `shared` module so the same screens can run on Android and iOS.

The current app entry point starts from the shared Home screen:

```kotlin
@Composable
fun App() {
    DZTheme {
        Home()
    }
}
```

## Project Status

This project is in the middle of an Android-to-Kotlin-Multiplatform migration. Most screen UI has been moved into `shared/src/commonMain`, where it can compile for both Android and iOS.

Current focus areas:

- Shared Compose screens for Android and iOS.
- Shared drawable and string resources through Compose Multiplatform resources.
- Android app shell that hosts the shared UI.
- iOS SwiftUI shell that hosts the shared Compose `UIViewController`.

The codebase currently contains mostly UI and screen-level composition. Tests are still placeholder examples and should be expanded as real business logic, navigation state, and user workflows are added.

## Tech Stack

- Kotlin Multiplatform
- Compose Multiplatform
- Material 3
- Android Gradle Plugin
- AndroidX Activity Compose
- JetBrains Compose Resources
- SwiftUI wrapper for iOS hosting

Version highlights are managed in `gradle/libs.versions.toml`:

- Kotlin: `2.3.21`
- Compose Multiplatform: `1.11.0`
- Android Gradle Plugin: `9.2.1`
- Android compile SDK: `36`
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
├── shared/
│   └── src/
│       ├── commonMain/
│       │   ├── composeResources/
│       │   │   ├── drawable/
│       │   │   └── values/strings.xml
│       │   └── kotlin/com/example/dz/
│       │       ├── App.kt
│       │       ├── navigation/
│       │       ├── screens/
│       │       └── theme/
│       ├── androidMain/
│       ├── iosMain/
│       └── commonTest/
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

The shared Kotlin Multiplatform module. This is where the cross-platform Compose UI lives.

Important areas:

- `shared/src/commonMain/kotlin/com/example/dz/App.kt`
- `shared/src/commonMain/kotlin/com/example/dz/features`
- `shared/src/commonMain/kotlin/com/example/dz/navigation`
- `shared/src/commonMain/kotlin/com/example/dz/theme`
- `shared/src/commonMain/composeResources`
- `shared/src/iosMain/kotlin/com/example/dz/MainViewController.kt`

The shared module builds:

- An Android library consumed by `androidApp`.
- A static iOS framework named `Shared`.

## Shared Screens

Feature screen packages live in:

```text
shared/src/commonMain/kotlin/com/example/dz/features
```

Current feature groups include:

- `auth`
- `onboarding`
- `home`
- `library`
- `store`
- `search`
- `book`
- `payment`
- `profile`
- `collections`
- `social`
- `notification`

When adding or fixing a screen, prefer keeping it in `commonMain` unless it truly needs platform-specific APIs.

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
shared/src/commonMain/kotlin/com/example/dz/theme
```

Main theme entry point:

```kotlin
DZTheme { ... }
```

Brand and category colors are defined in `Color.kt`, typography helpers are in `Type.kt` and related theme files.

## Navigation

Basic bottom navigation helpers live in:

```text
shared/src/commonMain/kotlin/com/example/dz/navigation
```

Current bottom navigation routes:

- `home`
- `library`
- `store`
- `search`

The active app entry point currently launches the Home screen directly from `App.kt`. If full navigation is wired in later, keep the routing layer in shared code so Android and iOS stay aligned.

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
