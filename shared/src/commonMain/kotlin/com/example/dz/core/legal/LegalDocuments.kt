package com.example.dz.core.legal

/**
 * The terms and privacy text shown before someone creates an account.
 *
 * Kept here as whole documents rather than as loose entries in `strings.xml` because that is how
 * legal text is actually maintained and reviewed — as one versioned document, not forty
 * independent keys that can drift apart. Translating it means adding a locale variant of the
 * whole document, which is also how a translated policy has to be signed off.
 */

/**
 * Bumped whenever the wording changes in a way a reader would care about.
 *
 * Recording *which* version someone accepted, rather than a bare "agreed" flag, is what makes it
 * possible to ask again later without asking everyone: when the AI features land and the policy
 * gains a section, only the people below the new number need re-prompting.
 */
const val LEGAL_DOCUMENTS_VERSION = 1

sealed interface LegalBlock {
    data class Paragraph(val text: String) : LegalBlock
    data class Bullets(val items: List<String>) : LegalBlock
}

data class LegalSection(
    val heading: String,
    val blocks: List<LegalBlock>,
)

data class LegalDocument(
    val title: String,
    val version: Int,
    val sections: List<LegalSection>,
)

/** Which document a reader asked for; both are reachable from the sign-up screen. */
enum class LegalDocumentKind {
    Terms,
    Privacy,
}

object LegalDocuments {

    fun of(kind: LegalDocumentKind): LegalDocument = when (kind) {
        LegalDocumentKind.Terms -> terms
        LegalDocumentKind.Privacy -> privacy
    }

    private fun paragraphs(vararg text: String): List<LegalBlock> =
        text.map { LegalBlock.Paragraph(it) }

    val terms = LegalDocument(
        title = "Terms of Service",
        version = LEGAL_DOCUMENTS_VERSION,
        sections = listOf(
            LegalSection(
                heading = "What DZ is",
                blocks = paragraphs(
                    "DZ is a reading app. It gives you a library of public-domain books, a place " +
                        "to track what you're reading, and shelves to organise it. Books come from " +
                        "Project Gutenberg through Gutendex, and cover art and details from OpenLibrary."
                )
            ),
            LegalSection(
                heading = "Your account",
                blocks = paragraphs(
                    "You need an account to use DZ. You can create one with an email and password, " +
                        "or with Google. You're responsible for what happens under your account, so " +
                        "keep your password to yourself and tell us if you think someone else has it.",
                    "One person, one account. You must be old enough to agree to these terms where " +
                        "you live — if you're under 16, ask a parent or guardian first."
                )
            ),
            LegalSection(
                heading = "The books",
                blocks = paragraphs(
                    "The books in DZ are in the public domain, which means nobody owns the right to " +
                        "stop you reading them. DZ doesn't own them either. We do our best to show " +
                        "accurate titles, authors and covers, but that information comes from other " +
                        "people's catalogues and is sometimes wrong."
                )
            ),
            LegalSection(
                heading = "What we ask of you",
                blocks = listOf(
                    LegalBlock.Bullets(
                        listOf(
                            "Don't try to break, overload, or get around the security of the service.",
                            "Don't use someone else's account, or let anyone use yours.",
                            "Don't scrape the app or use it to redistribute content at scale.",
                            "Don't upload anything unlawful into the parts of DZ you can write to.",
                        )
                    ),
                    LegalBlock.Paragraph(
                        "If you do these things we may suspend or close your account. Where it's " +
                            "something minor and fixable, we'll tell you first."
                    )
                )
            ),
            LegalSection(
                heading = "Closing your account",
                blocks = paragraphs(
                    "You can close your account at any time from Settings. When you do, we delete " +
                        "your account and everything attached to it — your library, your progress, " +
                        "your collections. Backups are overwritten within 30 days."
                )
            ),
            LegalSection(
                heading = "What DZ doesn't promise",
                blocks = paragraphs(
                    "DZ is provided as it is. We try to keep it running and to keep your data safe, " +
                        "but we can't promise it will never be unavailable or never lose data, and " +
                        "we're not liable for losses that follow from using it. Nothing here takes " +
                        "away rights you have by law as a consumer."
                )
            ),
            LegalSection(
                heading = "Changes",
                blocks = paragraphs(
                    "If we change these terms in a way that matters, we'll tell you in the app and " +
                        "ask you to agree again before you carry on. Small corrections — a typo, a " +
                        "clearer sentence — we'll just make."
                )
            ),
        )
    )

