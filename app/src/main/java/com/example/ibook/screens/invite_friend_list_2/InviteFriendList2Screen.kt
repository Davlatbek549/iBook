package com.example.ibook.screens.invite_friend_list_2

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Message
import androidx.compose.material.icons.filled.PersonAddAlt1
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
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
import androidx.compose.ui.zIndex
import com.example.ibook.R
import com.example.ibook.ui.theme.IBookTheme
import kotlin.math.min

private data class InviteFriend(
    val imageRes: Int,
    val nameRes: Int
)

private data class InviteFriendsMetrics(
    val horizontalPadding: Dp,
    val topSpacing: Dp,
    val actionButtonSize: Dp,
    val actionIconSize: Dp,
    val headerToSceneSpacing: Dp,
    val sceneHeight: Dp,
    val sideAvatarSize: Dp,
    val centerAvatarSize: Dp,
    val ringSize: Dp,
    val titleSpacing: Dp,
    val titleFontSize: TextUnit,
    val titleLineHeight: TextUnit,
    val listTopSpacing: Dp,
    val rowHeight: Dp,
    val rowAvatarSize: Dp,
    val rowNameFontSize: TextUnit,
    val waveFontSize: TextUnit,
    val buttonTopSpacing: Dp,
    val buttonHeight: Dp,
    val buttonCornerRadius: Dp,
    val buttonIconSize: Dp,
    val buttonFontSize: TextUnit,
    val bottomSpacing: Dp
)

private val inviteFriends = listOf(
    InviteFriend(R.drawable.profile_1, R.string.friend_name_mona),
    InviteFriend(R.drawable.profile_7, R.string.friend_name_mark),
    InviteFriend(R.drawable.profile_8, R.string.friend_name_patricia),
    InviteFriend(R.drawable.profile_3, R.string.friend_name_mary),
    InviteFriend(R.drawable.profile_10, R.string.friend_name_linda)
)

@Composable
fun InviteFriendList2Screen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onMessageClick: () -> Unit = {},
    onDiscoverPeopleClick: () -> Unit = {}
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val metrics = rememberInviteFriendsMetrics(maxWidth = maxWidth, maxHeight = maxHeight)

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .padding(horizontal = metrics.horizontalPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(metrics.topSpacing))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    InviteCircleButton(
                        onClick = onBackClick,
                        buttonSize = metrics.actionButtonSize,
                        iconSize = metrics.actionIconSize
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_back),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    InviteCircleButton(
                        onClick = onMessageClick,
                        buttonSize = metrics.actionButtonSize,
                        iconSize = metrics.actionIconSize
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.Message,
                            contentDescription = stringResource(R.string.cd_message),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(metrics.headerToSceneSpacing))

                    InviteHeroScene(
                        metrics = metrics,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(metrics.sceneHeight)
                    )

                    Spacer(modifier = Modifier.height(metrics.titleSpacing))

                    Text(
                        text = stringResource(R.string.invite_friend_list_2_title),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontSize = metrics.titleFontSize,
                            lineHeight = metrics.titleLineHeight,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Spacer(modifier = Modifier.height(metrics.listTopSpacing))

                    inviteFriends.forEach { friend ->
                        InviteFriendRow(
                            friend = friend,
                            metrics = metrics
                        )
                    }
                }

                Spacer(modifier = Modifier.height(metrics.buttonTopSpacing))

                DiscoverPeopleButton(
                    onClick = onDiscoverPeopleClick,
                    metrics = metrics
                )

                Spacer(modifier = Modifier.height(metrics.bottomSpacing))
            }
        }
    }
}

@Composable
private fun InviteHeroScene(
    metrics: InviteFriendsMetrics,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        InviteProfileCircle(
            imageResId = R.drawable.profile_5,
            size = metrics.sideAvatarSize,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(x = metrics.sideAvatarSize * 0.16f)
        )

        InviteProfileCircle(
            imageResId = R.drawable.profile_4,
            size = metrics.sideAvatarSize,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset(x = -metrics.sideAvatarSize * 0.16f)
        )

        BrokenGradientRing(
            modifier = Modifier
                .size(metrics.ringSize)
                .zIndex(1f)
        )

        InviteProfileCircle(
            imageResId = R.drawable.profile_12,
            size = metrics.centerAvatarSize,
            modifier = Modifier.zIndex(2f)
        )
    }
}

@Composable
private fun InviteFriendRow(
    friend: InviteFriend,
    metrics: InviteFriendsMetrics,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(metrics.rowHeight),
        verticalAlignment = Alignment.CenterVertically
    ) {
        InviteProfileCircle(
            imageResId = friend.imageRes,
            size = metrics.rowAvatarSize
        )

        Spacer(modifier = Modifier.width(metrics.rowAvatarSize * 0.64f))

        Text(
            text = stringResource(friend.nameRes),
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = metrics.rowNameFontSize,
                lineHeight = metrics.rowNameFontSize * 1.25f,
                fontWeight = FontWeight.Bold
            )
        )

        Text(
            text = "👋",
            fontSize = metrics.waveFontSize,
            lineHeight = metrics.waveFontSize,
            modifier = Modifier.padding(start = 14.dp)
        )
    }
}

