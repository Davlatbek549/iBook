package com.example.ibook.screens.forgot_password

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ibook.R
import kotlinx.coroutines.launch

@Composable
fun ForgotPasswordScreen(
    onBack: () -> Unit = {},
    onSendLink: suspend (method: ResetMethod) -> Unit = {}
) {
    var selectedMethod by remember { mutableStateOf<ResetMethod>(ResetMethod.EMAIL) }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {

            Spacer(modifier = Modifier.padding(top = 52.dp))

            BackButton(onClick = onBack)

            Spacer(modifier = Modifier.padding(top = 32.dp))

            TitleSection()

            Spacer(modifier = Modifier.height(32.dp))

            OptionCard(
                method = ResetMethod.EMAIL,
                isSelected = selectedMethod == ResetMethod.EMAIL,
                onClick = { selectedMethod = ResetMethod.EMAIL }
            )

            Spacer(modifier = Modifier.height(24.dp))

            OptionCard(
                method = ResetMethod.WHATSAPP,
                isSelected = selectedMethod == ResetMethod.WHATSAPP,
                onClick = { selectedMethod = ResetMethod.WHATSAPP }
            )

            Spacer(modifier = Modifier.height(24.dp))
            SendLinkButton(
                isLoading = isLoading,
                enabled = !isLoading,
                onClick = {
                    coroutineScope.launch {
                        isLoading = true
                        try {
                            onSendLink(selectedMethod)
                        } finally {
                            isLoading = false
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            ResendLinkSection()

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun BackButton(onClick: () -> Unit) {
    val borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = CircleShape
            )
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.ChevronLeft,
            contentDescription = "Go back",
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
private fun TitleSection() {
    val primaryText = MaterialTheme.colorScheme.onSurface
    val secondaryText = primaryText.copy(alpha = 0.7f)
    Column {
        Text(
            text = stringResource(R.string.title_forgot_password),
            style = MaterialTheme.typography.headlineLarge,
            color = primaryText,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.select_option),
            style = MaterialTheme.typography.titleSmall,
            color = secondaryText,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun OptionCard(
    method: ResetMethod,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val primaryText = MaterialTheme.colorScheme.onSurface
    val secondaryText = primaryText.copy(alpha = 0.7f)
    val borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else borderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Icon(
                painter = when (method) {
                    ResetMethod.EMAIL -> painterResource(R.drawable.ic_email_filled)
                    ResetMethod.WHATSAPP -> painterResource(R.drawable.ic_whatsapp)
                },
                contentDescription = null,
                tint = when (isSelected) {
                    true -> MaterialTheme.colorScheme.primary
                    false -> primaryText
                },
                modifier = Modifier.size(24.dp)
            )


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Top)
            ) {
                Text(
                    text = when (method) {
                        ResetMethod.EMAIL -> "Send to your email"
                        ResetMethod.WHATSAPP -> "Send to your whatsapp"
                    },
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = primaryText
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = when (method) {
                        ResetMethod.EMAIL -> "Link reset will be send to your email address registered"
                        ResetMethod.WHATSAPP -> "Link reset will be send to your whatsapp account"
                    },
                    fontSize = 14.sp,
                    color = secondaryText,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
private fun SendLinkButton(
    isLoading: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 0.dp
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp
            )
        } else {
            Text(
                text = "Send Link",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
private fun ResendLinkSection() {
    val primaryText = MaterialTheme.colorScheme.onSurface
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Didn't receive link? ",
            fontSize = 14.sp,
            color = primaryText.copy(alpha = 0.7f)
        )

        Text(
            text = "Resend Link",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable { /* Handle resend */ }
        )
    }
}

enum class ResetMethod {
    EMAIL,
    WHATSAPP
}

@Preview(showBackground = true)
@Composable
fun ForgotPasswordScreenPreview() {
    ForgotPasswordScreen()
}