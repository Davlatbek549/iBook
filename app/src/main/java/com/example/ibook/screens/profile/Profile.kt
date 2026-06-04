package com.example.ibook.screens.profile

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ibook.R
import com.example.ibook.ui.theme.IBookTheme

data class ProfileUiState(
    @param:DrawableRes val profileImageRes: Int = R.drawable.profile_1,
    val username: String = "Baodesigner",
    val birthday: String = "18 / 08 / 1996",
    val address: String = "7B Ut Tich, Phuong 4, Tan Binh...",
    val phone: String = "+84355869631"
)

private data class ProfileMetrics(
    val horizontalPadding: Dp,
    val topSpacing: Dp,
    val backButtonSize: Dp,
    val backIconSize: Dp,
    val titleTopSpacing: Dp,
    val titleSize: TextUnit,
    val avatarTopSpacing: Dp,
    val avatarSize: Dp,
    val cameraButtonSize: Dp,
    val cameraIconSize: Dp,
    val firstFieldTopSpacing: Dp,
    val rowHeight: Dp,
    val rowCorner: Dp,
    val rowHorizontalPadding: Dp,
    val rowIconSize: Dp,
    val rowIconBoxSize: Dp,
    val rowIconColumnWidth: Dp,
    val rowTextSize: TextUnit,
    val rowTextLineHeight: TextUnit,
    val rowSpacing: Dp,
    val chevronSize: Dp,
    val saveTopSpacing: Dp,
    val saveButtonHeight: Dp,
    val saveButtonCorner: Dp,
    val saveTextSize: TextUnit,
    val bottomSpacing: Dp
)

@Composable
fun Profile(
    modifier: Modifier = Modifier,
    profile: ProfileUiState = ProfileUiState(),
    onBackClick: () -> Unit = {},
    onPhotoClick: () -> Unit = {},
    onUsernameClick: () -> Unit = {},
    onBirthdayClick: () -> Unit = {},
    onAddressClick: () -> Unit = {},
    onPhoneClick: () -> Unit = {},
    onSaveClick: () -> Unit = {}
) {
    ProfileScreen(
        modifier = modifier,
        profile = profile,
        onBackClick = onBackClick,
        onPhotoClick = onPhotoClick,
        onUsernameClick = onUsernameClick,
        onBirthdayClick = onBirthdayClick,
        onAddressClick = onAddressClick,
        onPhoneClick = onPhoneClick,
        onSaveClick = onSaveClick
    )
}

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    profile: ProfileUiState = ProfileUiState(),
    onBackClick: () -> Unit = {},
    onPhotoClick: () -> Unit = {},
    onUsernameClick: () -> Unit = {},
    onBirthdayClick: () -> Unit = {},
    onAddressClick: () -> Unit = {},
    onPhoneClick: () -> Unit = {},
    onSaveClick: () -> Unit = {}
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val metrics = rememberProfileMetrics(maxWidth = maxWidth, maxHeight = maxHeight)
        val colorScheme = MaterialTheme.colorScheme

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = metrics.horizontalPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(metrics.topSpacing))

            ProfileBackButton(
                metrics = metrics,
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(metrics.titleTopSpacing))

            Text(
                text = stringResource(R.string.profile_edit_title),
                color = colorScheme.onSurface.copy(alpha = 0.76f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = metrics.titleSize,
                    lineHeight = metrics.titleSize * 1.08f,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.sp
                )
            )

            Spacer(modifier = Modifier.height(metrics.avatarTopSpacing))

            ProfileAvatar(
                imageRes = profile.profileImageRes,
                metrics = metrics,
                onPhotoClick = onPhotoClick
            )

            Spacer(modifier = Modifier.height(metrics.firstFieldTopSpacing))

            ProfileNameField(
                value = profile.username,
                metrics = metrics,
                onClick = onUsernameClick
            )

            Spacer(modifier = Modifier.height(metrics.rowSpacing))

            ProfileField(
                value = profile.birthday,
                icon = painterResource(R.drawable.ic_calendar),
                showChevron = true,
                metrics = metrics,
                onClick = onBirthdayClick
            )

            Spacer(modifier = Modifier.height(metrics.rowSpacing))

            ProfileField(
                value = profile.address,
                icon = painterResource(R.drawable.ic_map),
                metrics = metrics,
                onClick = onAddressClick
            )

            Spacer(modifier = Modifier.height(metrics.rowSpacing))

            ProfileField(
                value = profile.phone,
                icon = painterResource(R.drawable.ic_phone),
                metrics = metrics,
                onClick = onPhoneClick
            )

            Spacer(modifier = Modifier.height(metrics.saveTopSpacing))

            ProfileSaveButton(
                metrics = metrics,
                onClick = onSaveClick,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(metrics.bottomSpacing))
        }
    }
}

@Composable
private fun ProfileBackButton(
    metrics: ProfileMetrics,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(metrics.backButtonSize)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.16f))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_back),
            contentDescription = stringResource(R.string.cd_back),
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(metrics.backIconSize)
        )
    }
}

