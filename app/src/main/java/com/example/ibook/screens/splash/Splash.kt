package com.example.ibook.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ibook.R
import com.example.ibook.ui.theme.ColorPrimary
import com.example.ibook.ui.theme.ColorBackgroundLight
import com.example.ibook.ui.theme.ColorText

private data class SplashMetrics(
    val logoSize: Dp,
    val logoTitleGap: Dp,
    val appNameFontSize: TextUnit,
    val footerBottomPadding: Dp,
    val footerTitleFontSize: TextUnit,
    val footerVersionGap: Dp,
    val versionFontSize: TextUnit
)

@Composable
fun SplashScreen() {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorBackgroundLight)
    ) {
        val metrics = rememberSplashMetrics(maxWidth = maxWidth, maxHeight = maxHeight)

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.ic_app_image),
                contentDescription = "iBook Logo",
                modifier = Modifier.size(metrics.logoSize)
            )

            Spacer(modifier = Modifier.height(metrics.logoTitleGap))

            Text(
                text = stringResource(R.string.app_name),
                fontSize = metrics.appNameFontSize,
                fontWeight = FontWeight.SemiBold,
                color = ColorPrimary
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = metrics.footerBottomPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = stringResource(R.string.logo),
                fontSize = metrics.footerTitleFontSize,
                color = ColorPrimary
            )

            Spacer(modifier = Modifier.height(metrics.footerVersionGap))

            Text(
                text = stringResource(R.string.version),
                fontSize = metrics.versionFontSize,
                color = ColorText
            )
        }
    }
}

@Composable
private fun rememberSplashMetrics(
    maxWidth: Dp,
    maxHeight: Dp
): SplashMetrics = remember(maxWidth, maxHeight) {
    val widthScale = (maxWidth / 390.dp).coerceIn(0.82f, 1.25f)
    val heightScale = (maxHeight / 844.dp).coerceIn(0.82f, 1.2f)
    val compactness = minOf(widthScale, heightScale)

    SplashMetrics(
        logoSize = (164.dp * compactness).coerceIn(128.dp, 198.dp),
        logoTitleGap = (10.dp * heightScale).coerceIn(8.dp, 14.dp),
        appNameFontSize = (35f * compactness).coerceIn(28f, 42f).sp,
        footerBottomPadding = (60.dp * heightScale).coerceIn(40.dp, 76.dp),
        footerTitleFontSize = (14f * compactness).coerceIn(12f, 17f).sp,
        footerVersionGap = (4.dp * heightScale).coerceIn(3.dp, 6.dp),
        versionFontSize = (12f * compactness).coerceIn(10f, 15f).sp
    )
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen()
}
