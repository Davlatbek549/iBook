package com.ibook.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class AppButtonType {
    PRIMARY,
    OUTLINED
}

@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    type: AppButtonType = AppButtonType.PRIMARY
) {
    val shape = RoundedCornerShape(14.dp)

    when (type) {
        AppButtonType.PRIMARY -> {
            Button(
                onClick = onClick,
                enabled = enabled,
                modifier = modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = shape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6C5CE7),
                    disabledContainerColor = Color(0xFF6C5CE7).copy(alpha = 0.4f)
                )
            ) {
                ButtonText(text, Color.White)
            }
        }

        AppButtonType.OUTLINED -> {
            OutlinedButton(
                onClick = onClick,
                enabled = enabled,
                modifier = modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = shape,
                border = BorderStroke(1.dp, Color(0xFF6C5CE7)),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White
                )
            ) {
                ButtonText(text, Color(0xFF6C5CE7))
            }
        }
    }
}

@Composable
private fun ButtonText(
    text: String,
    color: Color
) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color = color
    )
}

@Preview(
    name = "App Buttons – Verify & Done",
    showBackground = true,
    backgroundColor = 0xFFF5F5F5
)
@Composable
fun AppButtonAllPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AppButton(
            text = "Verify",
            onClick = {}
        )

        AppButton(
            text = "Done",
            type = AppButtonType.OUTLINED,
            onClick = {}
        )
    }
}