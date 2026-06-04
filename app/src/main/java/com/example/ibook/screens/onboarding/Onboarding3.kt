package com.example.ibook.screens.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ibook.R
import com.example.ibook.app_components.buttons.AppButton

@Composable
fun OnboardingScreenThree() {
    val primaryText = Color(0xFF353645)
    val secondaryText = Color(0xFF9B9EAE)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FF))
    ) {
        PhoneChallengeImages(
            modifier = Modifier
                .fillMaxWidth()
                .height(496.dp)
                .align(Alignment.TopCenter)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(324.dp)
                .align(Alignment.BottomCenter)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(top = 40.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(R.string.onboarding3_bottom_text),
                    style = MaterialTheme.typography.displaySmall,
                    fontSize = 28.sp,
                    lineHeight = 34.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryText,
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.onboarding3_bottom_text_2),
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    color = secondaryText,
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.height(28.dp))

                AppButton(
                    onClick = {},
                    text = stringResource(R.string.get_started),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                )
            }

            Box(
                modifier = Modifier
                    .width(86.dp)
                    .height(4.dp)
                    .align(Alignment.BottomCenter)
                    .offset(y = (-8).dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFF23242D).copy(alpha = 0.62f))
            )
        }

        Row(
            modifier = Modifier
                .width(327.dp)
                .height(4.dp)
                .align(Alignment.TopCenter)
                .offset(y = 64.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val itemCount = 3
            val gap = 12.dp
            val totalWidth = 327.dp
            val itemWidth = (totalWidth - gap * (itemCount - 1)) / itemCount

            repeat(itemCount) { index ->
                Box(
                    modifier = Modifier
                        .height(4.dp)
                        .width(itemWidth)
                        .background(
                            color = if (index == 2) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                secondaryText.copy(alpha = 0.22f)
                            },
                            shape = RoundedCornerShape(50)
                        )
                )
            }
        }
    }
}

@Composable
private fun PhoneChallengeImages(
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Image(
            painter = painterResource(id = R.drawable.img_onboarding3_phone),
            contentDescription = null,
            modifier = Modifier
                .width(600.dp)
                .height(650.dp)
                .align(Alignment.TopCenter)
                .offset(x = 3.dp, y = 120.dp),
            contentScale = ContentScale.Fit
        )

        Image(
            painter = painterResource(id = R.drawable.onboarding3_3),
            contentDescription = null,
            modifier = Modifier
                .size(73.dp)
                .offset(x = 74.dp, y = 88.dp),
            contentScale = ContentScale.Fit
        )

        Image(
            painter = painterResource(id = R.drawable.onbording3_1),
            contentDescription = null,
            modifier = Modifier
                .size(92.dp)
                .offset(x = 228.dp, y = 164.dp),
            contentScale = ContentScale.Fit
        )

        Image(
            painter = painterResource(id = R.drawable.onborading3_2),
            contentDescription = null,
            modifier = Modifier
                .size(87.dp)
                .offset(x = 18.dp, y = 300.dp),
            contentScale = ContentScale.Fit
        )
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
fun OnboardingScreenThreePreview() {
    OnboardingScreenThree()
}
