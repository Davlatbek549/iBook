package com.example.dz.presentation.profile.profile_edit

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.core.media.CropGeometry
import com.example.dz.core.media.CropPosition
import com.example.dz.core.media.CropShape
import com.example.dz.core.platform.StatusBarAppearance
import com.example.dz.designsystem.components.organic.ORGANIC_GUTTER
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.OrganicShape
import com.example.dz.designsystem.theme.organicBodyFontFamily
import com.example.dz.designsystem.theme.organicHeadingFontFamily
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.ep_cancel
import dz.shared.generated.resources.ep_crop_area_banner
import dz.shared.generated.resources.ep_crop_area_photo
import dz.shared.generated.resources.ep_crop_hint
import dz.shared.generated.resources.ep_crop_title
import dz.shared.generated.resources.ep_done
import kotlin.math.min
import kotlin.math.roundToInt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.decodeToImageBitmap
import org.jetbrains.compose.resources.stringResource

/**
 * Frames a picked picture before it goes on the profile: a circle for the photo, a 3:1 banner for
 * the banner, each exactly the shape the server stores. Drag to move, pinch to zoom; the picture
 * always covers the frame, so there is no way to leave an empty edge.
 *
 * Dark behind the picture, unlike the rest of Organic, so the photo is judged on its own colours
 * and not against the cream page. The top bar keeps Edit Profile's Cancel · title · action.
 */
@Composable
internal fun PhotoCropScreen(
    crop: PhotoCrop,
    onConfirm: (CropPosition) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val geometry = remember(crop.image) { CropGeometry(crop.image.width, crop.image.height, crop.shape) }
    var position by remember(crop.image) { mutableStateOf(CropPosition()) }
    // Decoded off the main thread: a 2048px JPEG takes long enough to drop frames.
    val bitmap by produceState<ImageBitmap?>(null, crop.image) {
        value = withContext(Dispatchers.Default) { runCatching { crop.image.bytes.decodeToImageBitmap() }.getOrNull() }
    }

    StatusBarAppearance(darkBackground = true)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(OrganicColors.neutral900)
            .statusBarsPadding()
            .navigationBarsPadding(),
    ) {
        CropTopBar(
            isCutting = crop.isCutting,
            canConfirm = bitmap != null,
            onCancel = onCancel,
            onConfirm = { onConfirm(position) },
        )

        BoxWithConstraints(modifier = Modifier.fillMaxWidth().weight(1f)) {
            val density = LocalDensity.current
            val frame = with(density) {
                val width = when (crop.shape) {
                    CropShape.Circle -> min(maxWidth.toPx(), maxHeight.toPx()) - 2 * ORGANIC_GUTTER.toPx()
                    CropShape.Banner -> maxWidth.toPx() - 2 * 16.dp.toPx()
                }
                val size = Size(width, width / crop.shape.aspect)
                Rect(Offset((maxWidth.toPx() - size.width) / 2, (maxHeight.toPx() - size.height) / 2), size)
            }
            val cornerRadius = with(density) { OrganicShape.radiusLg.toPx() }
            val currentFrame by rememberUpdatedState(frame)
            val gesturesOn by rememberUpdatedState(!crop.isCutting)
            val areaDescription = stringResource(
                if (crop.shape == CropShape.Banner) Res.string.ep_crop_area_banner else Res.string.ep_crop_area_photo
            )

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    // A zoomed picture is larger than the stage; without this it draws over the top bar.
                    .clipToBounds()
                    .semantics { contentDescription = areaDescription }
                    .pointerInput(geometry) {
                        detectTransformGestures { centroid, pan, zoom, _ ->
                            if (!gesturesOn) return@detectTransformGestures
                            val f = currentFrame
                            position = geometry.transform(
                                position = position,
                                frameWidth = f.width,
                                panX = pan.x,
                                panY = pan.y,
                                zoomBy = zoom,
                                centroidX = centroid.x - f.center.x,
                                centroidY = centroid.y - f.center.y,
                            )
                        }
                    },
            ) {
                val image = bitmap ?: return@Canvas
                val scale = geometry.scaleOnScreen(position.zoom, frame.width)
                val drawnWidth = crop.image.width * scale
                val drawnHeight = crop.image.height * scale
                drawImage(
                    image = image,
                    dstOffset = IntOffset(
                        (frame.center.x - position.centerX * drawnWidth).roundToInt(),
                        (frame.center.y - position.centerY * drawnHeight).roundToInt(),
                    ),
                    dstSize = IntSize(drawnWidth.roundToInt(), drawnHeight.roundToInt()),
                    filterQuality = FilterQuality.High,
                )

                // Everything outside the frame is dimmed, so the frame reads as a window.
                val hole = Path().apply {
                    when (crop.shape) {
                        CropShape.Circle -> addOval(frame)
                        CropShape.Banner -> addRoundRect(RoundRect(frame, CornerRadius(cornerRadius)))
                    }
                }
                val scrim = Path().apply {
                    fillType = PathFillType.EvenOdd
                    addRect(Rect(Offset.Zero, size))
                    addPath(hole)
                }
                drawPath(scrim, OrganicColors.neutral900.copy(alpha = 0.72f))
                drawPath(hole, OrganicColors.neutral100.copy(alpha = 0.9f), style = Stroke(width = 2.dp.toPx()))
            }

            if (bitmap == null) {
                CircularProgressIndicator(color = OrganicColors.accent400, modifier = Modifier.align(Alignment.Center))
            }

            Text(
                text = stringResource(Res.string.ep_crop_hint),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = ORGANIC_GUTTER, vertical = 28.dp),
                textAlign = TextAlign.Center,
                fontFamily = organicBodyFontFamily(),
                fontSize = 14.sp,
                color = OrganicColors.neutral400,
            )
        }
    }
}

@Composable
private fun CropTopBar(isCutting: Boolean, canConfirm: Boolean, onCancel: () -> Unit, onConfirm: () -> Unit) {
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
                .alpha(if (isCutting) 0.45f else 1f)
                .clickable(enabled = !isCutting, role = Role.Button, onClick = onCancel)
                .padding(vertical = 8.dp),
            fontFamily = body,
            fontSize = 15.sp,
            color = OrganicColors.neutral300,
        )
        Text(
            text = stringResource(Res.string.ep_crop_title),
            modifier = Modifier.align(Alignment.Center),
            fontFamily = organicHeadingFontFamily(),
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp,
            color = OrganicColors.neutral100,
        )
        Box(modifier = Modifier.align(Alignment.CenterEnd)) {
            if (isCutting) {
                CircularProgressIndicator(
                    color = OrganicColors.accent400,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(20.dp),
                )
            } else {
                Text(
                    text = stringResource(Res.string.ep_done),
                    modifier = Modifier
                        .alpha(if (canConfirm) 1f else 0.45f)
                        .clickable(enabled = canConfirm, role = Role.Button, onClick = onConfirm)
                        .padding(vertical = 8.dp),
                    fontFamily = body,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = OrganicColors.accent400,
                )
            }
        }
    }
}
