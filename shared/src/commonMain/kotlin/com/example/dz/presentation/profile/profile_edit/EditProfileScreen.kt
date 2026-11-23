package com.example.dz.presentation.profile.profile_edit

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.core.media.ImagePickResult
import com.example.dz.core.media.rememberImagePicker
import com.example.dz.designsystem.components.icons.OrganicIcons
import com.example.dz.designsystem.components.organic.ORGANIC_GUTTER
import com.example.dz.designsystem.components.organic.OrganicButton
import com.example.dz.designsystem.components.organic.OrganicCard
import com.example.dz.designsystem.components.organic.OrganicCountryCodeChip
import com.example.dz.designsystem.components.organic.OrganicField
import com.example.dz.designsystem.components.organic.OrganicFieldSpinner
import com.example.dz.designsystem.components.organic.OrganicHelperText
import com.example.dz.designsystem.components.organic.OrganicHelperTone
import com.example.dz.designsystem.components.organic.OrganicOptionCard
import com.example.dz.designsystem.components.organic.OrganicPickerRow
import com.example.dz.designsystem.components.organic.OrganicPickerRowState
import com.example.dz.designsystem.components.organic.OrganicProfilePhotos
import com.example.dz.designsystem.components.organic.OrganicSectionLabel
import com.example.dz.designsystem.components.organic.OrganicSectionTag
import com.example.dz.designsystem.components.organic.OrganicSegment
import com.example.dz.designsystem.components.organic.OrganicSegmentedControl
import com.example.dz.designsystem.components.organic.OrganicSuggestionPill
import com.example.dz.designsystem.components.organic.OrganicTagTone
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.OrganicShape
import com.example.dz.designsystem.theme.organicBodyFontFamily
import com.example.dz.designsystem.theme.organicHeadingFontFamily
import com.example.dz.domain.model.Countries
import com.example.dz.domain.model.ProfileVisibility
import com.example.dz.domain.model.SpecialityKind
import com.example.dz.presentation.common.SystemBackHandler
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.ep_address_city
import dz.shared.generated.resources.ep_address_city_hint
import dz.shared.generated.resources.ep_address_country
import dz.shared.generated.resources.ep_address_country_hint
import dz.shared.generated.resources.ep_address_label
import dz.shared.generated.resources.ep_address_postal
import dz.shared.generated.resources.ep_address_postal_hint
import dz.shared.generated.resources.ep_address_street
import dz.shared.generated.resources.ep_address_street_hint
import dz.shared.generated.resources.ep_banner_description
import dz.shared.generated.resources.ep_banner_empty
import dz.shared.generated.resources.ep_bio_count
import dz.shared.generated.resources.ep_bio_hint
import dz.shared.generated.resources.ep_bio_label
import dz.shared.generated.resources.ep_cancel
import dz.shared.generated.resources.ep_dob_hint
import dz.shared.generated.resources.ep_dob_label
import dz.shared.generated.resources.ep_email_change
import dz.shared.generated.resources.ep_email_label
import dz.shared.generated.resources.ep_email_soon
import dz.shared.generated.resources.ep_email_verified
import dz.shared.generated.resources.ep_gender_hint
import dz.shared.generated.resources.ep_gender_label
import dz.shared.generated.resources.ep_load_retry
import dz.shared.generated.resources.ep_name_hint
import dz.shared.generated.resources.ep_name_label
import dz.shared.generated.resources.ep_name_missing
import dz.shared.generated.resources.ep_once_locked
import dz.shared.generated.resources.ep_once_warning
import dz.shared.generated.resources.ep_phone_country
import dz.shared.generated.resources.ep_phone_hint
import dz.shared.generated.resources.ep_phone_label
import dz.shared.generated.resources.ep_photo_description
import dz.shared.generated.resources.ep_photo_hint_empty
import dz.shared.generated.resources.ep_photo_hint_filled
import dz.shared.generated.resources.ep_photo_unreadable
import dz.shared.generated.resources.ep_private_body
import dz.shared.generated.resources.ep_private_tag
import dz.shared.generated.resources.ep_public_body
import dz.shared.generated.resources.ep_public_tag
import dz.shared.generated.resources.ep_save
import dz.shared.generated.resources.ep_speciality_helper
import dz.shared.generated.resources.ep_speciality_hint_study
import dz.shared.generated.resources.ep_speciality_hint_work
import dz.shared.generated.resources.ep_speciality_label
import dz.shared.generated.resources.ep_speciality_study
import dz.shared.generated.resources.ep_speciality_work
import dz.shared.generated.resources.ep_title
import dz.shared.generated.resources.ep_username_available
import dz.shared.generated.resources.ep_username_checking
import dz.shared.generated.resources.ep_username_hint
import dz.shared.generated.resources.ep_username_invalid
import dz.shared.generated.resources.ep_username_label
import dz.shared.generated.resources.ep_username_missing
import dz.shared.generated.resources.ep_username_rule
import dz.shared.generated.resources.ep_username_taken
import dz.shared.generated.resources.ep_username_taken_plain
import dz.shared.generated.resources.ep_visibility_label
import dz.shared.generated.resources.ep_visibility_open
import dz.shared.generated.resources.ep_visibility_open_body
import dz.shared.generated.resources.ep_visibility_private
import dz.shared.generated.resources.ep_visibility_private_body
import org.jetbrains.compose.resources.stringResource

