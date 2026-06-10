package com.example.dz.screens.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.app_components.buttons.app_button.AppButton
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun OnboardingScreenTwo(
    onNextClick: () -> Unit = {},
    onSkipClick: () -> Unit = {}
) {
    val primaryText = MaterialTheme.colorScheme.onSurface
    val secondaryText = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        Image(
            painter = painterResource(Res.drawable.onboarding_2),
            contentDescription = null,
            modifier = Modifier
                .width(600.dp)
                .height(650.dp)
                .offset(x = (+10).dp, y = 120.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(324.dp)
                .align(Alignment.BottomCenter)
                .background(
                    MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
                )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(top = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = stringResource(Res.string.onboarding2_bottom_text),
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = primaryText
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    fontSize = 15.sp,
                    text = stringResource(Res.string.onboarding2_bottom_text_2),
                    style = MaterialTheme.typography.bodyMedium,
                    color = secondaryText,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                AppButton(
                    onClick = onNextClick,
                    text = stringResource(Res.string.next),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                )
            }
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
                        .background(if (index == 1) MaterialTheme.colorScheme.primary else secondaryText.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(50)
                        )
                )
            }
        }

    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
fun OnboardingScreenTwpPreview() {
    OnboardingScreenTwo()
}
