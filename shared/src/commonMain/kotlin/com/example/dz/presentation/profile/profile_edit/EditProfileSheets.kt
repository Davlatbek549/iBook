package com.example.dz.presentation.profile.profile_edit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.icons.OrganicIcons
import com.example.dz.designsystem.components.organic.OrganicBottomSheet
import com.example.dz.designsystem.components.organic.OrganicButton
import com.example.dz.designsystem.components.organic.OrganicChoiceRow
import com.example.dz.designsystem.components.organic.OrganicConfirmDialog
import com.example.dz.designsystem.components.organic.OrganicField
import com.example.dz.designsystem.components.organic.OrganicHelperText
import com.example.dz.designsystem.components.organic.OrganicHelperTone
import com.example.dz.designsystem.components.organic.OrganicSecondaryButton
import com.example.dz.designsystem.components.organic.OrganicSheetAction
import com.example.dz.designsystem.components.organic.OrganicWheelDefaults
import com.example.dz.designsystem.components.organic.OrganicWheelPicker
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.OrganicShape
import com.example.dz.designsystem.theme.organicBodyFontFamily
import com.example.dz.domain.model.BirthDate
import com.example.dz.domain.model.Countries
import com.example.dz.domain.model.Gender
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.ep_and
import dz.shared.generated.resources.ep_choose_photo
import dz.shared.generated.resources.ep_confirm_body_both
import dz.shared.generated.resources.ep_confirm_body_one
import dz.shared.generated.resources.ep_confirm_both_title
import dz.shared.generated.resources.ep_confirm_dob_title
import dz.shared.generated.resources.ep_confirm_gender_title
import dz.shared.generated.resources.ep_country_none
import dz.shared.generated.resources.ep_country_search
import dz.shared.generated.resources.ep_address_country
import dz.shared.generated.resources.ep_discard
import dz.shared.generated.resources.ep_discard_body
import dz.shared.generated.resources.ep_discard_title
import dz.shared.generated.resources.ep_dob_label
import dz.shared.generated.resources.ep_dob_sheet_body
import dz.shared.generated.resources.ep_done
import dz.shared.generated.resources.ep_field_address
import dz.shared.generated.resources.ep_field_banner
import dz.shared.generated.resources.ep_field_bio
import dz.shared.generated.resources.ep_field_dob
import dz.shared.generated.resources.ep_field_gender
import dz.shared.generated.resources.ep_field_name
import dz.shared.generated.resources.ep_field_phone
import dz.shared.generated.resources.ep_field_photo
import dz.shared.generated.resources.ep_field_speciality
import dz.shared.generated.resources.ep_field_username
import dz.shared.generated.resources.ep_field_visibility
import dz.shared.generated.resources.ep_gender_female
import dz.shared.generated.resources.ep_gender_label
import dz.shared.generated.resources.ep_gender_male
import dz.shared.generated.resources.ep_gender_none
import dz.shared.generated.resources.ep_go_back
import dz.shared.generated.resources.ep_keep_editing
import dz.shared.generated.resources.ep_once_warning
import dz.shared.generated.resources.ep_photos_soon
import dz.shared.generated.resources.ep_remove_photo
import dz.shared.generated.resources.ep_save
import dz.shared.generated.resources.ep_cancel
import dz.shared.generated.resources.ep_sheet_banner
import dz.shared.generated.resources.ep_sheet_profile_photo
import dz.shared.generated.resources.ep_take_photo
import dz.shared.generated.resources.month_1
import dz.shared.generated.resources.month_10
import dz.shared.generated.resources.month_11
import dz.shared.generated.resources.month_12
import dz.shared.generated.resources.month_2
import dz.shared.generated.resources.month_3
import dz.shared.generated.resources.month_4
import dz.shared.generated.resources.month_5
import dz.shared.generated.resources.month_6
import dz.shared.generated.resources.month_7
import dz.shared.generated.resources.month_8
import dz.shared.generated.resources.month_9
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

private val MONTHS: List<StringResource> = listOf(
    Res.string.month_1, Res.string.month_2, Res.string.month_3, Res.string.month_4,
    Res.string.month_5, Res.string.month_6, Res.string.month_7, Res.string.month_8,
    Res.string.month_9, Res.string.month_10, Res.string.month_11, Res.string.month_12,
)