private const val BIO_MAX = 500

/**
 * Edit Profile, as drawn in the design project's "Edit profile · the form" frames: Cancel · title ·
 * Save across the top, then photos, the public fields, visibility, and the private details.
 * Pushed from Settings, so no tab bar.
 */
@Composable
fun EditProfileScreen(
    uiState: EditProfileUiState,
    onEvent: (EditProfileEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    // Taps on empty space are handled app-wide, but here a tap on any control (a segment, a
    // visibility card, a row that opens a sheet, Save) also ends typing, so the keyboard goes.
    val focusManager = LocalFocusManager.current
    val send: (EditProfileEvent) -> Unit = remember(onEvent, focusManager) {
        { event ->
            if (event !is EditProfileEvent.Typing) focusManager.clearFocus()
            onEvent(event)
        }
    }

    // The system back gesture asks the same question Cancel does; over the crop screen, it backs out of the crop.
    SystemBackHandler(enabled = !uiState.isSaving) {
        send(if (uiState.crop != null) EditProfileEvent.CropCancelled else EditProfileEvent.CancelClicked)
    }

    val crop = uiState.crop
    if (crop != null) {
        PhotoCropScreen(
            crop = crop,
            onConfirm = { send(EditProfileEvent.CropConfirmed(it)) },
            onCancel = { send(EditProfileEvent.CropCancelled) },
            modifier = modifier,
        )
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(OrganicColors.bg)
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding(),
        ) {
            TopBar(uiState = uiState, onEvent = send)

            when {
                uiState.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = OrganicColors.accent)
                }
                uiState.loadError != null -> LoadError(message = uiState.loadError, onRetry = { send(EditProfileEvent.RetryLoad) })
                else -> Form(uiState = uiState, onEvent = send)
            }
        }
    }

    // Here rather than in the photo sheet, so the answer still has somewhere to land if the sheet
    // leaves composition while the camera or library is up.
    val imagePicker = rememberImagePicker { result ->
        when (result) {
            is ImagePickResult.Picked -> send(EditProfileEvent.PhotoPicked(result.image))
            is ImagePickResult.Failed -> send(EditProfileEvent.PhotoUnreadable)
            ImagePickResult.Cancelled -> Unit
        }
    }

    EditProfileSheets(uiState = uiState, onEvent = send, imagePicker = imagePicker)
    EditProfileDialogs(uiState = uiState, onEvent = send)
}

@Composable
private fun TopBar(uiState: EditProfileUiState, onEvent: (EditProfileEvent) -> Unit) {
    val body = organicBodyFontFamily()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = ORGANIC_GUTTER)
            .padding(top = 12.dp, bottom = 10.dp),
    ) {
        Text(
            text = stringResource(Res.string.ep_cancel),
            modifier = Modifier
                .align(Alignment.CenterStart)
                .alpha(if (uiState.isSaving) 0.45f else 1f)
                .clickable(enabled = !uiState.isSaving, role = Role.Button) { onEvent(EditProfileEvent.CancelClicked) }
                .padding(vertical = 8.dp),
            fontFamily = body,
            fontSize = 15.sp,
            color = OrganicColors.neutral700,
        )
        Text(
            text = stringResource(Res.string.ep_title),
            modifier = Modifier.align(Alignment.Center),
            fontFamily = organicHeadingFontFamily(),
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp,
            color = OrganicColors.text,
        )
        Box(modifier = Modifier.align(Alignment.CenterEnd)) {
            if (uiState.isSaving) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = OrganicColors.accent700, strokeWidth = 2.5.dp)
            } else {
                Text(
                    text = stringResource(Res.string.ep_save),
                    modifier = Modifier
                        .alpha(if (uiState.canSave) 1f else 0.45f)
                        .clickable(enabled = uiState.canSave, role = Role.Button) { onEvent(EditProfileEvent.SaveClicked) }
                        .padding(vertical = 8.dp),
                    fontFamily = body,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = OrganicColors.accent700,
                )
            }
        }
    }
}

