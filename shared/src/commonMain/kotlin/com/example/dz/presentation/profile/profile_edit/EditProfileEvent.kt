package com.example.dz.presentation.profile.profile_edit

import com.example.dz.domain.model.BirthDate
import com.example.dz.domain.model.Gender
import com.example.dz.domain.model.ProfileVisibility
import com.example.dz.domain.model.SpecialityKind

sealed interface EditProfileEvent {
    /**
     * Sent on every keystroke in a text field. Every other event hides the keyboard on its way to
     * the ViewModel (see EditProfileScreen), so a new text field's event must be one of these.
     */
    sealed interface Typing : EditProfileEvent

    // Fields
    data class NameChanged(val value: String) : Typing
    data class UsernameChanged(val value: String) : Typing
    data class UsernameSuggestionPicked(val value: String) : EditProfileEvent
    data class BioChanged(val value: String) : Typing
    data class SpecialityKindSelected(val kind: SpecialityKind) : EditProfileEvent
    data class SpecialityChanged(val value: String) : Typing
    data class VisibilitySelected(val visibility: ProfileVisibility) : EditProfileEvent
    data class PhoneNumberChanged(val value: String) : Typing
    data class CityChanged(val value: String) : Typing
    data class StreetChanged(val value: String) : Typing
    data class PostalCodeChanged(val value: String) : Typing

    // Sheets
    data object BannerClicked : EditProfileEvent
    data object PhotoClicked : EditProfileEvent
    data object RemovePhotoClicked : EditProfileEvent
    data object DateOfBirthClicked : EditProfileEvent
    data class DateOfBirthPicked(val date: BirthDate) : EditProfileEvent
    data object GenderClicked : EditProfileEvent
    data class GenderPicked(val gender: Gender) : EditProfileEvent
    data object PhoneCountryClicked : EditProfileEvent
    data object AddressCountryClicked : EditProfileEvent
    data class CountryPicked(val code: String) : EditProfileEvent
    data object SheetDismissed : EditProfileEvent

    // Saving and leaving
    data object SaveClicked : EditProfileEvent
    data object OneTimeConfirmed : EditProfileEvent
    data object CancelClicked : EditProfileEvent
    data object DiscardConfirmed : EditProfileEvent
    data object DialogDismissed : EditProfileEvent
    data object RetryLoad : EditProfileEvent
}
