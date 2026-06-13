package com.example.dz.screens.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.app_components.ink.inkCard
import com.example.dz.theme.InkShape
import com.example.dz.theme.inkBodyFontFamily
import com.example.dz.theme.inkColors
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.get_started
import dz.shared.generated.resources.img_maria_renzy
import dz.shared.generated.resources.onb3_copy
import dz.shared.generated.resources.onb3_title
import dz.shared.generated.resources.profile_2
import dz.shared.generated.resources.profile_3
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun OnboardingScreenThree(
    onGetStartedClick: () -> Unit = {},
    onLoginClick: () -> Unit = {}
) {
    OnboardingScaffold(
        title = stringResource(Res.string.onb3_title),
        copy = stringResource(Res.string.onb3_copy),
        pageIndex = 2,
        buttonText = stringResource(Res.string.get_started),
        onButtonClick = onGetStartedClick,
        onSkipClick = onLoginClick,
        hero = { FriendActivityHero() }
    )
}

@Composable
private fun FriendActivityHero() {
    Column(
        modifier = Modifier.width(250.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ActivityCard(Res.drawable.profile_2, "Patricia", "is reading Olive, Again", offsetX = 0.dp)
        ActivityCard(Res.drawable.profile_3, "Daniel", "finished Bestiary", offsetX = 16.dp)
        ActivityCard(Res.drawable.img_maria_renzy, "Maria", "loved Mexican Gothic", offsetX = 4.dp)
    }
}

@Composable
private fun ActivityCard(
    avatar: DrawableResource,
    name: String,
    activity: String,
    offsetX: Dp,
) {
    val colors = inkColors()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .offset(x = offsetX)
            .inkCard(colors)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Image(
            painter = painterResource(avatar),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(InkShape.radiusSm))
        )
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.SemiBold, color = colors.ink)) {
                    append(name)
                }
                append(" ")
                append(activity)
            },
            fontFamily = inkBodyFontFamily(),
            fontSize = 12.5.sp,
            lineHeight = 17.sp,
            color = colors.inkSoft
        )
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
fun OnboardingScreenThreePreview() {
    OnboardingScreenThree()
}
