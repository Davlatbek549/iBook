package com.example.dz.domain.model

/**
 * Everything Edit Profile shows and saves, as dz-server's `/users/me` holds it.
 *
 * Saving sends the whole of this back, because the server treats a profile write as a full
 * replacement: a field left out is cleared. [language] and [currentGoalMinutes] are not edited on
 * that screen but travel with it for exactly that reason.
 *
 * The four fields the server never clears by omission — [username], [visibility],
 * [dateOfBirth] and [gender] — are still sent every time; repeating them is harmless.
 */
data class EditableProfile(
    val name: String,
    val username: String? = null,
    val email: String? = null,
    val emailVerified: Boolean = false,
    val avatarUrl: String? = null,
    val bannerUrl: String? = null,
    val bio: String? = null,
    /** ISO 3166 alpha-2 code of the country the number was entered for, e.g. "GB". */
    val phoneCountry: String? = null,
    /** The number without its dial code, as the person typed it. */
    val phoneNumber: String? = null,
    /** Can be set once; after that only support can change it. */
    val dateOfBirth: BirthDate? = null,
    /** Can be set once; after that only support can change it. */
    val gender: Gender? = null,
    val specialityKind: SpecialityKind? = null,
    val speciality: String? = null,
    val address: PostalAddress = PostalAddress(),
    val visibility: ProfileVisibility = ProfileVisibility.Open,
    val language: String? = null,
    val currentGoalMinutes: Int? = null,
)

enum class Gender { Female, Male, PreferNotToSay }

/** Whether [EditableProfile.speciality] is a job or a course of study. */
enum class SpecialityKind { Work, Study }

/** Who may see the profile as a whole. New accounts start [Open]. */
enum class ProfileVisibility { Open, Private }

data class PostalAddress(
    /** ISO 3166 alpha-2, e.g. "GB". */
    val countryCode: String? = null,
    val city: String? = null,
    val street: String? = null,
    val postalCode: String? = null,
) {
    val isEmpty: Boolean get() = countryCode == null && city == null && street == null && postalCode == null
}

/**
 * A calendar date with no time or zone, which is all a birthday is. The project has no date
 * library, and this needs nothing beyond reading and writing the ISO form the server uses.
 */
data class BirthDate(val year: Int, val month: Int, val day: Int) : Comparable<BirthDate> {

    init {
        require(month in 1..12) { "month $month" }
        require(day in 1..daysIn(year, month)) { "day $day of $year-$month" }
    }

    /** `1999-03-14`. */
    fun toIso(): String = "${year.toString().padStart(4, '0')}-${month.pad()}-${day.pad()}"

    override fun compareTo(other: BirthDate): Int =
        compareValuesBy(this, other, BirthDate::year, BirthDate::month, BirthDate::day)

    companion object {
        /** Reads `1999-03-14`; anything else, or an impossible date, is `null`. */
        fun parseIso(text: String): BirthDate? {
            val parts = text.trim().split('-')
            if (parts.size != 3) return null
            val (year, month, day) = parts.map { it.toIntOrNull() ?: return null }
            return runCatching { BirthDate(year, month, day) }.getOrNull()
        }

        /**
         * The calendar date at [epochMillis], read at the given UTC offset. Howard Hinnant's
         * days-to-civil algorithm, so no date library is needed for "today".
         */
        fun fromEpochMillis(epochMillis: Long, utcOffsetMillis: Long = 0L): BirthDate {
            val days = floorDiv(epochMillis + utcOffsetMillis, 86_400_000L)
            val z = days + 719_468
            val era = floorDiv(z, 146_097L)
            val doe = z - era * 146_097
            val yoe = (doe - doe / 1_460 + doe / 36_524 - doe / 146_096) / 365
            val doy = doe - (365 * yoe + yoe / 4 - yoe / 100)
            val mp = (5 * doy + 2) / 153
            val day = (doy - (153 * mp + 2) / 5 + 1).toInt()
            val month = (if (mp < 10) mp + 3 else mp - 9).toInt()
            val year = (yoe + era * 400 + if (month <= 2) 1 else 0).toInt()
            return BirthDate(year, month, day)
        }

        private fun floorDiv(a: Long, b: Long): Long {
            val q = a / b
            return if ((a % b != 0L) && ((a < 0) != (b < 0))) q - 1 else q
        }

        fun daysIn(year: Int, month: Int): Int = when (month) {
            2 -> if (isLeapYear(year)) 29 else 28
            4, 6, 9, 11 -> 30
            else -> 31
        }

        private fun isLeapYear(year: Int) = (year % 4 == 0 && year % 100 != 0) || year % 400 == 0

        private fun Int.pad() = toString().padStart(2, '0')
    }
}

/** What dz-server said about a username while someone was typing it. */
data class UsernameAvailability(
    /** As the server normalised it: trimmed, lower case, no "@". */
    val username: String,
    val available: Boolean,
    val problem: UsernameProblem? = null,
    /** Free alternatives, only offered when the username is taken. */
    val suggestions: List<String> = emptyList(),
)

enum class UsernameProblem { Taken, Invalid }
