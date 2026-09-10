package com.example.dz.core.auth

/**
 * The two kinds of code the server mails, and where each one's last send is recorded locally.
 *
 * Recording it is what lets the resend countdown mean "time since a code was sent" rather than
 * "time since this screen was built". The code screen is reached from a cold start as well as
 * from the screen that asked for the code — an unverified session reopens straight onto it — and
 * a countdown started at construction would make a reader whose code expired days ago sit out the
 * full wait before they could ask for another.
 *
 * The kinds are kept apart because the server keeps them apart: asking for a reset code says
 * nothing about how recently a confirmation code went out, so one flow's cooldown must not gate
 * the other's.
 *
 * Named here rather than on `VerificationPurpose` because the send happens in the data layer,
 * which has no business importing a presentation type.
 */
enum class MailedCodeKind(val lastSentSettingKey: String) {
    EmailVerification("code_sent_at_email_verification"),
    PasswordReset("code_sent_at_password_reset"),
}