    val privacy = LegalDocument(
        title = "Privacy Policy",
        version = LEGAL_DOCUMENTS_VERSION,
        sections = listOf(
            LegalSection(
                heading = "The short version",
                blocks = paragraphs(
                    "DZ keeps what it needs to give you an account and remember what you're reading. " +
                        "We don't sell your data, we don't advertise to you, and we don't track you " +
                        "across other apps or websites."
                )
            ),
            LegalSection(
                heading = "What we collect, and why",
                blocks = listOf(
                    LegalBlock.Bullets(
                        listOf(
                            "Your email and name — to identify your account and contact you about it. " +
                                "From sign-up, or from Google if you sign in that way.",
                            "Your password — stored as a BCrypt hash. We can't read it, and nobody at " +
                                "DZ can tell you what it is.",
                            "Your Google account id, if you use Google sign-in — so we recognise you " +
                                "next time. We ask Google only for your name, email address and " +
                                "profile picture.",
                            "Optional profile details — avatar, bio, phone number, language. Only what " +
                                "you choose to fill in.",
                            "Your library, reading progress, collections and reading goal — so your " +
                                "shelf is the same on every device you sign in on.",
                            "Session tokens — to keep you signed in. Stored hashed, and revoked when " +
                                "you sign out.",
                        )
                    ),
                    LegalBlock.Paragraph(
                        "We don't collect location, contacts, advertising identifiers, or anything " +
                            "you haven't given us on purpose."
                    )
                )
            ),
            LegalSection(
                heading = "Who else sees something",
                blocks = listOf(
                    LegalBlock.Bullets(
                        listOf(
                            "Google — only if you choose Google sign-in, and only to confirm who you are.",
                            "Gutendex and OpenLibrary — when you browse or open a book, DZ asks them " +
                                "for it, so they see that request. They don't receive your name or email.",
                            "Render and Neon — they host the service and its database, in Frankfurt. " +
                                "They hold the data on our behalf and don't use it for anything else.",
                        )
                    ),
                    LegalBlock.Paragraph(
                        "We don't sell your data or share it with advertisers. There is no such " +
                            "arrangement to opt out of."
                    )
                )
            ),
            LegalSection(
                heading = "Where your data lives",
                blocks = paragraphs(
                    "On servers in Frankfurt, Germany. If you're outside the EU, your data is " +
                        "processed there."
                )
            ),
            LegalSection(
                heading = "How long we keep it",
                blocks = paragraphs(
                    "For as long as your account exists. Close your account and we delete it, with " +
                        "backups overwritten within 30 days. Expired session tokens are cleared out " +
                        "as we go."
                )
            ),
            LegalSection(
                heading = "What you can do",
                blocks = listOf(
                    LegalBlock.Bullets(
                        listOf(
                            "See it — most of it is visible in the app; ask us for the rest.",
                            "Correct it — edit your profile at any time.",
                            "Delete it — close your account from Settings and it goes.",
                            "Take it with you — ask and we'll send you your data in a readable format.",
                            "Complain — if you're in the EU or UK you can complain to your data " +
                                "protection authority.",
                        )
                    )
                )
            ),
            LegalSection(
                heading = "Children",
                blocks = paragraphs(
                    "DZ isn't meant for children under 16. We don't knowingly keep data about them, " +
                        "and if we find we have, we delete it."
                )
            ),
            LegalSection(
                heading = "Changes",
                blocks = paragraphs(
                    "If we start collecting something new or using your data for something new — for " +
                        "example a feature that suggests what to read next — we'll update this " +
                        "policy, tell you in the app, and ask you to agree before that feature does " +
                        "anything with your data."
                )
            ),
        )
    )
}