@Composable
private fun DiscoverPeopleButton(
    onClick: () -> Unit,
    metrics: InviteFriendsMetrics,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(metrics.buttonHeight)
            .clip(RoundedCornerShape(metrics.buttonCornerRadius))
            .background(MaterialTheme.colorScheme.primary)
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.PersonAddAlt1,
            contentDescription = stringResource(R.string.cd_discover_people),
            tint = Color.White,
            modifier = Modifier.size(metrics.buttonIconSize)
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = stringResource(R.string.discover_people),
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = metrics.buttonFontSize,
                lineHeight = metrics.buttonFontSize * 1.25f,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
private fun InviteCircleButton(
    onClick: () -> Unit,
    buttonSize: Dp,
    iconSize: Dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .size(buttonSize)
            .clip(CircleShape)
            .background(Color(0xFFE5E5E5))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f),
                shape = CircleShape
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.size(iconSize),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}

@Composable
private fun InviteProfileCircle(
    imageResId: Int,
    size: Dp,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = imageResId),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(Color.LightGray)
    )
}

@Composable
private fun BrokenGradientRing(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val strokeWidth = size.minDimension * 0.07f
        val inset = strokeWidth * 0.5f
        val arcSize = Size(
            width = size.width - strokeWidth,
            height = size.height - strokeWidth
        )
        val brush = Brush.sweepGradient(
            colors = listOf(
                Color(0xFF5C4CF5),
                Color(0xFF8B2FE2),
                Color(0xFFF03D69),
                Color(0xFFFF7A21),
                Color(0xFFFFD65B),
                Color(0xFF5C4CF5)
            )
        )
        val starts = listOf(16f, 62f, 108f, 154f, 200f, 246f, 292f, 338f)

        starts.forEach { startAngle ->
            drawArc(
                brush = brush,
                startAngle = startAngle,
                sweepAngle = 27f,
                useCenter = false,
                topLeft = androidx.compose.ui.geometry.Offset(inset, inset),
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }
    }
}

@Composable
private fun rememberInviteFriendsMetrics(
    maxWidth: Dp,
    maxHeight: Dp
): InviteFriendsMetrics {
    val widthRatio = (maxWidth / 390.dp).coerceIn(0.86f, 1.18f)
    val heightRatio = (maxHeight / 844.dp).coerceIn(0.82f, 1.12f)
    val compactness = min(widthRatio, heightRatio)
    val isCompactHeight = maxHeight < 760.dp
    val spacingScale = if (isCompactHeight) 0.72f else 1f
    val heroScale = compactness.coerceIn(0.88f, 1.12f)

    return remember(maxWidth, maxHeight) {
        InviteFriendsMetrics(
            horizontalPadding = (25.dp * widthRatio).coerceIn(20.dp, 32.dp),
            topSpacing = (22.dp * spacingScale).coerceIn(16.dp, 36.dp),
            actionButtonSize = (44.dp * compactness).coerceIn(40.dp, 50.dp),
            actionIconSize = (25.dp * compactness).coerceIn(22.dp, 29.dp),
            headerToSceneSpacing = (16.dp * spacingScale).coerceIn(10.dp, 20.dp),
            sceneHeight = (128.dp * heroScale).coerceIn(112.dp, 148.dp),
            sideAvatarSize = (82.dp * heroScale).coerceIn(70.dp, 94.dp),
            centerAvatarSize = (104.dp * heroScale).coerceIn(92.dp, 118.dp),
            ringSize = (126.dp * heroScale).coerceIn(112.dp, 142.dp),
            titleSpacing = (20.dp * spacingScale).coerceIn(12.dp, 24.dp),
            titleFontSize = (24f * compactness).coerceIn(21f, 28f).sp,
            titleLineHeight = (30f * compactness).coerceIn(27f, 34f).sp,
            listTopSpacing = (30.dp * spacingScale).coerceIn(18.dp, 34.dp),
            rowHeight = (97.dp * spacingScale).coerceIn(76.dp, 102.dp),
            rowAvatarSize = (56.dp * compactness).coerceIn(48.dp, 62.dp),
            rowNameFontSize = (17f * compactness).coerceIn(15f, 19f).sp,
            waveFontSize = (42f * compactness).coerceIn(36f, 48f).sp,
            buttonTopSpacing = (12.dp * spacingScale).coerceIn(8.dp, 16.dp),
            buttonHeight = (69.dp * compactness).coerceIn(60.dp, 74.dp),
            buttonCornerRadius = (21.dp * compactness).coerceIn(18.dp, 24.dp),
            buttonIconSize = (27.dp * compactness).coerceIn(24.dp, 30.dp),
            buttonFontSize = (16f * compactness).coerceIn(14f, 18f).sp,
            bottomSpacing = (22.dp * spacingScale).coerceIn(18.dp, 36.dp)
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun InviteFriendList2ScreenPreview() {
    IBookTheme(darkTheme = false) {
        InviteFriendList2Screen()
    }
}