@Composable
private fun LoadError(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = ORGANIC_GUTTER),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = message,
            textAlign = TextAlign.Center,
            fontFamily = organicBodyFontFamily(),
            fontSize = 14.sp,
            lineHeight = 20.sp,
            color = OrganicColors.neutral700,
        )
        Spacer(Modifier.height(18.dp))
        OrganicButton(text = stringResource(Res.string.ep_load_retry), onClick = onRetry)
    }
}

@Composable
private fun Form(uiState: EditProfileUiState, onEvent: (EditProfileEvent) -> Unit) {
    val draft = uiState.draft
    val enabled = !uiState.isSaving
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = ORGANIC_GUTTER)
            .padding(top = 6.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        uiState.saveError?.let { SaveErrorBanner(it) }
        if (uiState.photoUnreadable) SaveErrorBanner(stringResource(Res.string.ep_photo_unreadable))

        OrganicProfilePhotos(
            name = draft.name,
            banner = uiState.bannerModel,
            photo = uiState.photoModel,
            onBannerClick = { onEvent(EditProfileEvent.BannerClicked) },
            onPhotoClick = { onEvent(EditProfileEvent.PhotoClicked) },
            bannerDescription = stringResource(Res.string.ep_banner_description),
            photoDescription = stringResource(Res.string.ep_photo_description),
            emptyBannerLabel = stringResource(Res.string.ep_banner_empty).takeIf { uiState.bannerModel == null },
            hint = stringResource(
                if (uiState.photoModel == null) Res.string.ep_photo_hint_empty else Res.string.ep_photo_hint_filled
            ),
            enabled = enabled,
        )

        SectionIntro(
            tag = stringResource(Res.string.ep_public_tag),
            icon = OrganicIcons.Globe,
            tone = OrganicTagTone.Accent,
            body = stringResource(Res.string.ep_public_body),
        )

        Field(label = stringResource(Res.string.ep_name_label)) {
            OrganicField(
                value = draft.name,
                onValueChange = { onEvent(EditProfileEvent.NameChanged(it)) },
                placeholder = stringResource(Res.string.ep_name_hint),
                enabled = enabled,
                isError = uiState.nameMissing,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words, imeAction = ImeAction.Next),
            )
            if (uiState.nameMissing) {
                OrganicHelperText(stringResource(Res.string.ep_name_missing), tone = OrganicHelperTone.Warning)
            }
        }

        UsernameField(uiState = uiState, onEvent = onEvent, enabled = enabled)

        Field(label = stringResource(Res.string.ep_bio_label)) {
            OrganicField(
                value = draft.bio.orEmpty(),
                onValueChange = { onEvent(EditProfileEvent.BioChanged(it)) },
                placeholder = stringResource(Res.string.ep_bio_hint),
                singleLine = false,
                minHeight = 100.dp,
                enabled = enabled,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
            )
            Text(
                text = stringResource(Res.string.ep_bio_count, draft.bio.orEmpty().length, BIO_MAX),
                modifier = Modifier.fillMaxWidth().padding(end = 8.dp),
                textAlign = TextAlign.End,
                fontFamily = organicBodyFontFamily(),
                fontSize = 12.sp,
                color = OrganicColors.neutral700,
            )
        }

        Field(label = stringResource(Res.string.ep_speciality_label)) {
            OrganicSegmentedControl(
                options = listOf(
                    OrganicSegment(stringResource(Res.string.ep_speciality_work), OrganicIcons.Briefcase),
                    OrganicSegment(stringResource(Res.string.ep_speciality_study), OrganicIcons.GraduationCap),
                ),
                selectedIndex = if (uiState.specialityKind == SpecialityKind.Study) 1 else 0,
                onSelect = { index ->
                    onEvent(EditProfileEvent.SpecialityKindSelected(if (index == 1) SpecialityKind.Study else SpecialityKind.Work))
                },
                enabled = enabled,
            )
            OrganicField(
                value = draft.speciality.orEmpty(),
                onValueChange = { onEvent(EditProfileEvent.SpecialityChanged(it)) },
                placeholder = stringResource(
                    if (uiState.specialityKind == SpecialityKind.Study) Res.string.ep_speciality_hint_study
                    else Res.string.ep_speciality_hint_work
                ),
                enabled = enabled,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences, imeAction = ImeAction.Next),
            )
            OrganicHelperText(stringResource(Res.string.ep_speciality_helper))
        }

        Field(label = stringResource(Res.string.ep_visibility_label)) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OrganicOptionCard(
                    title = stringResource(Res.string.ep_visibility_open),
                    body = stringResource(Res.string.ep_visibility_open_body),
                    icon = OrganicIcons.Globe,
                    selected = draft.visibility == ProfileVisibility.Open,
                    onClick = { onEvent(EditProfileEvent.VisibilitySelected(ProfileVisibility.Open)) },
                    enabled = enabled,
                )
                OrganicOptionCard(
                    title = stringResource(Res.string.ep_visibility_private),
                    body = stringResource(Res.string.ep_visibility_private_body),
                    icon = OrganicIcons.Friends,
                    selected = draft.visibility == ProfileVisibility.Private,
                    onClick = { onEvent(EditProfileEvent.VisibilitySelected(ProfileVisibility.Private)) },
                    enabled = enabled,
                )
            }
        }

        SectionIntro(
            tag = stringResource(Res.string.ep_private_tag),
            icon = OrganicIcons.Lock,
            tone = OrganicTagTone.Sage,
            body = stringResource(Res.string.ep_private_body),
            modifier = Modifier.padding(top = 8.dp),
        )

        Field(label = stringResource(Res.string.ep_email_label)) {
            EmailRow(email = draft.email.orEmpty(), verified = draft.emailVerified)
            OrganicHelperText(stringResource(Res.string.ep_email_soon))
        }

        Field(label = stringResource(Res.string.ep_phone_label)) {
            val country = Countries.byCode(uiState.phoneCountry)
            OrganicField(
                value = draft.phoneNumber.orEmpty(),
                onValueChange = { onEvent(EditProfileEvent.PhoneNumberChanged(it)) },
                placeholder = stringResource(Res.string.ep_phone_hint),
                enabled = enabled,
                startPadding = 8.dp,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next),
                leading = {
                    if (country != null) {
                        OrganicCountryCodeChip(
                            flag = country.flag,
                            dialCode = country.dialCode,
                            onClick = { onEvent(EditProfileEvent.PhoneCountryClicked) },
                            contentDescription = stringResource(Res.string.ep_phone_country, country.dialCode, country.name),
                            enabled = enabled,
                            modifier = Modifier.padding(end = 12.dp),
                        )
                    }
                },
            )
        }

        Field(label = stringResource(Res.string.ep_dob_label)) {
            OrganicPickerRow(
                value = draft.dateOfBirth?.let { birthDateText(it) },
                placeholder = stringResource(Res.string.ep_dob_hint),
                onClick = { onEvent(EditProfileEvent.DateOfBirthClicked) },
                icon = OrganicIcons.Calendar,
                state = if (uiState.dateOfBirthLocked) OrganicPickerRowState.Locked else OrganicPickerRowState.Editable,
                enabled = enabled,
            )
            OneTimeHelper(locked = uiState.dateOfBirthLocked)
        }

        Field(label = stringResource(Res.string.ep_gender_label)) {
            OrganicPickerRow(
                value = draft.gender?.let { genderText(it) },
                placeholder = stringResource(Res.string.ep_gender_hint),
                onClick = { onEvent(EditProfileEvent.GenderClicked) },
                icon = OrganicIcons.Profile,
                state = if (uiState.genderLocked) OrganicPickerRowState.Locked else OrganicPickerRowState.Editable,
                enabled = enabled,
            )
            OneTimeHelper(locked = uiState.genderLocked)
        }

        Field(label = stringResource(Res.string.ep_address_label)) {
            AddressCard(uiState = uiState, onEvent = onEvent, enabled = enabled)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
private fun UsernameField(uiState: EditProfileUiState, onEvent: (EditProfileEvent) -> Unit, enabled: Boolean) {
    val status = uiState.usernameStatus
    val showProblem = status is UsernameStatus.Taken || status is UsernameStatus.Invalid
    // The answer appears under the field, where the keyboard would cover it. When it arrives,
    // scroll the whole block — message and suggestions too — above the keyboard.
    val requester = remember { BringIntoViewRequester() }
    LaunchedEffect(status) {
        if (status is UsernameStatus.Taken || status is UsernameStatus.Invalid || status is UsernameStatus.Available) {
            requester.bringIntoView()
        }
    }
    Field(label = stringResource(Res.string.ep_username_label), modifier = Modifier.bringIntoViewRequester(requester)) {
        OrganicField(
            value = uiState.draft.username.orEmpty(),
            onValueChange = { onEvent(EditProfileEvent.UsernameChanged(it)) },
            placeholder = stringResource(Res.string.ep_username_hint),
            enabled = enabled,
            isError = showProblem,
            startPadding = 20.dp,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrectEnabled = false,
                keyboardType = KeyboardType.Ascii,
                imeAction = ImeAction.Next,
            ),
            leading = {
                Text(
                    text = "@",
                    modifier = Modifier.padding(end = 2.dp),
                    fontFamily = organicBodyFontFamily(),
                    fontSize = 15.sp,
                    color = OrganicColors.neutral700,
                )
            },
            trailing = {
                when (status) {
                    UsernameStatus.Checking -> OrganicFieldSpinner()
                    UsernameStatus.Available ->
                        Icon(OrganicIcons.Check, contentDescription = null, modifier = Modifier.size(18.dp), tint = OrganicColors.accent2_700)
                    is UsernameStatus.Taken, UsernameStatus.Invalid ->
                        Icon(OrganicIcons.Alert, contentDescription = null, modifier = Modifier.size(19.dp), tint = OrganicColors.accent700)
                    else -> Unit
                }
            },
        )
        when (status) {
            UsernameStatus.Checking -> OrganicHelperText(stringResource(Res.string.ep_username_checking))
            UsernameStatus.Available -> OrganicHelperText(
                stringResource(Res.string.ep_username_available),
                tone = OrganicHelperTone.Success,
                icon = OrganicIcons.Check,
            )
            is UsernameStatus.Taken -> {
                OrganicHelperText(
                    stringResource(if (status.suggestions.isEmpty()) Res.string.ep_username_taken_plain else Res.string.ep_username_taken),
                    tone = OrganicHelperTone.Warning,
                )
                if (status.suggestions.isNotEmpty()) {
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        status.suggestions.forEach { suggestion ->
                            OrganicSuggestionPill(
                                text = "@$suggestion",
                                onClick = { onEvent(EditProfileEvent.UsernameSuggestionPicked(suggestion)) },
                            )
                        }
                    }
                }
            }
            UsernameStatus.Invalid -> OrganicHelperText(stringResource(Res.string.ep_username_invalid), tone = OrganicHelperTone.Warning)
            UsernameStatus.Missing ->
                if (uiState.hasChanges) {
                    OrganicHelperText(stringResource(Res.string.ep_username_missing), tone = OrganicHelperTone.Warning)
                }
            else -> Unit
        }
        if (status !is UsernameStatus.Invalid) OrganicHelperText(stringResource(Res.string.ep_username_rule))
    }
}

