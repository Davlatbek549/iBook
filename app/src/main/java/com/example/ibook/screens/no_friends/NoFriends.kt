package com.example.ibook.screens.no_friends

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Message
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.ibook.R
import kotlin.math.min

data class OrbitProfile(
    val imageRes: Int,
    val sizeFraction: Float,
    val radiusFraction: Float,
    val startAngleDeg: Float,
    val speedMultiplier: Float = 1f
)

@Composable
fun NoFriendsScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onMessageClick: () -> Unit = {},
    onFacebookInviteClick: () -> Unit = {},
    onInstagramInviteClick: () -> Unit = {}
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val metrics = rememberNoFriendsMetrics(maxWidth = maxWidth, maxHeight = maxHeight)
        val secondaryText = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        val containerColor = MaterialTheme.colorScheme.background

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = containerColor
        ) {
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircleActionButton(
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

                    CircleActionButton(
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

                Spacer(modifier = Modifier.height(metrics.headerToSceneSpacing))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(metrics.sceneContainerHeight),
                    contentAlignment = Alignment.Center
                ) {
                    ResponsiveProfilesScene(
                        modifier = Modifier.size(metrics.sceneSize)
                    )
                }

                Spacer(modifier = Modifier.height(metrics.sceneToTextSpacing))

                Text(
                    text = stringResource(R.string.invite_text),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = metrics.messageFontSize,
                        lineHeight = metrics.messageLineHeight
                    ),
                    fontWeight = FontWeight.SemiBold,
                    color = secondaryText
                )

                Spacer(modifier = Modifier.height(metrics.textToButtonSpacing))

                SocialInviteButtonOutlined(
                    text = stringResource(R.string.invite_from_facebook),
                    iconRes = R.drawable.ic_facebook,
                    onClick = onFacebookInviteClick,
                    height = metrics.buttonHeight,
                    iconSize = metrics.buttonIconSize,
                    cornerRadius = metrics.buttonCornerRadius,
                    borderWidth = metrics.buttonBorderWidth,
                    textStyle = MaterialTheme.typography.titleMedium.copy(
                        fontSize = metrics.buttonFontSize,
                        lineHeight = metrics.buttonLineHeight
                    )
                )

                Spacer(modifier = Modifier.height(metrics.buttonsSpacing))

                SocialInviteButtonFilled(
                    text = stringResource(R.string.invite_from_instagram),
                    iconRes = R.drawable.ic_instagram,
                    onClick = onInstagramInviteClick,
                    height = metrics.buttonHeight,
                    iconSize = metrics.buttonIconSize,
                    cornerRadius = metrics.buttonCornerRadius,
                    textStyle = MaterialTheme.typography.titleMedium.copy(
                        fontSize = metrics.buttonFontSize,
                        lineHeight = metrics.buttonLineHeight
                    )
                )

                Spacer(modifier = Modifier.height(metrics.bottomSpacing))
            }
        }
    }
}

