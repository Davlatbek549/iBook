package com.example.dz.designsystem.components.organic

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.icons.OrganicIcons
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.OrganicShape
import com.example.dz.designsystem.theme.organicBodyFontFamily

/**
 * The two ways into a search: the live field on Search, and the bar on Browse that only looks like
 * one and takes the reader there.
 *
 * Geometry from `dz-all-screens.html` (`#scr-search`, `#scr-categories`).
 */

/**
 * The query field on Search: a 56dp pill on neutral-100, a magnifier, and the typed text at 15sp.
 *
 * Like [OrganicField] it rests on a neutral-300 hairline that reads as part of the fill, and takes
 * the terracotta edge the frame draws once it has focus. The keyboard's action key says Search and
 * calls [onSearch], which is how a reader says "this is the query I meant" rather than a prefix.
 */
@Composable
fun OrganicSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester? = null,
    onSearch: () -> Unit = {},
) {
    var focused by remember { mutableStateOf(false) }
    val shape = RoundedCornerShape(OrganicShape.pill)
    val body = organicBodyFontFamily()

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(shape)
            .background(OrganicColors.neutral100)
            .border(1.dp, if (focused) OrganicColors.accent else OrganicColors.neutral300, shape)
            .then(if (focusRequester != null) Modifier.focusRequester(focusRequester) else Modifier)
            .onFocusChanged { focused = it.isFocused },
        textStyle = TextStyle(
            fontFamily = body,
            fontSize = 15.sp,
            color = OrganicColors.text
        ),
        singleLine = true,
        cursorBrush = SolidColor(OrganicColors.accent),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch() }),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier.padding(horizontal = 22.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = OrganicIcons.Search,
                    contentDescription = null,
                    tint = OrganicColors.neutral700,
                    modifier = Modifier.size(18.dp)
                )
                Box(modifier = Modifier.weight(1f)) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            fontFamily = body,
                            fontSize = 15.sp,
                            color = OrganicColors.neutral500,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    innerTextField()
                }
            }
        }
    )
}

/**
 * The search bar on Browse: a 50dp neutral-200 pill with the magnifier and a hint, and no text
 * field inside it. Typing happens on Search, where the results are, so this is a button — a real
 * field here would raise a keyboard over a grid with nowhere to show what it found.
 */
@Composable
fun OrganicSearchBar(
    placeholder: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(OrganicShape.pill))
            .background(OrganicColors.neutral200)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = OrganicIcons.Search,
            contentDescription = null,
            tint = OrganicColors.neutral600,
            modifier = Modifier.size(17.dp)
        )
        Text(
            text = placeholder,
            fontFamily = organicBodyFontFamily(),
            fontSize = 14.sp,
            color = OrganicColors.neutral600,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
