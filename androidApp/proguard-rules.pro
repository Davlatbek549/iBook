# R8 rules for the app module. Most libraries here (kotlinx.serialization, Ktor, Coil, Koin,
# SQLDelight, Compose) ship their own consumer rules; only what they do not cover lives here.

# Credential Manager finds its Play Services provider by reflection, so R8 sees nothing using it
# and strips it — Google sign-in then fails in release builds only. From the Credential Manager
# integration guide.
-if class androidx.credentials.CredentialManager
-keep class androidx.credentials.playservices.** {
  *;
}