@Composable
fun ResponsiveProfilesScene(
    modifier: Modifier = Modifier
) {
    val centerImage = R.drawable.profile_12

    val profiles = listOf(
        OrbitProfile(R.drawable.profile_1, 0.18f, 0.72f, 210f, 1.00f),
        OrbitProfile(R.drawable.profile_2, 0.07f, 0.82f, 195f, 0.85f),
        OrbitProfile(R.drawable.profile_3, 0.20f, 0.78f, 160f, 1.10f),
        OrbitProfile(R.drawable.profile_4, 0.08f, 0.88f, 138f, 0.75f),
        OrbitProfile(R.drawable.profile_5, 0.12f, 0.64f, 118f, 1.00f),

        OrbitProfile(R.drawable.profile_6, 0.22f, 0.83f, 92f, 0.90f),
        OrbitProfile(R.drawable.profile_7, 0.08f, 0.60f, 64f, 1.15f),
        OrbitProfile(R.drawable.profile_8, 0.18f, 0.80f, 28f, 0.80f),
        OrbitProfile(R.drawable.profile_9, 0.07f, 0.92f, 8f, 1.05f),
        OrbitProfile(R.drawable.profile_10, 0.16f, 0.76f, 340f, 0.70f),

        OrbitProfile(R.drawable.profile_11, 0.12f, 0.85f, 318f, 0.90f),
        OrbitProfile(R.drawable.profile_1, 0.15f, 0.96f, 286f, 1.00f),
        OrbitProfile(R.drawable.profile_2, 0.08f, 0.90f, 266f, 1.10f),
        OrbitProfile(R.drawable.profile_3, 0.07f, 0.98f, 244f, 0.80f),
        OrbitProfile(R.drawable.profile_4, 0.07f, 0.74f, 228f, 1.00f),

        OrbitProfile(R.drawable.profile_5, 0.17f, 0.88f, 184f, 0.75f),
        OrbitProfile(R.drawable.profile_6, 0.07f, 0.98f, 172f, 1.20f),
        OrbitProfile(R.drawable.profile_7, 0.16f, 0.95f, 144f, 0.90f),
        OrbitProfile(R.drawable.profile_8, 0.08f, 1.00f, 48f, 1.00f),
        OrbitProfile(R.drawable.profile_9, 0.10f, 0.68f, 52f, 0.85f),
        OrbitProfile(R.drawable.profile_10, 0.08f, 0.92f, 108f, 1.15f)
    )

    val infiniteTransition = rememberInfiniteTransition(label = "orbit_transition")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 32000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "orbit_rotation"
    )

    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val density = LocalDensity.current
        val sceneSideDp = min(maxWidth, maxHeight)
        val sceneSidePx = with(density) { sceneSideDp.toPx() }

        val centerSize = with(density) { (sceneSidePx * 0.26f).toDp() }
        val ringSize = with(density) { (sceneSidePx * 0.42f).toDp() }

        Box(
            modifier = Modifier.size(sceneSideDp),
            contentAlignment = Alignment.Center
        ) {
            profiles.forEach { profile ->
                val avatarSize = with(density) { (sceneSidePx * profile.sizeFraction).toDp() }

                val radiusPx = sceneSidePx * 0.5f * profile.radiusFraction
                val angleDeg = profile.startAngleDeg + rotation * profile.speedMultiplier
                val angleRad = Math.toRadians(angleDeg.toDouble())

                val xDp = with(density) { (radiusPx * kotlin.math.cos(angleRad)).toFloat().toDp() }
                val yDp = with(density) { (radiusPx * kotlin.math.sin(angleRad)).toFloat().toDp() }

                ProfileCircle(
                    imageResId = profile.imageRes,
                    size = avatarSize,
                    modifier = Modifier
                        .offset(x = xDp, y = yDp)
                        .zIndex(1f)
                )
            }

            GradientArcRing(
                modifier = Modifier
                    .size(ringSize)
                    .zIndex(2f)
            )

            ProfileCircle(
                imageResId = centerImage,
                size = centerSize,
                modifier = Modifier.zIndex(3f)
            )
        }
    }
}

private fun orbitShiftX(index: Int, angle: Float): Float {
    val radians = Math.toRadians((angle + index * 17f).toDouble())
    val amplitude = when (index % 4) {
        0 -> 0.018f
        1 -> 0.012f
        2 -> 0.015f
        else -> 0.010f
    }
    return (kotlin.math.cos(radians) * amplitude).toFloat()
}

private fun orbitShiftY(index: Int, angle: Float): Float {
    val radians = Math.toRadians((angle + index * 19f).toDouble())
    val amplitude = when (index % 4) {
        0 -> 0.015f
        1 -> 0.010f
        2 -> 0.018f
        else -> 0.012f
    }
    return (kotlin.math.sin(radians) * amplitude).toFloat()
}

@Composable
fun GradientArcRing(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val strokeWidth = size.minDimension * 0.06f
        val arcSize = Size(size.width, size.height)

        val brush = Brush.sweepGradient(
            colors = listOf(
                Color(0xFF5C4CF5),
                Color(0xFF8E2DE2),
                Color(0xFFFF3D57),
                Color(0xFFFF7A21),
                Color(0xFFFFC83D),
                Color(0xFF5C4CF5)
            )
        )

        val arcLength = 28f
        val starts = listOf(10f, 56f, 102f, 148f, 194f, 240f, 286f, 332f)

        starts.forEach { startAngle ->
            drawArc(
                brush = brush,
                startAngle = startAngle,
                sweepAngle = arcLength,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                size = arcSize
            )
        }
    }
}

@Composable
fun ProfileCircle(
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
fun CircleActionButton(
    onClick: () -> Unit,
    buttonSize: Dp,
    iconSize: Dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .size(buttonSize)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surface)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.18f),
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
fun SocialInviteButtonOutlined(
    text: String,
    iconRes: Int,
    onClick: () -> Unit,
    height: Dp,
    iconSize: Dp,
    cornerRadius: Dp,
    borderWidth: Dp,
    textStyle: TextStyle
) {
    val colorScheme = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(cornerRadius))
            .border(
                width = borderWidth,
                color = colorScheme.primary,
                shape = RoundedCornerShape(cornerRadius)
            )
            .background(colorScheme.surface)
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(iconSize)
        )

        Spacer(modifier = Modifier.width(iconSize * 0.45f))

        Text(
            text = text,
            color = colorScheme.onSurface,
            style = textStyle,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun SocialInviteButtonFilled(
    text: String,
    iconRes: Int,
    onClick: () -> Unit,
    height: Dp,
    iconSize: Dp,
    cornerRadius: Dp,
    textStyle: TextStyle
) {
    val colorScheme = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(cornerRadius))
            .background(
                color = colorScheme.primary
            )
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(iconSize)
        )

        Spacer(modifier = Modifier.width(iconSize * 0.45f))

        Text(
            text = text,
            color = Color.White,
            style = textStyle,
            fontWeight = FontWeight.SemiBold
        )
    }
}

