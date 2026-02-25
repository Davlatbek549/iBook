package com.example.ibook.app_components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
            .background(Color(0xFFF6F6F7))
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
                tint = Color(0xFF6C63FF)
            )

            Spacer(modifier = Modifier.width(12.dp))

            if (isEditable) {
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    enabled = isEditable,
                    singleLine = true,
                    keyboardOptions = keyboardOptions,
                    visualTransformation = visualTransformation,
                    modifier = Modifier.weight(1f),
                    textStyle = androidx.compose.ui.text.TextStyle(
                        fontSize = 16.sp,
                        color = Color.Black
                    ),
                    decorationBox = { innerTextField ->
                        if (value.isEmpty()) {
                            Text(
                                text = hint,
                                color = Color.Gray,
                                fontSize = 16.sp
                            )
                        }
                        innerTextField()
                    }
                )
            } else {
                Text(
                    text = if (value.isEmpty()) hint else value,
                    modifier = Modifier.weight(1f),
                    fontSize = 16.sp,
                    color = if (value.isEmpty()) Color.Gray else Color.Black
                )
            }

            if (inputType == InputKind.Password) {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible)
                            Icons.Default.KeyboardArrowDown
                        else
                            Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            } else if (rightIcon != null) {
                Icon(
                    imageVector = rightIcon,
                    contentDescription = null,
                    tint = Color.Gray,
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
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()                      // <-- important
            .background(Color(0xFF2E2E2E))      // <-- your screen background
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        UniversalInputField(
            value = email,
            onValueChange = { email = it },
            hint = "Email or Phone number",
            leftIcon = Icons.Default.Person,
            inputType = InputKind.Email
        )

        UniversalInputField(
            value = password,
            onValueChange = { password = it },
            hint = "Password",
            leftIcon = Icons.Default.Lock,
            rightIcon = Icons.Default.AddCircle,
            inputType = InputKind.Password
        )
        var dateOfBirth by remember { mutableStateOf("") }

        UniversalInputField(
            value = dateOfBirth,
            onValueChange = {},
            hint = "Day of birth",
            leftIcon = Icons.Default.DateRange,
            rightIcon = Icons.Default.CheckCircle,
            isEditable = false,
            onClick = { /* open date picker */ }
        )
    }

}