@Composable
private fun EmailRow(email: String, verified: Boolean) {
    val body = organicBodyFontFamily()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(OrganicShape.pill))
            .background(OrganicColors.neutral100)
            .padding(start = 20.dp, end = 18.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(OrganicIcons.Mail, contentDescription = null, modifier = Modifier.size(18.dp), tint = OrganicColors.neutral700)
        Text(
            text = email,
            modifier = Modifier.weight(1f),
            fontFamily = body,
            fontSize = 15.sp,
            maxLines = 1,
            // A long address beside the tag and Change would otherwise stop at a word, unmarked.
            overflow = TextOverflow.Ellipsis,
            color = OrganicColors.text,
        )
        if (verified) {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(OrganicShape.pill))
                    .background(OrganicColors.accent2_200)
                    .padding(horizontal = 9.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(OrganicIcons.Check, contentDescription = null, modifier = Modifier.size(11.dp), tint = OrganicColors.accent2_800)
                Text(
                    text = stringResource(Res.string.ep_email_verified),
                    fontFamily = body,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = OrganicColors.accent2_800,
                )
            }
        }
        // Drawn but switched off: changing an address needs a code flow the server does not have yet.
        Text(
            text = stringResource(Res.string.ep_email_change),
            modifier = Modifier.alpha(0.45f),
            fontFamily = body,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = OrganicColors.accent700,
        )
    }
}

