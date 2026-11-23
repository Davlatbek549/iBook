package com.example.dz.presentation.profile.profile_edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.core.error.AppError
import com.example.dz.core.media.CropGeometry
import com.example.dz.core.media.CropPosition
import com.example.dz.core.media.CropRegion
import com.example.dz.core.media.CropShape
import com.example.dz.core.media.PickedImage
import com.example.dz.core.media.cropToJpeg
import com.example.dz.core.platform.deviceRegionCode
import com.example.dz.core.result.AppResult
import com.example.dz.core.time.currentEpochMillis
import com.example.dz.core.time.localUtcOffsetMillis
import com.example.dz.domain.model.BirthDate
import com.example.dz.domain.model.Countries
import com.example.dz.domain.model.EditableProfile
import com.example.dz.domain.model.ProfileImage
import com.example.dz.domain.model.UsernameProblem
import com.example.dz.domain.usecase.user.CheckUsernameUseCase
import com.example.dz.domain.usecase.user.GetEditableProfileUseCase
import com.example.dz.domain.usecase.user.SaveEditableProfileUseCase
import com.example.dz.domain.usecase.user.SaveProfileImageUseCase
import com.example.dz.presentation.mvi.toPresentationMessage
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditProfileViewModel(
    private val getProfile: GetEditableProfileUseCase,
    private val saveProfile: SaveEditableProfileUseCase,
    private val checkUsername: CheckUsernameUseCase,
    private val saveImage: SaveProfileImageUseCase,
    /** Injected so tests do not depend on the machine's clock or locale. */
    today: () -> BirthDate = {
        val now = currentEpochMillis()
        BirthDate.fromEpochMillis(now, localUtcOffsetMillis(now))
    },
    deviceRegion: () -> String? = ::deviceRegionCode,
    /** Injected so tests need no image codec. */
    private val cropImage: suspend (PickedImage, CropRegion, CropShape) -> PickedImage = ::cropToJpeg,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        EditProfileUiState(
            today = today(),
            defaultPhoneCountry = deviceRegion()?.takeIf { Countries.byCode(it) != null }
                ?: EditProfileUiState.FALLBACK_COUNTRY,
        )
    )
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<EditProfileEffect>()
    val effects = _effects.asSharedFlow()

    private var usernameCheck: Job? = null

    init {
        load()
    }

    fun onEvent(event: EditProfileEvent) {
        // Nothing on the form moves while a save is in flight; the screen dims it to say so.
        if (_uiState.value.isSaving) return
        when (event) {
            is EditProfileEvent.NameChanged -> editDraft { copy(name = event.value.take(NAME_MAX)) }
            is EditProfileEvent.UsernameChanged -> onUsernameTyped(event.value)
            is EditProfileEvent.UsernameSuggestionPicked -> onUsernameTyped(event.value)
            is EditProfileEvent.BioChanged -> editDraft { copy(bio = event.value.take(BIO_MAX)) }
            is EditProfileEvent.SpecialityKindSelected -> editDraft { copy(specialityKind = event.kind) }
            is EditProfileEvent.SpecialityChanged ->
                editDraft { copy(speciality = event.value.take(SPECIALITY_MAX)) }
            is EditProfileEvent.VisibilitySelected -> editDraft { copy(visibility = event.visibility) }
            is EditProfileEvent.PhoneNumberChanged -> editDraft {
                copy(
                    phoneNumber = event.value.filter { it.isDigit() || it in PHONE_PUNCTUATION }.take(PHONE_MAX),
                    // Pin the chip's country the moment a number is typed against it.
                    phoneCountry = phoneCountry ?: _uiState.value.phoneCountry,
                )
            }
            is EditProfileEvent.CityChanged -> editDraft { copy(address = address.copy(city = event.value.take(CITY_MAX))) }
            is EditProfileEvent.StreetChanged ->
                editDraft { copy(address = address.copy(street = event.value.take(STREET_MAX))) }
            is EditProfileEvent.PostalCodeChanged ->
                editDraft { copy(address = address.copy(postalCode = event.value.take(POSTAL_MAX))) }

            EditProfileEvent.BannerClicked -> openSheet(EditProfileSheet.Photo(PhotoTarget.Banner))
            EditProfileEvent.PhotoClicked -> openSheet(EditProfileSheet.Photo(PhotoTarget.Profile))
            EditProfileEvent.RemovePhotoClicked -> removePhoto()
            is EditProfileEvent.PhotoPicked -> usePickedPhoto(event)
            EditProfileEvent.PhotoUnreadable -> _uiState.update { it.copy(sheet = null, photoUnreadable = true) }
            is EditProfileEvent.CropConfirmed -> useCrop(event.position)
            EditProfileEvent.CropCancelled -> _uiState.update { if (it.crop?.isCutting == true) it else it.copy(crop = null) }
            EditProfileEvent.DateOfBirthClicked ->
                if (!_uiState.value.dateOfBirthLocked) openSheet(EditProfileSheet.DateOfBirth)
            is EditProfileEvent.DateOfBirthPicked -> {
                val latest = _uiState.value.today
                editDraft { copy(dateOfBirth = minOf(event.date, latest)) }
                closeSheet()
            }
            EditProfileEvent.GenderClicked -> if (!_uiState.value.genderLocked) openSheet(EditProfileSheet.Gender)
            is EditProfileEvent.GenderPicked -> {
                editDraft { copy(gender = event.gender) }
                closeSheet()
            }
            EditProfileEvent.PhoneCountryClicked -> openSheet(EditProfileSheet.Country(CountryTarget.Phone))
            EditProfileEvent.AddressCountryClicked -> openSheet(EditProfileSheet.Country(CountryTarget.Address))
            is EditProfileEvent.CountryPicked -> pickCountry(event.code)
            EditProfileEvent.SheetDismissed -> closeSheet()

            EditProfileEvent.SaveClicked -> onSave()
            EditProfileEvent.OneTimeConfirmed -> save()
            EditProfileEvent.CancelClicked -> onCancel()
            EditProfileEvent.DiscardConfirmed -> {
                _uiState.update { it.copy(dialog = null) }
                emit(EditProfileEffect.NavigateBack)
            }
            EditProfileEvent.DialogDismissed -> _uiState.update { it.copy(dialog = null) }
            EditProfileEvent.RetryLoad -> load()
        }
    }

    private fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, loadError = null) }
            when (val result = getProfile()) {
                is AppResult.Success -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        saved = result.data,
                        draft = result.data,
                        usernameStatus = statusForUnchecked(result.data.username, result.data.username)
                            ?: UsernameStatus.Unchanged,
                    )
                }
                is AppResult.Error -> _uiState.update {
                    it.copy(isLoading = false, loadError = result.error.toPresentationMessage())
                }
            }
        }
    }

    private fun editDraft(change: EditableProfile.() -> EditableProfile) {
        _uiState.update { it.copy(draft = it.draft.change(), saveError = null, photoUnreadable = false) }
    }

    /**
     * Shown as typed but lower case, without spaces or a leading "@" — the server would store it
     * that way anyway, so the field never shows something other than what will be saved.
     *
     * Checked against the server 400ms after typing stops. A newer keystroke cancels the wait and
     * any check in flight, so a slow answer about "amel" can never overwrite the one about "amelia".
     */
    private fun onUsernameTyped(raw: String, keepSaveError: Boolean = false) {
        val username = raw.trim().removePrefix("@").replace(" ", "").lowercase().take(USERNAME_MAX)
        val savedUsername = _uiState.value.saved?.username
        usernameCheck?.cancel()

        val immediate = statusForUnchecked(username, savedUsername)
        _uiState.update {
            it.copy(
                draft = it.draft.copy(username = username),
                usernameStatus = immediate ?: UsernameStatus.Checking,
                saveError = if (keepSaveError) it.saveError else null,
            )
        }
        if (immediate != null) return

        usernameCheck = viewModelScope.launch {
            delay(USERNAME_CHECK_DELAY_MS)
            val status = when (val result = checkUsername(username)) {
                is AppResult.Success -> when {
                    result.data.available -> UsernameStatus.Available
                    result.data.problem == UsernameProblem.Invalid -> UsernameStatus.Invalid
                    else -> UsernameStatus.Taken(result.data.suggestions)
                }
                is AppResult.Error -> UsernameStatus.Unknown
            }
            _uiState.update { state ->
                if (state.draft.username == username) state.copy(usernameStatus = status) else state
            }
        }
    }

    /** The status a username has without asking the server, or `null` when it must be asked. */
    private fun statusForUnchecked(username: String?, savedUsername: String?): UsernameStatus? = when {
        username.isNullOrEmpty() -> UsernameStatus.Missing
        username == savedUsername -> UsernameStatus.Unchanged
        !USERNAME_REGEX.matches(username) -> UsernameStatus.Invalid
        else -> null
    }

    private fun openSheet(sheet: EditProfileSheet) = _uiState.update { it.copy(sheet = sheet, photoUnreadable = false) }

    private fun closeSheet() = _uiState.update { it.copy(sheet = null) }

    private fun removePhoto() {
        val target = (_uiState.value.sheet as? EditProfileSheet.Photo)?.target ?: return
        // Removes a new pick as well as the saved picture: "Remove" leaves no photo either way.
        _uiState.update {
            when (target) {
                PhotoTarget.Banner -> it.copy(draft = it.draft.copy(bannerUrl = null), newBanner = null)
                PhotoTarget.Profile -> it.copy(draft = it.draft.copy(avatarUrl = null), newPhoto = null)
            }.copy(sheet = null, saveError = null)
        }
    }

    /**
     * The sheet stays open while the camera or library is up, so it still says which picture the
     * pick is for when the answer comes back. The pick goes to the crop screen, not the form.
     */
    private fun usePickedPhoto(event: EditProfileEvent.PhotoPicked) {
        val target = (_uiState.value.sheet as? EditProfileSheet.Photo)?.target ?: return
        _uiState.update {
            it.copy(crop = PhotoCrop(target, event.image), sheet = null, saveError = null, photoUnreadable = false)
        }
    }

    private fun useCrop(position: CropPosition) {
        val crop = _uiState.value.crop?.takeUnless { it.isCutting } ?: return
        _uiState.update { it.copy(crop = crop.copy(isCutting = true)) }
        viewModelScope.launch {
            val region = CropGeometry(crop.image.width, crop.image.height, crop.shape).region(position)
            val cropped = try {
                cropImage(crop.image, region, crop.shape)
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                null
            }
            _uiState.update {
                val placed = when {
                    cropped == null -> it
                    crop.target == PhotoTarget.Banner -> it.copy(newBanner = cropped)
                    else -> it.copy(newPhoto = cropped)
                }
                placed.copy(crop = null, photoUnreadable = cropped == null)
            }
        }
    }

    private fun pickCountry(code: String) {
        val target = (_uiState.value.sheet as? EditProfileSheet.Country)?.target ?: return
        editDraft {
            when (target) {
                CountryTarget.Phone -> copy(phoneCountry = code)
                // An address country also seeds the phone's, if the phone has not chosen one.
                CountryTarget.Address -> copy(
                    address = address.copy(countryCode = code),
                    phoneCountry = phoneCountry ?: code.takeIf { phoneNumber.isNullOrBlank() },
                )
            }
        }
        closeSheet()
    }

    private fun onSave() {
        val state = _uiState.value
        if (!state.canSave) return
        val pending = state.pendingOneTime
        if (pending.any) {
            _uiState.update { it.copy(dialog = EditProfileDialog.ConfirmOneTime(pending)) }
        } else {
            save()
        }
    }

    /**
     * One Save is up to three calls: the fields, then the photo, then the banner, each only when it
     * changed. Fields go first, so a refused username stops the Save before any picture is sent.
     *
     * A call that succeeds stays saved even if a later one fails. The form then shows what is
     * stored and keeps what is not, so Save stays live and sends only what is left.
     */
    private fun save() {
        val saved = _uiState.value.saved ?: return
        val draft = _uiState.value.draft.normalized()
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, dialog = null, saveError = null) }

            if (draft.withoutPictures() != saved.normalized().withoutPictures()) {
                when (val result = saveProfile(draft)) {
                    is AppResult.Success -> _uiState.update {
                        // The pictures in the draft are still to do; everything else is now stored.
                        it.copy(
                            saved = result.data,
                            draft = result.data.copy(avatarUrl = it.draft.avatarUrl, bannerUrl = it.draft.bannerUrl),
                            usernameStatus = UsernameStatus.Unchanged,
                        )
                    }
                    is AppResult.Error -> return@launch saveFailed(result.error, draft.username)
                }
            }

            for (image in ProfileImage.entries) {
                val change = pictureChange(_uiState.value, image) ?: continue
                when (val result = saveImage(image, change.jpeg)) {
                    is AppResult.Success -> _uiState.update { it.withStoredPicture(image, result.data) }
                    is AppResult.Error -> return@launch saveFailed(result.error, username = null)
                }
            }

            _uiState.update { it.copy(isSaving = false) }
            emit(EditProfileEffect.Saved)
        }
    }

    private fun saveFailed(error: AppError, username: String?) {
        _uiState.update {
            it.copy(
                isSaving = false,
                saveError = error.toPresentationMessage(),
                // Someone took the username between the check and the save: show it as taken, and
                // ask again so the suggestions come with it.
                usernameStatus = if (error.isUsernameTaken()) UsernameStatus.Taken(emptyList()) else it.usernameStatus,
            )
        }
        if (error.isUsernameTaken()) username?.let { onUsernameTyped(it, keepSaveError = true) }
    }

    /** `jpeg` replaces the picture; `null` removes it. */
    private class PictureChange(val jpeg: ByteArray?)

    /** What Save has to do for [image]: send a new pick, remove the stored one, or nothing. */
    private fun pictureChange(state: EditProfileUiState, image: ProfileImage): PictureChange? {
        val (picked, wanted, stored) = when (image) {
            ProfileImage.Photo -> Triple(state.newPhoto, state.draft.avatarUrl, state.saved?.avatarUrl)
            ProfileImage.Banner -> Triple(state.newBanner, state.draft.bannerUrl, state.saved?.bannerUrl)
        }
        return when {
            picked != null -> PictureChange(picked.bytes)
            wanted == null && stored != null -> PictureChange(null)
            else -> null
        }
    }

    /** [stored] is the whole profile after one picture changed; only that picture is taken from it. */
    private fun EditProfileUiState.withStoredPicture(image: ProfileImage, stored: EditableProfile) = when (image) {
        ProfileImage.Photo -> copy(
            saved = saved?.copy(avatarUrl = stored.avatarUrl),
            draft = draft.copy(avatarUrl = stored.avatarUrl),
            newPhoto = null,
        )
        ProfileImage.Banner -> copy(
            saved = saved?.copy(bannerUrl = stored.bannerUrl),
            draft = draft.copy(bannerUrl = stored.bannerUrl),
            newBanner = null,
        )
    }

    private fun EditableProfile.withoutPictures() = copy(avatarUrl = null, bannerUrl = null)

    private fun onCancel() {
        if (_uiState.value.hasChanges) {
            _uiState.update { it.copy(dialog = EditProfileDialog.Discard) }
        } else {
            emit(EditProfileEffect.NavigateBack)
        }
    }

    private fun emit(effect: EditProfileEffect) {
        viewModelScope.launch { _effects.emit(effect) }
    }

    private fun AppError.isUsernameTaken() =
        this is AppError.Profile && reason == AppError.ProfileReason.UsernameTaken

    private companion object {
        val USERNAME_REGEX = Regex("^[a-z0-9._]{3,30}$")
        const val USERNAME_CHECK_DELAY_MS = 400L
        const val USERNAME_MAX = 30
        const val NAME_MAX = 100
        const val BIO_MAX = 500
        const val SPECIALITY_MAX = 100
        const val PHONE_MAX = 20
        const val CITY_MAX = 100
        const val STREET_MAX = 200
        const val POSTAL_MAX = 20
        const val PHONE_PUNCTUATION = " -()"
    }
}
