package com.example.dz.presentation.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.dz.designsystem.theme.InkShape
import com.example.dz.designsystem.theme.inkColors
import com.example.dz.designsystem.theme.inkDisplayFontFamily
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.book_cover
import dz.shared.generated.resources.book_cover_2
import dz.shared.generated.resources.book_cover_3
import dz.shared.generated.resources.continue_btn
import dz.shared.generated.resources.onb1_caption
import dz.shared.generated.resources.onb1_copy
import dz.shared.generated.resources.onb1_title
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun OnboardingScreenOne(
    onNextClick: () -> Unit = {},
    onSkipClick: () -> Unit = {}
) {
    OnboardingScaffold(
        title = stringResource(Res.string.onb1_title),
        copy = stringResource(Res.string.onb1_copy),
        pageIndex = 0,
        buttonText = stringResource(Res.string.continue_btn),
        onButtonClick = onNextClick,
        onSkipClick = onSkipClick,
        hero = { FannedCoversHero() }
    )
}

@Composable
private fun FannedCoversHero() {
    val colors = inkColors()

    Box(modifier = Modifier.size(width = 230.dp, height = 270.dp)) {
        FannedCover(
            cover = Res.drawable.book_cover_3,
            width = 110.dp, height = 162.dp,
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(y = 30.dp)
                .graphicsLayer { rotationZ = -7f }
        )
        FannedCover(
            cover = Res.drawable.book_cover_2,
            width = 110.dp, height = 162.dp,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(y = 36.dp)
                .graphicsLayer { rotationZ = 7f }
        )
        FannedCover(
            cover = Res.drawable.book_cover,
            width = 124.dp, height = 184.dp,
            elevation = 18.dp,
            modifier = Modifier
                .offset(x = 58.dp, y = 8.dp)
                .zIndex(2f)
        )
        Text(
            text = stringResource(Res.string.onb1_caption),
            modifier = Modifier.align(Alignment.BottomCenter),
            fontFamily = inkDisplayFontFamily(),
            fontStyle = FontStyle.Italic,
            fontSize = 13.sp,
            color = colors.muted
        )
    }
}

@Composable
private fun FannedCover(
    cover: DrawableResource,
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier,
    elevation: Dp = 12.dp,
) {
    Image(
        painter = painterResource(cover),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .width(width)
            .height(height)
            .shadow(elevation, RoundedCornerShape(InkShape.cover), clip = true)
    )
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
fun OnboardingScreenOnePreview() {
    OnboardingScreenOne()
}
