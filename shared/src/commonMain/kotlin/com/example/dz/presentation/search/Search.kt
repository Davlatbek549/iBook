package com.example.dz.presentation.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.icons.OrganicIcons
import com.example.dz.designsystem.components.organic.ORGANIC_GUTTER
import com.example.dz.designsystem.components.organic.ORGANIC_TAB_BAR_CLEARANCE
import com.example.dz.designsystem.components.organic.OrganicBookCover
import com.example.dz.designsystem.components.organic.OrganicCircleIconButton
import com.example.dz.designsystem.components.organic.OrganicKicker
import com.example.dz.designsystem.components.organic.OrganicScreen
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.OrganicShape
import com.example.dz.designsystem.theme.organicBodyFontFamily
import com.example.dz.designsystem.theme.organicHeadingFontFamily
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.search_free
import dz.shared.generated.resources.search_in_library
import dz.shared.generated.resources.search_placeholder
import dz.shared.generated.resources.search_recent
import dz.shared.generated.resources.search_results_count
import dz.shared.generated.resources.search_title
import org.jetbrains.compose.resources.stringResource

/**
 * Search — query and results, in the Organic design system. Geometry from `#scr-search`.
 *
 * The old Ink screen mixed a category/mood grid into this same list; that grid is now Browse's
 * (reached one step earlier, from Home/Library's search circle), so this screen is just the field,
 * real recent-search chips, and results showing "In your library" or a price.
 */
@Composable
fun SearchScreen(
    uiState: SearchUiState = SearchUiState(),
    onEvent: (SearchEvent) -> Unit = {},
    modifier: Modifier = Modifier
) {
    OrganicScreen(modifier = modifier) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(top = 16.dp, bottom = ORGANIC_TAB_BAR_CLEARANCE),
            verticalArrangement = Arrangement.spacedBy(22.dp)
        ) {
            item(key = "header") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ORGANIC_GUTTER),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    OrganicCircleIconButton(
                        icon = OrganicIcons.ChevronLeft,
                        onClick = { onEvent(SearchEvent.BackClicked) },
                        contentDescription = "Back",
                        size = 38.dp
                    )
                    Text(
                        text = stringResource(Res.string.search_title),
                        fontFamily = organicHeadingFontFamily(),
                        fontWeight = FontWeight.Normal,
                        fontSize = 30.sp,
                        color = OrganicColors.text
                    )
                }
            }

            item(key = "field") {
                SearchField(
                    value = uiState.query,
                    onValueChange = { onEvent(SearchEvent.QueryChanged(it)) },
                    onSubmit = { onEvent(SearchEvent.SearchClicked) },
                    modifier = Modifier.padding(horizontal = ORGANIC_GUTTER)
                )
            }

            if (uiState.recentSearches.isNotEmpty()) {
                item(key = "recent") {
                    Column(
                        modifier = Modifier.padding(horizontal = ORGANIC_GUTTER),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OrganicKicker(text = stringResource(Res.string.search_recent), color = OrganicColors.neutral700)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            uiState.recentSearches.forEach { recent ->
                                Text(
                                    text = recent,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(OrganicShape.pill))
                                        .background(OrganicColors.neutral200)
                                        .clickable(role = Role.Button) {
                                            onEvent(SearchEvent.RecentSearchClicked(recent))
                                        }
                                        .padding(horizontal = 16.dp, vertical = 9.dp),
                                    fontFamily = organicBodyFontFamily(),
                                    fontSize = 13.sp,
                                    color = OrganicColors.text
                                )
                            }
                        }
                    }
                }
            }

            if (uiState.results.isNotEmpty()) {
                item(key = "results-count") {
                    OrganicKicker(
                        text = stringResource(Res.string.search_results_count, uiState.results.size),
                        color = OrganicColors.neutral700,
                        modifier = Modifier.padding(horizontal = ORGANIC_GUTTER)
                    )
                }
                itemsIndexed(items = uiState.results, key = { _, result -> result.id }) { _, result ->
                    SearchResultRow(
                        result = result,
                        onClick = { onEvent(SearchEvent.BookClicked(result.id)) },
                        modifier = Modifier.padding(horizontal = ORGANIC_GUTTER)
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchField(
    value: String,
    onValueChange: (String) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(OrganicShape.pill))
            .background(OrganicColors.neutral100),
        textStyle = TextStyle(
            fontFamily = organicBodyFontFamily(),
            fontSize = 15.sp,
            color = OrganicColors.text
        ),
        singleLine = true,
        cursorBrush = SolidColor(OrganicColors.accent),
        keyboardActions = KeyboardActions(onSearch = { onSubmit() }),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 22.dp),
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
                            text = stringResource(Res.string.search_placeholder),
                            fontFamily = organicBodyFontFamily(),
                            fontSize = 15.sp,
                            color = OrganicColors.neutral600
                        )
                    }
                    innerTextField()
                }
            }
        }
    )
}

@Composable
private fun SearchResultRow(
    result: SearchResultUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(role = Role.Button, onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        OrganicBookCover(title = result.title, coverUrl = result.coverUrl, width = 52.dp, height = 76.dp, cornerRadius = 10.dp)

        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(
                text = result.title,
                fontFamily = organicHeadingFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 17.sp,
                lineHeight = 19.sp,
                color = OrganicColors.text
            )
            Text(
                text = result.author,
                fontFamily = organicBodyFontFamily(),
                fontSize = 12.sp,
                color = OrganicColors.neutral700
            )
            Text(
                text = when {
                    result.inLibrary -> stringResource(Res.string.search_in_library)
                    result.price != null -> result.price
                    result.isFree -> stringResource(Res.string.search_free)
                    else -> ""
                },
                fontFamily = organicBodyFontFamily(),
                fontSize = 12.sp,
                color = OrganicColors.accent700
            )
        }

        Icon(
            imageVector = OrganicIcons.ChevronRight,
            contentDescription = null,
            tint = OrganicColors.neutral600,
            modifier = Modifier.size(15.dp)
        )
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
private fun SearchScreenPreview() {
    SearchScreen(
        uiState = SearchUiState(
            query = "gothic",
            recentSearches = listOf("Strout", "short stories", "audio"),
            results = listOf(
                SearchResultUi("1", "Mexican Gothic", "Silvia Moreno-Garcia", null, inLibrary = true, isFree = false, price = null),
                SearchResultUi("2", "The Hacienda", "Isabel Cañas", null, inLibrary = false, isFree = false, price = "$10.99"),
            )
        )
    )
}