@Composable
private fun AddressCard(uiState: EditProfileUiState, onEvent: (EditProfileEvent) -> Unit, enabled: Boolean) {
    val address = uiState.draft.address
    val country = Countries.byCode(address.countryCode)
    OrganicCard(
        modifier = Modifier.fillMaxWidth().alpha(if (enabled) 1f else 0.5f),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 18.dp, vertical = 6.dp),
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = enabled, role = Role.Button) { onEvent(EditProfileEvent.AddressCountryClicked) }
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    AddressLabel(stringResource(Res.string.ep_address_country))
                    Text(
                        text = country?.let { "${it.flag}  ${it.name}" } ?: stringResource(Res.string.ep_address_country_hint),
                        fontFamily = organicBodyFontFamily(),
                        fontSize = 15.sp,
                        color = if (country == null) OrganicColors.neutral700 else OrganicColors.text,
                    )
                }
                Icon(OrganicIcons.ChevronRight, contentDescription = null, modifier = Modifier.size(15.dp), tint = OrganicColors.neutral600)
            }
            AddressLine(
                label = stringResource(Res.string.ep_address_city),
                value = address.city.orEmpty(),
                placeholder = stringResource(Res.string.ep_address_city_hint),
                onChange = { onEvent(EditProfileEvent.CityChanged(it)) },
                enabled = enabled,
                capitalization = KeyboardCapitalization.Words,
            )
            AddressLine(
                label = stringResource(Res.string.ep_address_street),
                value = address.street.orEmpty(),
                placeholder = stringResource(Res.string.ep_address_street_hint),
                onChange = { onEvent(EditProfileEvent.StreetChanged(it)) },
                enabled = enabled,
                capitalization = KeyboardCapitalization.Words,
            )
            AddressLine(
                label = stringResource(Res.string.ep_address_postal),
                value = address.postalCode.orEmpty(),
                placeholder = stringResource(Res.string.ep_address_postal_hint),
                onChange = { onEvent(EditProfileEvent.PostalCodeChanged(it)) },
                enabled = enabled,
                capitalization = KeyboardCapitalization.Characters,
                imeAction = ImeAction.Done,
            )
        }
    }
}

