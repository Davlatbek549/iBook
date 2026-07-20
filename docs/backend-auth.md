# Backend & Auth decision (Phase 4)

## Decision

We use **Firebase Authentication** as the auth backend, called through its
**Identity Toolkit REST API** from the shared Ktor client — no native Firebase SDK, no
`google-services.json`, and no server of our own to build or deploy.

Considered alternatives:

| Option | Why not |
| --- | --- |
| Small Ktor server (bcrypt + JWT + users table) | Real code to write, host, monitor, and keep free-tier alive; overkill while auth is the only server feature. |
| Firebase native SDKs (or GitLive KMP wrapper) | Adds platform-specific setup (`google-services.json`, iOS plist, CocoaPods) for features we don't need yet. |
| Firebase REST API (chosen) | Works identically on Android/iOS through the existing shared Ktor stack, keeps our `AuthApi` contract, and Firebase provides password hashing, token issuing, and the user store. |

The roadmap's "secure auth storage server-side" and "deploy the backend" tasks are satisfied by
Firebase itself: passwords are hashed with scrypt on Google's side, sessions are signed ID tokens
(JWTs), and there is nothing to deploy.

## How it is wired

- `FirebaseAuthApi` (shared module) implements the existing `AuthApi` interface:
  - sign-up → `POST /v1/accounts:signUp`, then `POST /v1/accounts:update` to set the display name;
  - login → `POST /v1/accounts:signInWithPassword`;
  - logout → local session clear only (Firebase ID tokens are stateless).
- `FirebaseConfig` holds the project's **Web API key** (not a secret — it only identifies the
  Firebase project).
- DI (`CoreModule`): while `ApiConfig.useMockBackend` is `true` the app keeps using the in-memory
  mock backend. Flipping it to `false` (Person 2's task) routes auth to Firebase.
- Firebase error codes (`EMAIL_EXISTS`, `INVALID_LOGIN_CREDENTIALS`, `WEAK_PASSWORD`, …) are mapped
  to `AppError.Auth(reason)` so the UI can show distinct wrong-password / duplicate-email /
  weak-password / rate-limited states.

## Token lifetime

The stored token is the Firebase **ID token**, valid for ~1 hour. After it expires, authenticated
calls fail with 401; the app's global 401 handling (Phase 4, Person 2) clears the session and
routes back to login. Silent refresh using the `refreshToken` (via
`securetoken.googleapis.com/v1/token`) is a known future improvement, noted for Phase 5.

## One-time Firebase setup

1. Go to <https://console.firebase.google.com> → **Add project** (name e.g. `dz-app`; Analytics
   can stay off).
2. In the project: **Build → Authentication → Get started → Sign-in method → Email/Password →
   Enable → Save**.
3. **Project settings (gear icon) → General → Web API Key** — copy it.
4. Paste it into `FirebaseConfig.apiKey`
   (`shared/src/commonMain/kotlin/com/example/dz/data/remote/api/FirebaseConfig.kt`).
5. Set `useMockBackend = false` in `ApiConfig` to go live.