@Composable
private fun ProfileAvatar(
    @DrawableRes imageRes: Int,
    metrics: ProfileMetrics,
    onPhotoClick: () -> Unit
) {
    Box(
        modifier = Modifier.size(metrics.avatarSize)
    ) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = stringResource(R.string.cd_profile_photo),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(metrics.cameraButtonSize)
                .clip(RoundedCornerShape(metrics.cameraButtonSize * 0.36f))
                .background(MaterialTheme.colorScheme.surface)
                .border(
                    width = 1.5.dp,
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(metrics.cameraButtonSize * 0.36f)
                )
                .clickable(onClick = onPhotoClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_camera),
                contentDescription = stringResource(R.string.cd_change_profile_photo),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(metrics.cameraIconSize)
            )
        }
    }
}

@Composable
private fun ProfileNameField(
    value: String,
    metrics: ProfileMetrics,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(metrics.rowHeight)
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(metrics.rowIconBoxSize)
                .clip(RoundedCornerShape(metrics.rowCorner))
                .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.08f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_profile),
                contentDescription = stringResource(R.string.cd_username),
                tint = Color.Unspecified,
                modifier = Modifier.size(metrics.rowIconSize)
            )
        }

        Spacer(modifier = Modifier.width(metrics.rowHorizontalPadding))

        Text(
            text = value,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = metrics.rowTextSize,
                lineHeight = metrics.rowTextLineHeight,
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.sp
            )
        )
    }
}

@Composable
private fun ProfileField(
    value: String,
    icon: Painter,
    metrics: ProfileMetrics,
    onClick: () -> Unit,
    showChevron: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(metrics.rowHeight)
            .clip(RoundedCornerShape(metrics.rowCorner))
            .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.08f))
            .clickable(onClick = onClick)
            .padding(horizontal = metrics.rowHorizontalPadding),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.width(metrics.rowIconColumnWidth),
            contentAlignment = Alignment.CenterStart
        ) {
            Icon(
                painter = icon,
                contentDescription = value,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(metrics.rowIconSize)
            )
        }

        Text(
            text = value,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = metrics.rowTextSize,
                lineHeight = metrics.rowTextLineHeight,
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.sp
            )
        )

        if (showChevron) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = stringResource(R.string.cd_open),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f),
                modifier = Modifier.size(metrics.chevronSize)
            )
        }
    }
}

@Composable
private fun ProfileSaveButton(
    metrics: ProfileMetrics,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(metrics.saveButtonHeight)
            .clip(RoundedCornerShape(metrics.saveButtonCorner))
            .background(MaterialTheme.colorScheme.primary)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.profile_save),
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = metrics.saveTextSize,
                lineHeight = metrics.saveTextSize * 1.2f,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp
            )
        )
    }
}

@Composable
private fun rememberProfileMetrics(
    maxWidth: Dp,
    maxHeight: Dp
): ProfileMetrics {
    return remember(maxWidth, maxHeight) {
        val horizontalPadding = (maxWidth * 0.075f).coerceIn(28.dp, 38.dp)
        val rowTextSize = (maxWidth.value * 0.037f).coerceIn(15f, 18f).sp
        val titleSize = (maxWidth.value * 0.093f).coerceIn(36f, 44f).sp
        val backButtonSize = (maxWidth * 0.112f).coerceIn(48.dp, 58.dp)
        val rowHeight = (maxHeight * 0.077f).coerceIn(68.dp, 82.dp)

        ProfileMetrics(
            horizontalPadding = horizontalPadding,
            topSpacing = (maxHeight * 0.06f).coerceIn(42.dp, 58.dp),
            backButtonSize = backButtonSize,
            backIconSize = backButtonSize * 0.34f,
            titleTopSpacing = (maxHeight * 0.035f).coerceIn(28.dp, 42.dp),
            titleSize = titleSize,
            avatarTopSpacing = (maxHeight * 0.048f).coerceIn(34.dp, 54.dp),
            avatarSize = (maxWidth * 0.36f).coerceIn(132.dp, 166.dp),
            cameraButtonSize = (maxWidth * 0.063f).coerceIn(26.dp, 34.dp),
            cameraIconSize = (maxWidth * 0.038f).coerceIn(16.dp, 21.dp),
            firstFieldTopSpacing = (maxHeight * 0.05f).coerceIn(38.dp, 58.dp),
            rowHeight = rowHeight,
            rowCorner = (rowHeight * 0.36f).coerceIn(24.dp, 32.dp),
            rowHorizontalPadding = (maxWidth * 0.058f).coerceIn(20.dp, 28.dp),
            rowIconSize = (maxWidth * 0.058f).coerceIn(24.dp, 31.dp),
            rowIconBoxSize = rowHeight,
            rowIconColumnWidth = (maxWidth * 0.125f).coerceIn(48.dp, 64.dp),
            rowTextSize = rowTextSize,
            rowTextLineHeight = rowTextSize * 1.25f,
            rowSpacing = (maxHeight * 0.028f).coerceIn(22.dp, 30.dp),
            chevronSize = (maxWidth * 0.065f).coerceIn(26.dp, 34.dp),
            saveTopSpacing = (maxHeight * 0.13f).coerceIn(82.dp, 140.dp),
            saveButtonHeight = (maxHeight * 0.082f).coerceIn(68.dp, 84.dp),
            saveButtonCorner = (maxWidth * 0.052f).coerceIn(22.dp, 28.dp),
            saveTextSize = (maxWidth.value * 0.042f).coerceIn(17f, 20f).sp,
            bottomSpacing = (maxHeight * 0.028f).coerceIn(22.dp, 34.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    IBookTheme {
        ProfileScreen()
    }
}
