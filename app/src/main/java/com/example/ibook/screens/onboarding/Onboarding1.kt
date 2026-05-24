package com.example.ibook.screens.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
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
fun OnboardingScreenOne() {
    val primaryText = MaterialTheme.colorScheme.onSurface
    val secondaryText = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        Image(
            painter = painterResource(id = R.drawable.onboarding1),
            contentDescription = null,
            modifier = Modifier
                .width(274.dp)
                .height(558.dp)
                .offset(x = (+23).dp, y = 135.dp)
                .rotate(+7.9f)
        )

        Image(
            painter = painterResource(id = R.drawable.onboarding1_2),
            contentDescription = null,
            modifier = Modifier
                .width(222.dp)
                .height(453.dp)
                .offset(x = 195.dp, y = 220.dp)
                .rotate(-8.32f)
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
                    text = stringResource(R.string.onboarding1_bottom_text),
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = primaryText
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    fontSize = 15.sp,
                    text = stringResource(R.string.onboarding1_bottom_text_2),
                    style = MaterialTheme.typography.bodyMedium,
                    color = secondaryText,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                AppButton(
                    onClick = {},
                    text = stringResource(R.string.next),
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
                        .background(if (index == 0) MaterialTheme.colorScheme.primary else secondaryText.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(50)
                        )
                )
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingScreenOnePreview() {
    OnboardingScreenOne()
}