/** "14 March 1999". */
@Composable
internal fun birthDateText(date: BirthDate): String = "${date.day} ${stringResource(MONTHS[date.month - 1])} ${date.year}"

@Composable
internal fun genderText(gender: Gender): String = stringResource(
    when (gender) {
        Gender.Female -> Res.string.ep_gender_female
        Gender.Male -> Res.string.ep_gender_male
        Gender.PreferNotToSay -> Res.string.ep_gender_none
    }
)

@Composable
internal fun EditProfileSheets(uiState: EditProfileUiState, onEvent: (EditProfileEvent) -> Unit) {
    val dismiss = { onEvent(EditProfileEvent.SheetDismissed) }
    when (val sheet = uiState.sheet) {
        null -> Unit
        is EditProfileSheet.Photo -> PhotoSheet(
            target = sheet.target,
            hasPhoto = when (sheet.target) {
                PhotoTarget.Banner -> uiState.draft.bannerUrl != null
                PhotoTarget.Profile -> uiState.draft.avatarUrl != null
            },
            onRemove = { onEvent(EditProfileEvent.RemovePhotoClicked) },
            onDismiss = dismiss,
        )
        EditProfileSheet.DateOfBirth -> DateOfBirthSheet(
            initial = uiState.draft.dateOfBirth ?: BirthDate(uiState.today.year - 25, 1, 1),
            today = uiState.today,
            onDone = { onEvent(EditProfileEvent.DateOfBirthPicked(it)) },
            onDismiss = dismiss,
        )
        EditProfileSheet.Gender -> GenderSheet(
            initial = uiState.draft.gender,
            onDone = { onEvent(EditProfileEvent.GenderPicked(it)) },
            onDismiss = dismiss,
        )
        is EditProfileSheet.Country -> CountrySheet(
            selectedCode = when (sheet.target) {
                CountryTarget.Phone -> uiState.phoneCountry
                CountryTarget.Address -> uiState.draft.address.countryCode
            },
            showDialCodes = sheet.target == CountryTarget.Phone,
            onPick = { onEvent(EditProfileEvent.CountryPicked(it)) },
            onDismiss = dismiss,
        )
    }
}

/**
 * Taking and choosing photos are drawn but switched off: there is nowhere to store an uploaded
 * image yet, and a photo that vanished on Save would be worse than none. Removing one works.
 */
@Composable
private fun PhotoSheet(target: PhotoTarget, hasPhoto: Boolean, onRemove: () -> Unit, onDismiss: () -> Unit) {
    OrganicBottomSheet(
        title = stringResource(if (target == PhotoTarget.Banner) Res.string.ep_sheet_banner else Res.string.ep_sheet_profile_photo),
        onDismiss = onDismiss,
    ) {
        Column {
            OrganicSheetAction(text = stringResource(Res.string.ep_take_photo), icon = OrganicIcons.Camera, onClick = {}, enabled = false)
            OrganicSheetAction(text = stringResource(Res.string.ep_choose_photo), icon = OrganicIcons.Image, onClick = {}, enabled = false)
            OrganicHelperText(stringResource(Res.string.ep_photos_soon), modifier = Modifier.padding(top = 4.dp, bottom = 6.dp))
            if (hasPhoto) {
                OrganicSheetAction(
                    text = stringResource(Res.string.ep_remove_photo),
                    icon = OrganicIcons.Trash,
                    onClick = onRemove,
                    destructive = true,
                )
            }
        }
        OrganicSecondaryButton(
            text = stringResource(Res.string.ep_cancel),
            onClick = onDismiss,
            modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
        )
    }
}

/**
 * Day · month · year wheels over one selection band. The picked date is held here until Done;
 * a day the new month does not have is pulled back to its last day.
 */
