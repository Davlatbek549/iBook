package com.example.ibook.app_components.inputs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ibook.ui.theme.IBookTheme

@Composable
fun UniversalInputField(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    leftIcon: ImageVector,
    modifier: Modifier = Modifier,
    rightIcon: ImageVector? = null,
    inputType: InputKind = InputKind.Text,
    isEditable: Boolean = true,
    onClick: (() -> Unit)? = null
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
            .then(
                if (!isEditable && onClick != null)
                    Modifier.clickable { onClick() }
                else Modifier
            ),
        contentAlignment = Alignment.CenterStart
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = leftIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(12.dp))

            if (isEditable) {

                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    singleLine = true,
                    keyboardOptions = keyboardOptions,
                    visualTransformation = visualTransformation,
                    modifier = Modifier.weight(1f),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    decorationBox = { innerTextField ->

                        if (value.isEmpty()) {
                            Text(
                                text = hint,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        innerTextField()
                    }
                )

            } else {

                Text(
                    text = value.ifEmpty { hint },
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (value.isEmpty())
                        MaterialTheme.colorScheme.onSurfaceVariant
                    else
                        MaterialTheme.colorScheme.onSurface
                )
            }

            if (inputType == InputKind.Password) {

                IconButton(
                    onClick = { passwordVisible = !passwordVisible }
                ) {

                    Icon(
                        imageVector = if (passwordVisible)
                            Icons.Filled.Visibility
                        else
                            Icons.Filled.VisibilityOff,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .padding(start = 15.dp)
                    )
                }

            } else if (rightIcon != null) {

                Icon(
                    imageVector = rightIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .then(
                            if (onClick != null)
                                Modifier.clickable { onClick() }
                            else Modifier
                        )
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

    IBookTheme {

        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var dateOfBirth by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            UniversalInputField(
                value = email,
                onValueChange = { email = it },
                hint = "Email or Phone number",
                leftIcon = Icons.Filled.Person,
                inputType = InputKind.Email
            )

            UniversalInputField(
                value = password,
                onValueChange = { password = it },
                hint = "Password",
                leftIcon = Icons.Filled.Lock,
                inputType = InputKind.Password
            )

            UniversalInputField(
                value = dateOfBirth,
                onValueChange = {},
                hint = "Day of birth",
                leftIcon = Icons.Filled.DateRange,
                rightIcon = Icons.Filled.CheckCircle,
                isEditable = false
            )
        }
    }
}