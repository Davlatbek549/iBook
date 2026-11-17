package com.example.dz.designsystem.components.organic

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.dz.designsystem.components.icons.OrganicIcons
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.OrganicShape
import com.example.dz.designsystem.theme.organicBodyFontFamily

/**
 * The banner with the profile photo overlapping its lower-left corner. Built for Edit Profile and
 * meant to sit at the top of Profile too, so it knows nothing about editing beyond the two taps.
 *
 * Spec: banner full width at 3:1 with a 28dp radius; photo a 96dp circle in a 4dp page-colour ring,
 * its top 52dp above the banner's bottom and 14dp from its left. An empty banner is a sage tint
 * with soft circles — never a grey box; an empty photo is the initial, as everywhere else.
 */
@Composable
fun OrganicProfilePhotos(
    name: String,
    bannerUrl: String?,
    photoUrl: String?,
    onBannerClick: () -> Unit,
    onPhotoClick: () -> Unit,
    bannerDescription: String,
    photoDescription: String,
    modifier: Modifier = Modifier,
    /** Written on an empty banner, e.g. "Add a banner". `null` draws none. */
    emptyBannerLabel: String? = null,
    /** The line beside the photo, under the banner. */
    hint: String? = null,
    enabled: Boolean = true,
) {
    val bannerShape = RoundedCornerShape(OrganicShape.radiusLg)
    BoxWithConstraints(modifier = modifier.fillMaxWidth().alpha(if (enabled) 1f else 0.5f)) {
        val bannerHeight = maxWidth / 3
        // The photo hangs 44dp below the banner (96 − 52), which is the block's extra height.
        Box(modifier = Modifier.fillMaxWidth().height(bannerHeight + 44.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(3f)
                    .clip(bannerShape)
                    .clickable(enabled = enabled, role = Role.Button, onClick = onBannerClick)
                    .semantics { contentDescription = bannerDescription },
            ) {
                if (bannerUrl != null) {
                    WashedImage(url = bannerUrl, modifier = Modifier.fillMaxSize())
                } else {
                    EmptyBanner(label = emptyBannerLabel)
                }
                CameraButton(
                    modifier = Modifier.align(Alignment.BottomEnd).padding(12.dp),
                    enabled = enabled,
                    onClick = onBannerClick,
                )
            }

            Box(
                modifier = Modifier
                    .offset(x = 14.dp, y = bannerHeight - 52.dp)
                    .size(104.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(104.dp)
                        .clip(CircleShape)
                        .background(OrganicColors.bg)
                        .padding(4.dp)
                        .clip(CircleShape)
                        .clickable(enabled = enabled, role = Role.Button, onClick = onPhotoClick)
                        .semantics { contentDescription = photoDescription },
                    contentAlignment = Alignment.Center,
                ) {
                    if (photoUrl != null) {
                        WashedImage(url = photoUrl, modifier = Modifier.fillMaxSize())
                    } else {
                        OrganicAvatar(name = name, size = 96.dp, fontSize = 38.sp)
                    }
                }
                // The badge says "this changes", so it is drawn on the photo whichever state it is in.
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = (-2).dp, y = (-4).dp)
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(OrganicColors.accent)
                        .border(3.dp, OrganicColors.bg, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(OrganicIcons.Camera, contentDescription = null, modifier = Modifier.size(15.dp), tint = OrganicColors.neutral100)
                }
            }

            hint?.let {
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 132.dp, bottom = 14.dp),
                ) {
                    Text(
                        text = it,
                        fontFamily = organicBodyFontFamily(),
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        color = OrganicColors.neutral700,
                    )
                }
            }
        }
    }
}

@Composable
private fun CameraButton(onClick: () -> Unit, enabled: Boolean, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(44.dp)
            .shadow(1.dp, CircleShape, ambientColor = OrganicColors.shadow, spotColor = OrganicColors.shadow)
            .clip(CircleShape)
            .background(OrganicColors.bg)
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(OrganicIcons.Camera, contentDescription = null, modifier = Modifier.size(19.dp), tint = OrganicColors.neutral800)
    }
}

/** Sage ground with two soft circles bleeding off its edges, and an optional label top-left. */
@Composable
private fun EmptyBanner(label: String?) {
    Box(modifier = Modifier.fillMaxSize().background(OrganicColors.accent2_200)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = OrganicColors.accent2_300,
                radius = size.height * 0.95f,
                center = Offset(size.width * 0.92f, size.height * 0.05f),
            )
            drawCircle(
                color = OrganicColors.accent2_100,
                radius = size.height * 0.36f,
                center = Offset(size.width * 0.58f, size.height * 0.92f),
            )
        }
        label?.let {
            Text(
                text = it,
                modifier = Modifier.padding(start = 18.dp, top = 18.dp),
                fontFamily = organicBodyFontFamily(),
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = OrganicColors.accent2_900,
            )
        }
    }
}

/**
 * A photograph given the system's "washed" treatment — desaturated and lifted so it sits back
 * into the warm page rather than on top of it.
 */
@Composable
private fun WashedImage(url: String, modifier: Modifier = Modifier) {
    AsyncImage(
        model = url,
        contentDescription = null,
        modifier = modifier.alpha(0.94f),
        contentScale = ContentScale.Crop,
        colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0.6f) }),
    )
}