@Composable
private fun DateOfBirthSheet(initial: BirthDate, today: BirthDate, onDone: (BirthDate) -> Unit, onDismiss: () -> Unit) {
    val years = remember(today) { (EARLIEST_YEAR..today.year).toList() }
    var year by remember { mutableStateOf(initial.year.coerceIn(EARLIEST_YEAR, today.year)) }
    var month by remember { mutableStateOf(initial.month) }
    var day by remember { mutableStateOf(initial.day) }
    val daysInMonth = BirthDate.daysIn(year, month)
    // Read through this rather than [day]: the 31st becomes the 30th when the month is shortened.
    val safeDay = day.coerceAtMost(daysInMonth)
    val monthNames = MONTHS.map { stringResource(it) }

    OrganicBottomSheet(
        title = stringResource(Res.string.ep_dob_label),
        subtitle = stringResource(Res.string.ep_dob_sheet_body),
        onDismiss = onDismiss,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(OrganicShape.radiusLg))
                .background(OrganicColors.neutral100)
                .padding(horizontal = 8.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(OrganicWheelDefaults.ItemHeight)
                    .clip(RoundedCornerShape(OrganicShape.pill))
                    .background(OrganicColors.neutral200),
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                OrganicWheelPicker(
                    items = (1..daysInMonth).map { it.toString() },
                    selectedIndex = safeDay - 1,
                    onSelectedIndexChange = { day = it + 1 },
                    modifier = Modifier.weight(0.8f),
                )
                OrganicWheelPicker(
                    items = monthNames,
                    selectedIndex = month - 1,
                    onSelectedIndexChange = { month = it + 1 },
                    modifier = Modifier.weight(1.6f),
                    alignment = Alignment.CenterStart,
                )
                OrganicWheelPicker(
                    items = years.map { it.toString() },
                    selectedIndex = years.indexOf(year).coerceAtLeast(0),
                    onSelectedIndexChange = { year = years[it] },
                    modifier = Modifier.weight(1f),
                )
            }
        }
        OrganicHelperText(stringResource(Res.string.ep_once_warning), tone = OrganicHelperTone.Warning, icon = OrganicIcons.Lock)
        OrganicButton(
            text = stringResource(Res.string.ep_done),
            onClick = { onDone(BirthDate(year, month, safeDay)) },
        )
    }
}

@Composable
private fun GenderSheet(initial: Gender?, onDone: (Gender) -> Unit, onDismiss: () -> Unit) {
    var picked by remember { mutableStateOf(initial) }
    OrganicBottomSheet(title = stringResource(Res.string.ep_gender_label), onDismiss = onDismiss) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Gender.entries.forEach { gender ->
                OrganicChoiceRow(text = genderText(gender), selected = picked == gender, onClick = { picked = gender })
            }
        }
        OrganicHelperText(stringResource(Res.string.ep_once_warning), tone = OrganicHelperTone.Warning, icon = OrganicIcons.Lock)
        OrganicButton(
            text = stringResource(Res.string.ep_done),
            onClick = { picked?.let(onDone) },
            enabled = picked != null,
        )
    }
}

/** A searchable list of every country. For a phone number each row also shows its dial code. */
@Composable
private fun CountrySheet(selectedCode: String?, showDialCodes: Boolean, onPick: (String) -> Unit, onDismiss: () -> Unit) {
    var query by remember { mutableStateOf("") }
    val matches = remember(query) { Countries.search(query) }
    val font = organicBodyFontFamily()

    OrganicBottomSheet(title = stringResource(Res.string.ep_address_country), onDismiss = onDismiss) {
        OrganicField(
            value = query,
            onValueChange = { query = it },
            placeholder = stringResource(Res.string.ep_country_search),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            leading = {
                Icon(
                    OrganicIcons.Search,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 10.dp).size(18.dp),
                    tint = OrganicColors.neutral700,
                )
            },
            startPadding = 18.dp,
        )
        if (matches.isEmpty()) {
            Text(
                text = stringResource(Res.string.ep_country_none),
                modifier = Modifier.padding(vertical = 18.dp),
                fontFamily = font,
                fontSize = 14.sp,
                color = OrganicColors.neutral700,
            )
        }
        LazyColumn(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.62f)) {
            items(matches, key = { it.code }) { country ->
                val selected = country.code == selectedCode
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(OrganicShape.radiusMd))
                        .background(if (selected) OrganicColors.accent100 else OrganicColors.bg)
                        .clickable(role = Role.Button) { onPick(country.code) }
                        .padding(horizontal = 12.dp, vertical = 13.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = country.flag, fontSize = 18.sp)
                    Text(
                        text = country.name,
                        modifier = Modifier.weight(1f),
                        fontFamily = font,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 15.sp,
                        color = OrganicColors.text,
                    )
                    if (showDialCodes) {
                        Text(text = country.dialCode, fontFamily = font, fontSize = 14.sp, color = OrganicColors.neutral700)
                    }
                    if (selected) {
                        Icon(OrganicIcons.Check, contentDescription = null, modifier = Modifier.size(16.dp), tint = OrganicColors.accent700)
                    }
                }
            }
        }
    }
}

