package com.example.dz.app_components.buttons.icon_button

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.ic_apple
import dz.shared.generated.resources.ic_google
import org.jetbrains.compose.resources.painterResource

@Composable
fun IconButton(
    onClick: (() -> Unit),
    modifier: Modifier = Modifier,
    text: String,
    leftIcon: Painter,
    borderColor: Color = MaterialTheme.colorScheme.outline,
    borderWidth: Dp = 1.dp
) {
    Row(
        modifier = modifier
            .border(
                color = borderColor,
                width = borderWidth,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = leftIcon,
            contentDescription = "Icon",
            tint = Color.Unspecified // preserves original icon colors
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, style = MaterialTheme.typography.bodyLarge)
    }
}


@Preview(showBackground = true)
@Composable
fun IconButtonPreview() {
    Column {
        Spacer(modifier = Modifier.height(10.dp))
        IconButton(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            text = "Sign in with Google",
            leftIcon = painterResource(Res.drawable.ic_google)
        )
        Spacer(modifier = Modifier.height(10.dp))
        IconButton(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            text = "Sign in with Apple",
            leftIcon = painterResource(Res.drawable.ic_apple)
        )
        Spacer(modifier = Modifier.height(10.dp))
    }
}
