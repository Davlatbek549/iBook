package com.example.dz.presentation.browse

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.icons.OrganicIcons
import com.example.dz.designsystem.components.organic.ORGANIC_GUTTER
import com.example.dz.designsystem.components.organic.ORGANIC_TAB_BAR_CLEARANCE
import com.example.dz.designsystem.components.organic.OrganicCircleIconButton
import com.example.dz.designsystem.components.organic.OrganicFilterPill
import com.example.dz.designsystem.components.organic.OrganicKicker
import com.example.dz.designsystem.components.organic.OrganicScreen
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.OrganicShape
import com.example.dz.designsystem.theme.organicBodyFontFamily
import com.example.dz.designsystem.theme.organicHeadingFontFamily
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.browse_all_categories
import dz.shared.generated.resources.browse_category_count
import dz.shared.generated.resources.browse_mood_cosy
import dz.shared.generated.resources.browse_mood_epic
import dz.shared.generated.resources.browse_mood_label
import dz.shared.generated.resources.browse_mood_short
import dz.shared.generated.resources.browse_mood_unsettling
import dz.shared.generated.resources.browse_search_hint
import dz.shared.generated.resources.browse_sort_az
import dz.shared.generated.resources.browse_title
import org.jetbrains.compose.resources.stringResource

private val tileTints = listOf(
    OrganicColors.accent200 to OrganicColors.accent300,
    OrganicColors.accent2_200 to OrganicColors.accent2_300,
    OrganicColors.neutral200 to OrganicColors.neutral400,
    OrganicColors.accent100 to OrganicColors.accent300,
    OrganicColors.accent2_100 to OrganicColors.accent2_400,
    OrganicColors.neutral300 to OrganicColors.neutral400,
)

/**
 * Browse — the handoff's Categories screen (`#scr-categories`): search entry, mood chips, and a
 * category tile grid. Real categories and per-category counts (one more real network call each,
 * same source the category-detail list already uses); moods are the mock's own labels with no
 * real filtering behind them, so a tap only changes which chip is highlighted.
 */
@Composable
fun BrowseScreen(
    uiState: BrowseUiState = BrowseUiState(),
    onBackClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onMoodClick: (Int) -> Unit = {},
    onSortClick: () -> Unit = {},
    onCategoryClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val moods = listOf(
        stringResource(Res.string.browse_mood_cosy),
        stringResource(Res.string.browse_mood_unsettling),
        stringResource(Res.string.browse_mood_short),
        stringResource(Res.string.browse_mood_epic),
    )

    OrganicScreen(modifier = modifier) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(top = 16.dp, bottom = ORGANIC_TAB_BAR_CLEARANCE),
            verticalArrangement = Arrangement.spacedBy(18.dp)
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
                        onClick = onBackClick,
                        contentDescription = "Back",
                        size = 38.dp
                    )
                    Text(
                        text = stringResource(Res.string.browse_title),
                        fontFamily = organicHeadingFontFamily(),
                        fontWeight = FontWeight.Normal,
                        fontSize = 30.sp,
                        color = OrganicColors.text
                    )
                }
            }

            item(key = "search") {
                Row(
                    modifier = Modifier
                        .padding(horizontal = ORGANIC_GUTTER)
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(RoundedCornerShape(OrganicShape.pill))
                        .background(OrganicColors.neutral200)
                        .clickable(role = Role.Button, onClick = onSearchClick)
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = OrganicIcons.Search,
                        contentDescription = null,
                        tint = OrganicColors.neutral600,
                        modifier = Modifier.size(17.dp)
                    )
                    Text(
                        text = stringResource(Res.string.browse_search_hint),
                        fontFamily = organicBodyFontFamily(),
                        fontSize = 14.sp,
                        color = OrganicColors.neutral600
                    )
                }
            }

            item(key = "moods") {
                Column(
                    modifier = Modifier.padding(horizontal = ORGANIC_GUTTER),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OrganicKicker(text = stringResource(Res.string.browse_mood_label), color = OrganicColors.neutral600)
                    Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                        moods.forEachIndexed { index, mood ->
                            OrganicFilterPill(
                                label = mood,
                                selected = index == uiState.selectedMoodIndex,
                                onClick = { onMoodClick(index) }
                            )
                        }
                    }
                }
            }

            item(key = "categories-header") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ORGANIC_GUTTER),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    OrganicKicker(text = stringResource(Res.string.browse_all_categories), color = OrganicColors.neutral600)
                    Text(
                        text = stringResource(Res.string.browse_sort_az),
                        modifier = Modifier.clickable(role = Role.Button, onClick = onSortClick),
                        fontFamily = organicBodyFontFamily(),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = OrganicColors.accent700
                    )
                }
            }

            uiState.categories.chunked(2).forEachIndexed { rowIndex, row ->
                item(key = "row-$rowIndex") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = ORGANIC_GUTTER),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        row.forEachIndexed { colIndex, category ->
                            val tintIndex = rowIndex * 2 + colIndex
                            CategoryTile(
                                category = category,
                                background = tileTints[tintIndex % tileTints.size].first,
                                accent = tileTints[tintIndex % tileTints.size].second,
                                onClick = { onCategoryClick(category.id) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (row.size == 1) {
                            Box(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryTile(
    category: BrowseCategoryUi,
    background: Color,
    accent: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .height(104.dp)
            .clip(RoundedCornerShape(OrganicShape.radiusLg))
            .background(background)
            .clickable(role = Role.Button, onClick = onClick)
    ) {
        // Decorative — a soft block rather than a bespoke per-category illustration, since real
        // category names won't match the mock's hand-picked set (Gothic, Nature, …).
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 14.dp, end = 14.dp)
                .size(width = 26.dp, height = 38.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(accent)
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = category.name,
                fontFamily = organicHeadingFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 19.sp,
                lineHeight = 21.sp,
                color = OrganicColors.text
            )
            Text(
                text = stringResource(Res.string.browse_category_count, category.bookCount),
                fontFamily = organicBodyFontFamily(),
                fontSize = 12.sp,
                color = OrganicColors.neutral700
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 900)
@Composable
private fun BrowseScreenPreview() {
    BrowseScreen(
        uiState = BrowseUiState(
            categories = listOf(
                BrowseCategoryUi("fiction", "Fiction", 2140),
                BrowseCategoryUi("fantasy", "Fantasy", 1308),
                BrowseCategoryUi("history", "History", 3776),
                BrowseCategoryUi("biography", "Biography", 2902),
            )
        )
    )
}
