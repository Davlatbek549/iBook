package com.example.dz.presentation.profile.profile_edit

import com.example.dz.domain.model.BirthDate
import com.example.dz.domain.model.EditableProfile
import com.example.dz.domain.model.Gender
import com.example.dz.domain.model.PostalAddress
import com.example.dz.domain.model.SpecialityKind

/**
 * [saved] is the profile as the server last confirmed it; [draft] is the form. Everything Edit
 * Profile decides — whether Save is live, whether Cancel asks first, which one-time fields need a
 * confirmation — comes from comparing the two.
 */
data class EditProfileUiState(
    val isLoading: Boolean = true,
    val loadError: String? = null,
    val saved: EditableProfile? = null,
    val draft: EditableProfile = EditableProfile(name = ""),
    val usernameStatus: UsernameStatus = UsernameStatus.Unchanged,
    val isSaving: Boolean = false,
    val saveError: String? = null,
    val sheet: EditProfileSheet? = null,
    val dialog: EditProfileDialog? = null,
    /** Where a new phone number's dial code starts when neither it nor the address says. */
    val defaultPhoneCountry: String = FALLBACK_COUNTRY,
    /** The last day a date of birth may be; also where the year wheel ends. */
    val today: BirthDate = BirthDate(2026, 1, 1),
) {
    val hasChanges: Boolean
        get() = saved != null && draft.normalized() != saved.normalized()

    val nameMissing: Boolean get() = draft.name.isBlank()

    val canSave: Boolean
        get() = hasChanges && !nameMissing && !usernameStatus.blocksSave && !isSaving

    /** Set before this visit, so read-only. A value picked in this visit stays editable until Save. */
    val dateOfBirthLocked: Boolean get() = saved?.dateOfBirth != null
    val genderLocked: Boolean get() = saved?.gender != null

    /** The one-time fields this Save would set for the first time, which it must confirm first. */
    val pendingOneTime: PendingOneTime
        get() = PendingOneTime(
            dateOfBirth = draft.dateOfBirth.takeIf { saved?.dateOfBirth == null },
            gender = draft.gender.takeIf { saved?.gender == null },
        )

    /** The chip shows this even before a number is typed. */
    val phoneCountry: String
        get() = draft.phoneCountry ?: draft.address.countryCode ?: defaultPhoneCountry

    val specialityKind: SpecialityKind get() = draft.specialityKind ?: SpecialityKind.Work

    /** In form order, for the discard dialog's "your edits to your bio and banner". */
    val changedFields: List<ProfileField>
        get() {
            val before = saved?.normalized() ?: return emptyList()
            val after = draft.normalized()
            return buildList {
                if (after.avatarUrl != before.avatarUrl) add(ProfileField.Photo)
                if (after.bannerUrl != before.bannerUrl) add(ProfileField.Banner)
                if (after.name != before.name) add(ProfileField.Name)
                if (after.username != before.username) add(ProfileField.Username)
                if (after.bio != before.bio) add(ProfileField.Bio)
                if (after.speciality != before.speciality || after.specialityKind != before.specialityKind) {
                    add(ProfileField.Speciality)
                }
                if (after.visibility != before.visibility) add(ProfileField.Visibility)
                if (after.phoneNumber != before.phoneNumber || after.phoneCountry != before.phoneCountry) {
                    add(ProfileField.Phone)
                }
                if (after.dateOfBirth != before.dateOfBirth) add(ProfileField.DateOfBirth)
                if (after.gender != before.gender) add(ProfileField.Gender)
                if (after.address != before.address) add(ProfileField.Address)
            }
        }

    companion object {
        const val FALLBACK_COUNTRY = "GB"
    }
}

data class PendingOneTime(val dateOfBirth: BirthDate?, val gender: Gender?) {
    val any: Boolean get() = dateOfBirth != null || gender != null
}

/** What the live username check says about the username field. */
sealed interface UsernameStatus {
    /** The saved username, untouched — nothing to check. */
    data object Unchanged : UsernameStatus

    /** Empty. A username is required, so Save waits for one. */
    data object Missing : UsernameStatus

    data object Checking : UsernameStatus
    data object Available : UsernameStatus
    data class Taken(val suggestions: List<String>) : UsernameStatus

    /** Characters a username may not hold, or the wrong length. Caught before asking the server. */
    data object Invalid : UsernameStatus

    /** The check itself failed. Not a reason to block Save: the server checks again then. */
    data object Unknown : UsernameStatus

    val blocksSave: Boolean
        get() = this is Missing || this is Checking || this is Taken || this is Invalid
}

sealed interface EditProfileSheet {
    data class Photo(val target: PhotoTarget) : EditProfileSheet
    data object DateOfBirth : EditProfileSheet
    data object Gender : EditProfileSheet
    data class Country(val target: CountryTarget) : EditProfileSheet
}

enum class PhotoTarget { Banner, Profile }
enum class CountryTarget { Phone, Address }

sealed interface EditProfileDialog {
    /** First save of a date of birth or gender: say what will be locked before locking it. */
    data class ConfirmOneTime(val pending: PendingOneTime) : EditProfileDialog

    /** Cancel with unsaved edits. */
    data object Discard : EditProfileDialog
}

enum class ProfileField { Photo, Banner, Name, Username, Bio, Speciality, Visibility, Phone, DateOfBirth, Gender, Address }

/**
 * The form as it would be saved: trimmed, blanks as `null`, the username lower case. Two drafts
 * that would save the same are equal, so typing a space and deleting it is not a change.
 *
 * A speciality kind means nothing without its text, so it only counts alongside one — choosing
 * Study on an empty speciality is not an edit.
 */
internal fun EditableProfile.normalized(): EditableProfile {
    val number = phoneNumber.clean()
    val specialityText = speciality.clean()
    return copy(
        name = name.trim(),
        username = username.clean()?.removePrefix("@")?.lowercase(),
        avatarUrl = avatarUrl.clean(),
        bannerUrl = bannerUrl.clean(),
        bio = bio.clean(),
        phoneNumber = number,
        phoneCountry = phoneCountry.takeIf { number != null },
        speciality = specialityText,
        specialityKind = if (specialityText == null) null else (specialityKind ?: SpecialityKind.Work),
        address = PostalAddress(
            countryCode = address.countryCode.clean(),
            city = address.city.clean(),
            street = address.street.clean(),
            postalCode = address.postalCode.clean(),
        ),
    )
}

private fun String?.clean(): String? = this?.trim()?.ifEmpty { null }
