package com.example.dz.presentation.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.icons.InkIcons
import com.example.dz.designsystem.components.ink.InkField
import com.example.dz.designsystem.components.ink.InkLabel
import com.example.dz.designsystem.components.ink.InkSectionTitle
import com.example.dz.designsystem.components.ink.inkCard
import com.example.dz.designsystem.theme.InkColors
import com.example.dz.designsystem.theme.inkBodyFontFamily
import com.example.dz.designsystem.theme.inkColors
import com.example.dz.designsystem.theme.inkDisplayFontFamily
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.search_browse_by_mood
import dz.shared.generated.resources.search_placeholder
import dz.shared.generated.resources.search_recent
import dz.shared.generated.resources.search_title
import org.jetbrains.compose.resources.stringResource

private val moodCategories = listOf(
    "Literary fiction", "History", "Romance", "Essays",
    "Poetry", "Biography", "Fantasy", "Health"
)

@Composable
fun SearchScreen(
    onSearchFocusChange: (Boolean) -> Unit = {},
    onCategoryClick: (String) -> Unit = {},
    onBookClick: (bookId: String) -> Unit = {},
    onAuthorClick: (authorId: String) -> Unit = {}
) {
    val colors = inkColors()
    val displayFont = inkDisplayFontFamily()
    val bodyFont = inkBodyFontFamily()

    var query by remember { mutableStateOf("") }
    val recentSearches = remember {
        listOf("olive again", "paulo coelho", "gothic novels").toMutableStateList()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.paper)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 96.dp)
    ) {
        Column(modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 6.dp)) {
            Text(
                text = stringResource(Res.string.search_title),
                fontFamily = displayFont,
                fontWeight = FontWeight.Medium,
                fontSize = 24.sp,
                color = colors.ink
            )
            InkField(
                value = query,
                onValueChange = { query = it },
                placeholder = stringResource(Res.string.search_placeholder),
                modifier = Modifier
                    .padding(top = 16.dp)
                    .onFocusChanged { onSearchFocusChange(it.isFocused) },
                leadingIcon = InkIcons.Search,
                colors = colors
            )
        }

        if (recentSearches.isNotEmpty()) {
            Column(modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 24.dp)) {
                InkLabel(text = stringResource(Res.string.search_recent), colors = colors)
                Column(modifier = Modifier.padding(top = 6.dp)) {
                    recentSearches.forEachIndexed { i, recent ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { query = recent }
                                .padding(vertical = 11.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = InkIcons.Search,
                                contentDescription = null,
                                tint = colors.muted,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = recent,
                                modifier = Modifier.weight(1f),
                                fontFamily = bodyFont,
                                fontSize = 13.5.sp,
                                color = colors.inkSoft
                            )
                            Icon(
                                imageVector = InkIcons.Close,
                                contentDescription = null,
                                tint = colors.muted,
                                modifier = Modifier
                                    .size(11.dp)
                                    .clickable { recentSearches.remove(recent) }
                            )
                        }
                        if (i < recentSearches.size - 1) {
                            HorizontalDivider(thickness = 1.dp, color = colors.line)
                        }
                    }
                }
            }
        }

        Column(modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 22.dp)) {
            InkSectionTitle(
                text = stringResource(Res.string.search_browse_by_mood),
                colors = colors
            )
            Column(
                modifier = Modifier.padding(top = 14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                moodCategories.chunked(2).forEachIndexed { rowIndex, rowCats ->
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        rowCats.forEachIndexed { colIndex, category ->
                            val index = rowIndex * 2 + colIndex
                            MoodCard(
                                number = index + 1,
                                name = category,
                                highlighted = index % 3 == 0,
                                onClick = { onCategoryClick(category) },
                                modifier = Modifier.weight(1f),
                                colors = colors
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MoodCard(
    number: Int,
    name: String,
    highlighted: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: InkColors,
) {
    Column(
        modifier = modifier
            .inkCard(colors)
            .let { if (highlighted) it.background(colors.alt) else it }
            .clickable(onClick = onClick)
            .padding(horizontal = 15.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp)
    ) {
        Text(
            text = number.toString().padStart(2, '0'),
            fontFamily = inkDisplayFontFamily(),
            fontStyle = FontStyle.Italic,
            fontSize = 11.sp,
            color = colors.muted
        )
        Text(
            text = name,
            fontFamily = inkDisplayFontFamily(),
            fontWeight = FontWeight.Medium,
            fontSize = 15.sp,
            lineHeight = 18.sp,
            color = colors.ink
        )
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
fun SearchScreenPreview() {
    SearchScreen()
}
