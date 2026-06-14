package com.example.dz.presentation.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.ink.InkButton
import com.example.dz.designsystem.components.ink.InkDots
import com.example.dz.designsystem.theme.inkBodyFontFamily
import com.example.dz.designsystem.theme.inkColors
import com.example.dz.designsystem.theme.inkDisplayFontFamily
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.skip
import org.jetbrains.compose.resources.stringResource

/** Shared "Ink & Paper" onboarding layout: skip · hero · title · copy · dots · action. */
@Composable
internal fun OnboardingScaffold(
    title: String,
    copy: String,
    pageIndex: Int,
    buttonText: String,
    onButtonClick: () -> Unit,
    onSkipClick: () -> Unit,
    hero: @Composable () -> Unit,
) {
    val colors = inkColors()
    val displayFont = inkDisplayFontFamily()
    val bodyFont = inkBodyFontFamily()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.paper)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(start = 26.dp, end = 26.dp, bottom = 26.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = stringResource(Res.string.skip),
                modifier = Modifier
                    .clickable(onClick = onSkipClick)
                    .padding(8.dp),
                fontFamily = bodyFont,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.5.sp,
                color = colors.muted
            )
        }

        Box(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            hero()
        }

        Text(
            text = title,
            modifier = Modifier.fillMaxWidth(),
            fontFamily = displayFont,
            fontWeight = FontWeight.Medium,
            fontSize = 30.sp,
            lineHeight = 34.5.sp,
            textAlign = TextAlign.Center,
            color = colors.ink
        )

        Text(
            text = copy,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, top = 14.dp),
            fontFamily = bodyFont,
            fontSize = 14.sp,
            lineHeight = 23.sp,
            textAlign = TextAlign.Center,
            color = colors.inkSoft
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, bottom = 22.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            InkDots(count = 3, activeIndex = pageIndex, colors = colors)
        }

        InkButton(text = buttonText, onClick = onButtonClick, colors = colors)
    }
}