@Composable
internal fun EditProfileDialogs(uiState: EditProfileUiState, onEvent: (EditProfileEvent) -> Unit) {
    when (val dialog = uiState.dialog) {
        null -> Unit
        is EditProfileDialog.ConfirmOneTime -> ConfirmOneTimeDialog(
            pending = dialog.pending,
            isSaving = uiState.isSaving,
            onConfirm = { onEvent(EditProfileEvent.OneTimeConfirmed) },
            onDismiss = { onEvent(EditProfileEvent.DialogDismissed) },
        )
        EditProfileDialog.Discard -> OrganicConfirmDialog(
            icon = OrganicIcons.Alert,
            title = stringResource(Res.string.ep_discard_title),
            body = AnnotatedString(stringResource(Res.string.ep_discard_body, changedFieldsText(uiState.changedFields))),
            dismissText = stringResource(Res.string.ep_keep_editing),
            confirmText = stringResource(Res.string.ep_discard),
            onDismiss = { onEvent(EditProfileEvent.DialogDismissed) },
            onConfirm = { onEvent(EditProfileEvent.DiscardConfirmed) },
            confirmColor = OrganicColors.accent800,
        )
    }
}

/** Repeats the exact values, in bold, so they can be checked before they are locked. */
@Composable
private fun ConfirmOneTimeDialog(pending: PendingOneTime, isSaving: Boolean, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    val dateText = pending.dateOfBirth?.let { birthDateText(it) }
    val genderText = pending.gender?.let { genderText(it) }
    val values = listOfNotNull(dateText, genderText)
    val title = stringResource(
        when {
            dateText != null && genderText != null -> Res.string.ep_confirm_both_title
            dateText != null -> Res.string.ep_confirm_dob_title
            else -> Res.string.ep_confirm_gender_title
        }
    )
    val sentence = if (values.size == 2) {
        stringResource(Res.string.ep_confirm_body_both, values[0], values[1])
    } else {
        stringResource(Res.string.ep_confirm_body_one, values.firstOrNull().orEmpty())
    }
    OrganicConfirmDialog(
        icon = if (dateText != null) OrganicIcons.Calendar else OrganicIcons.Profile,
        title = title,
        body = boldValues(sentence, values),
        dismissText = stringResource(Res.string.ep_go_back),
        confirmText = stringResource(Res.string.ep_save),
        onDismiss = onDismiss,
        onConfirm = onConfirm,
        isBusy = isSaving,
    )
}

private fun boldValues(sentence: String, values: List<String>): AnnotatedString = buildAnnotatedString {
    append(sentence)
    values.forEach { value ->
        val start = sentence.indexOf(value)
        if (start >= 0) addStyle(SpanStyle(fontWeight = FontWeight.Bold), start, start + value.length)
    }
}

/** "bio", "bio and banner", "name, bio and banner". */
@Composable
private fun changedFieldsText(fields: List<ProfileField>): String {
    val names = fields.map { stringResource(it.label()) }
    val and = stringResource(Res.string.ep_and)
    return when (names.size) {
        0 -> ""
        1 -> names[0]
        else -> names.dropLast(1).joinToString(", ") + " $and " + names.last()
    }
}

private fun ProfileField.label(): StringResource = when (this) {
    ProfileField.Photo -> Res.string.ep_field_photo
    ProfileField.Banner -> Res.string.ep_field_banner
    ProfileField.Name -> Res.string.ep_field_name
    ProfileField.Username -> Res.string.ep_field_username
    ProfileField.Bio -> Res.string.ep_field_bio
    ProfileField.Speciality -> Res.string.ep_field_speciality
    ProfileField.Visibility -> Res.string.ep_field_visibility
    ProfileField.Phone -> Res.string.ep_field_phone
    ProfileField.DateOfBirth -> Res.string.ep_field_dob
    ProfileField.Gender -> Res.string.ep_field_gender
    ProfileField.Address -> Res.string.ep_field_address
}

private const val EARLIEST_YEAR = 1900