private data class NoFriendsMetrics(
    val horizontalPadding: Dp,
    val topSpacing: Dp,
    val headerToSceneSpacing: Dp,
    val sceneContainerHeight: Dp,
    val sceneSize: Dp,
    val sceneToTextSpacing: Dp,
    val textToButtonSpacing: Dp,
    val buttonsSpacing: Dp,
    val bottomSpacing: Dp,
    val actionButtonSize: Dp,
    val actionIconSize: Dp,
    val messageFontSize: androidx.compose.ui.unit.TextUnit,
    val messageLineHeight: androidx.compose.ui.unit.TextUnit,
    val buttonHeight: Dp,
    val buttonIconSize: Dp,
    val buttonCornerRadius: Dp,
    val buttonBorderWidth: Dp,
    val buttonFontSize: androidx.compose.ui.unit.TextUnit,
    val buttonLineHeight: androidx.compose.ui.unit.TextUnit
)

@Composable
private fun rememberNoFriendsMetrics(
    maxWidth: Dp,
    maxHeight: Dp
): NoFriendsMetrics {
    val widthRatio = (maxWidth / 390.dp).coerceIn(0.86f, 1.22f)
    val heightRatio = (maxHeight / 844.dp).coerceIn(0.82f, 1.18f)
    val compactness = min(widthRatio, heightRatio)
    val spaciousness = ((widthRatio + heightRatio) / 2f).coerceIn(0.86f, 1.18f)
    val isCompactHeight = maxHeight < 760.dp
    val topSpacingFactor = if (isCompactHeight) 0.55f else 1f
    val headerSpacingFactor = if (isCompactHeight) 0.6f else 1f
    val sceneSpacingFactor = if (isCompactHeight) 0.72f else 1f
    val textSpacingFactor = if (isCompactHeight) 0.76f else 1f
    val bottomSpacingFactor = if (isCompactHeight) 0.5f else 1f

    val sceneBase = min(
        maxWidth * if (isCompactHeight) 0.86f else 0.92f,
        maxHeight * if (isCompactHeight) 0.37f else 0.43f
    )
    val sceneSize = sceneBase.coerceIn(if (isCompactHeight) 220.dp else 250.dp, 500.dp)
    val sceneContainerHeight = if (sceneSize * 1.08f > 280.dp) {
        sceneSize * if (isCompactHeight) 0.96f else 1.08f
    } else {
        if (isCompactHeight) 232.dp else 280.dp
    }

    return NoFriendsMetrics(
        horizontalPadding = (22.dp * widthRatio).coerceIn(16.dp, 32.dp),
        topSpacing = ((40.dp * heightRatio) * topSpacingFactor).coerceIn(12.dp, 56.dp),
        headerToSceneSpacing = ((18.dp * heightRatio) * headerSpacingFactor).coerceIn(6.dp, 26.dp),
        sceneContainerHeight = sceneContainerHeight,
        sceneSize = sceneSize,
        sceneToTextSpacing = ((20.dp * heightRatio) * sceneSpacingFactor).coerceIn(10.dp, 44.dp),
        textToButtonSpacing = ((26.dp * compactness) * textSpacingFactor).coerceIn(14.dp, 34.dp),
        buttonsSpacing = ((18.dp * compactness) * textSpacingFactor).coerceIn(10.dp, 24.dp),
        bottomSpacing = ((23.dp * heightRatio) * bottomSpacingFactor).coerceIn(10.dp, 40.dp),
        actionButtonSize = (54.dp * compactness).coerceIn(44.dp, 60.dp),
        actionIconSize = (24.dp * compactness).coerceIn(18.dp, 26.dp),
        messageFontSize = (18f * spaciousness).coerceIn(15f, 20f).sp,
        messageLineHeight = (26f * spaciousness).coerceIn(22f, 28f).sp,
        buttonHeight = (72.dp * compactness).coerceIn(58.dp, 78.dp),
        buttonIconSize = (28.dp * compactness).coerceIn(20.dp, 30.dp),
        buttonCornerRadius = (24.dp * compactness).coerceIn(18.dp, 26.dp),
        buttonBorderWidth = (2.dp * compactness).coerceIn(1.25.dp, 2.5.dp),
        buttonFontSize = (16f * spaciousness).coerceIn(14f, 18f).sp,
        buttonLineHeight = (22f * spaciousness).coerceIn(18f, 24f).sp
    )
}

@Preview(
    name = "No Friends Screen",
    showBackground = true,
    showSystemUi = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
fun NoFriendsScreenPreview() {
    MaterialTheme {
        NoFriendsScreen()
    }
}
