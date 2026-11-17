package com.example.dz.data.mapper

import com.example.dz.data.remote.api.AddressDto
import com.example.dz.data.remote.api.ProfileResponseDto
import com.example.dz.data.remote.api.UpdateProfileRequestDto
import com.example.dz.data.remote.api.UsernameAvailabilityDto
import com.example.dz.domain.model.BirthDate
import com.example.dz.domain.model.Countries
import com.example.dz.domain.model.EditableProfile
import com.example.dz.domain.model.Gender
import com.example.dz.domain.model.PostalAddress
import com.example.dz.domain.model.ProfileVisibility
import com.example.dz.domain.model.SpecialityKind
import com.example.dz.domain.model.UsernameAvailability
import com.example.dz.domain.model.UsernameProblem

/**
 * Between dz-server's profile JSON and [EditableProfile].
 *
 * The server stores a phone number whole — "+44 7700 900 123" — beside the country it was entered
 * for. The app shows the dial code on a chip and only the rest in the field, so the code is joined
 * on the way out and split off on the way in.
 */
object ProfileMapper {

    fun toDomain(dto: ProfileResponseDto): EditableProfile = EditableProfile(
        name = dto.user.name,
        username = dto.user.username,
        email = dto.user.email,
        emailVerified = dto.user.emailVerified,
        avatarUrl = dto.user.avatarUrl,
        bannerUrl = dto.user.bannerUrl,
        bio = dto.bio,
        phoneCountry = dto.phoneCountry,
        phoneNumber = dto.phoneNumber?.let { splitDialCode(it, dto.phoneCountry) },
        dateOfBirth = dto.dateOfBirth?.let(BirthDate::parseIso),
        gender = when (dto.gender) {
            "FEMALE" -> Gender.Female
            "MALE" -> Gender.Male
            "PREFER_NOT_TO_SAY" -> Gender.PreferNotToSay
            else -> null
        },
        specialityKind = when (dto.specialityKind) {
            "WORK" -> SpecialityKind.Work
            "STUDY" -> SpecialityKind.Study
            else -> null
        },
        speciality = dto.speciality,
        address = dto.address?.let {
            PostalAddress(countryCode = it.country, city = it.city, street = it.street, postalCode = it.postalCode)
        } ?: PostalAddress(),
        visibility = if (dto.profileVisibility == "PRIVATE") ProfileVisibility.Private else ProfileVisibility.Open,
        language = dto.language,
        currentGoalMinutes = dto.currentGoalMinutes,
    )

    fun toRequest(profile: EditableProfile): UpdateProfileRequestDto {
        val number = profile.phoneNumber.clean()
        return UpdateProfileRequestDto(
            name = profile.name.trim(),
            username = profile.username.clean()?.removePrefix("@")?.lowercase(),
            avatarUrl = profile.avatarUrl.clean(),
            bannerUrl = profile.bannerUrl.clean(),
            bio = profile.bio.clean(),
            phoneNumber = number?.let { joinDialCode(it, profile.phoneCountry) },
            // A country with no number describes nothing, so it is not kept on its own.
            phoneCountry = profile.phoneCountry.takeIf { number != null },
            language = profile.language,
            currentGoalMinutes = profile.currentGoalMinutes,
            dateOfBirth = profile.dateOfBirth?.toIso(),
            gender = when (profile.gender) {
                Gender.Female -> "FEMALE"
                Gender.Male -> "MALE"
                Gender.PreferNotToSay -> "PREFER_NOT_TO_SAY"
                null -> null
            },
            specialityKind = when (profile.specialityKind) {
                SpecialityKind.Work -> "WORK"
                SpecialityKind.Study -> "STUDY"
                null -> null
            },
            speciality = profile.speciality.clean(),
            address = with(profile.address) {
                val cleaned = PostalAddress(countryCode.clean(), city.clean(), street.clean(), postalCode.clean())
                if (cleaned.isEmpty) null
                else AddressDto(cleaned.countryCode, cleaned.city, cleaned.street, cleaned.postalCode)
            },
            profileVisibility = when (profile.visibility) {
                ProfileVisibility.Open -> "OPEN"
                ProfileVisibility.Private -> "PRIVATE"
            },
        )
    }

    fun toDomain(dto: UsernameAvailabilityDto): UsernameAvailability = UsernameAvailability(
        username = dto.username,
        available = dto.available,
        problem = when (dto.reason) {
            "Taken" -> UsernameProblem.Taken
            "Invalid" -> UsernameProblem.Invalid
            else -> null
        },
        suggestions = dto.suggestions,
    )

    private fun joinDialCode(number: String, countryCode: String?): String {
        val dial = Countries.byCode(countryCode)?.dialCode ?: return number
        return if (number.startsWith("+")) number else "$dial $number"
    }

    private fun splitDialCode(stored: String, countryCode: String?): String {
        val dial = Countries.byCode(countryCode)?.dialCode ?: return stored
        return if (stored.startsWith(dial)) stored.removePrefix(dial).trim() else stored
    }

    /** Blank and whitespace-only become `null`, so the server never stores "   ". */
    private fun String?.clean(): String? = this?.trim()?.ifEmpty { null }
}