/** One line of the address card: a small label over borderless text, as the design draws it. */
@Composable
private fun AddressLine(
    label: String,
    value: String,
    placeholder: String,
    onChange: (String) -> Unit,
    enabled: Boolean,
    capitalization: KeyboardCapitalization,
    imeAction: ImeAction = ImeAction.Next,
) {
    val font = organicBodyFontFamily()
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
        AddressLabel(label)
        BasicTextField(
            value = value,
            onValueChange = onChange,
            enabled = enabled,
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(fontFamily = font, fontSize = 15.sp, color = OrganicColors.text),
            cursorBrush = SolidColor(OrganicColors.accent),
            keyboardOptions = KeyboardOptions(capitalization = capitalization, imeAction = imeAction),
            decorationBox = { inner ->
                Box {
                    if (value.isEmpty()) {
                        Text(text = placeholder, fontFamily = font, fontSize = 15.sp, color = OrganicColors.neutral700)
                    }
                    inner()
                }
            },
        )
    }
}

@Composable
private fun AddressLabel(text: String) {
    Text(
        text = text.uppercase(),
        fontFamily = organicBodyFontFamily(),
        fontWeight = FontWeight.Bold,
        fontSize = 10.sp,
        letterSpacing = 0.8.sp,
        color = OrganicColors.neutral700,
    )
}

@Composable
private fun OneTimeHelper(locked: Boolean) {
    if (locked) {
        OrganicHelperText(stringResource(Res.string.ep_once_locked))
    } else {
        OrganicHelperText(stringResource(Res.string.ep_once_warning), tone = OrganicHelperTone.Warning)
    }
}

@Composable
private fun SectionIntro(
    tag: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tone: OrganicTagTone,
    body: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OrganicSectionTag(text = tag, icon = icon, tone = tone)
        Text(text = body, fontFamily = organicBodyFontFamily(), fontSize = 13.sp, lineHeight = 18.sp, color = OrganicColors.neutral700)
    }
}

@Composable
private fun Field(label: String, modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OrganicSectionLabel(text = label)
        content()
    }
}

@Composable
private fun SaveErrorBanner(message: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(OrganicShape.radiusMd))
            .background(OrganicColors.accent100)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(OrganicIcons.Alert, contentDescription = null, modifier = Modifier.size(18.dp), tint = OrganicColors.accent700)
        Text(
            text = message,
            fontFamily = organicBodyFontFamily(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            lineHeight = 18.sp,
            color = OrganicColors.accent800,
        )
    }
}
