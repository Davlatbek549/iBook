package com.example.dz.app_components.inputs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.border
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.example.dz.theme.DZTheme
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.ic_email
import dz.shared.generated.resources.ic_password
import dz.shared.generated.resources.ic_visibility_off
import dz.shared.generated.resources.ic_visibility_on
import org.jetbrains.compose.resources.painterResource

@Composable
fun UniversalInputField(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    leftIcon: Painter,
    modifier: Modifier = Modifier,
    inputType: InputKind = InputKind.Text,
    borderColor: Color = MaterialTheme.colorScheme.outline,
    borderWidth: Dp = 1.dp,
    rightIcon: Painter? = null,
    onRightIconClick: (() -> Unit)? = null
) {

    var passwordVisible by remember { mutableStateOf(false) }

    val keyboardOptions = when (inputType) {
        InputKind.Text -> KeyboardOptions.Default
        InputKind.Email -> KeyboardOptions(keyboardType = KeyboardType.Email)
        InputKind.Phone -> KeyboardOptions(keyboardType = KeyboardType.Phone)
        InputKind.Number -> KeyboardOptions(keyboardType = KeyboardType.Number)
        InputKind.Password -> KeyboardOptions(keyboardType = KeyboardType.Password)
    }

    val visualTransformation =
        if (inputType == InputKind.Password && !passwordVisible)
            PasswordVisualTransformation()
        else
            VisualTransformation.None

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(
                width = borderWidth,
                color = borderColor,
                shape = RoundedCornerShape(20.dp)
            ),
        contentAlignment = Alignment.CenterStart
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    painter = leftIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )

                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    textStyle = LocalTextStyle.current.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 16.sp
                    ),
                    keyboardOptions = keyboardOptions,
                    visualTransformation = visualTransformation,
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier.fillMaxHeight(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (value.isEmpty()) {
                                Text(
                                    text = hint,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                    fontSize = 16.sp
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }

            if (inputType == InputKind.Password) {
                Icon(
                    painter = painterResource(
                        if (passwordVisible) Res.drawable.ic_visibility_on else Res.drawable.ic_visibility_off
                    ),
                    contentDescription = "Toggle password visibility",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { passwordVisible = !passwordVisible }
                )
            } else if (rightIcon != null) {
                Icon(
                    painter = rightIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onRightIconClick?.invoke() }
                )
            }
        }
    }
}

enum class InputKind {
    Text,
    Email,
    Phone,
    Number,
    Password
}

@Preview(showBackground = true)
@Composable
fun UniversalInputFieldPreview() {
    DZTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            UniversalInputField(
                value = "",
                onValueChange = {},
                hint = "Email or Phone number",
                leftIcon = painterResource(Res.drawable.ic_email)
            )

            UniversalInputField(
                value = "",
                onValueChange = {},
                hint = "Enter your password",
                leftIcon = painterResource(Res.drawable.ic_password),
                inputType = InputKind.Password
            )

            UniversalInputField(
                value = "",
                onValueChange = {},
                hint = "Enter email",
                leftIcon = painterResource(Res.drawable.ic_email),
                inputType = InputKind.Email
            )
        }
    }
}
