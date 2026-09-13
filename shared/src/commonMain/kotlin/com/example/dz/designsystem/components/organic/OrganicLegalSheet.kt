package com.example.dz.designsystem.components.organic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.core.legal.LegalBlock
import com.example.dz.core.legal.LegalDocument
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.OrganicShape
import com.example.dz.designsystem.theme.organicBodyFontFamily
import com.example.dz.designsystem.theme.organicHeadingFontFamily

/**
 * A legal document in a bottom sheet, with the agree button withheld until the reader has
 * actually reached the end of it.
 *
 * The gate is a weak signal — most people scroll without reading — but it does two useful things:
 * it stops someone agreeing to a document they never opened, and it gives you a record that the
 * text was at least put in front of them.
 *
 * Two failure modes it deliberately avoids. A document short enough not to scroll would otherwise
 * leave the button disabled forever, so an unscrollable body counts as read. And once the end has
 * been reached the gate stays open — scrolling back up to re-read a paragraph must not take the
 * button away again.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrganicLegalSheet(
    document: LegalDocument,
    onAgree: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    agreeLabel: String,
    keepReadingLabel: String,
) {
    // `skipPartiallyExpanded = true` became a set of the states the sheet may take: leaving
    // PartiallyExpanded out is what stops consent being agreed to from a half-read sheet. Passed
    // positionally because the parameter carries no name in this alpha's signature.
    val allowedSheetValues = setOf(SheetValue.Hidden, SheetValue.Expanded)
    val sheetState = rememberBottomSheetState(SheetValue.Hidden, allowedSheetValues)
    val scrollState = rememberScrollState()
    val heading = organicHeadingFontFamily()
    val body = organicBodyFontFamily()

    var hasReachedEnd by remember(document) { mutableStateOf(false) }

    // maxValue is Int.MAX_VALUE until the body has been measured, so waiting for a real number
    // keeps a not-yet-laid-out sheet from being mistaken for one that fits on screen.
    val atEnd by remember {
        derivedStateOf {
            val max = scrollState.maxValue
            when {
                max == Int.MAX_VALUE -> false
                max == 0 -> true
                else -> scrollState.value >= max - END_THRESHOLD_PX
            }
        }
    }
    LaunchedEffect(atEnd) { if (atEnd) hasReachedEnd = true }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = OrganicColors.bg,
        scrimColor = OrganicColors.neutral900.copy(alpha = 0.42f),
        dragHandle = {
            Box(
                modifier = Modifier.fillMaxWidth().padding(top = 14.dp, bottom = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(38.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(OrganicShape.pill))
                        .background(OrganicColors.neutral300)
                )
            }
        },
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight(0.9f)
                .navigationBarsPadding()
                .padding(horizontal = OrganicSheetPadding)
        ) {
            Text(
                text = document.title,
                modifier = Modifier.padding(top = 6.dp, bottom = 4.dp),
                fontFamily = heading,
                fontSize = 26.sp,
                lineHeight = 30.sp,
                color = OrganicColors.text
            )
            Text(
                text = "Version ${document.version}",
                fontFamily = body,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp,
                color = OrganicColors.neutral600
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(top = 20.dp),
                verticalArrangement = Arrangement.spacedBy(22.dp)
            ) {
                document.sections.forEach { section ->
                    Column(verticalArrangement = Arrangement.spacedBy(9.dp)) {
                        Text(
                            text = section.heading.uppercase(),
                            fontFamily = body,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.3.sp,
                            color = OrganicColors.accent700
                        )
                        section.blocks.forEach { block ->
                            when (block) {
                                is LegalBlock.Paragraph -> Text(
                                    text = block.text,
                                    fontFamily = body,
                                    fontSize = 15.sp,
                                    lineHeight = 25.sp,
                                    color = OrganicColors.neutral700
                                )
                                is LegalBlock.Bullets -> Column(
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    block.items.forEach { item ->
                                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                            Box(
                                                modifier = Modifier
                                                    .padding(top = 9.dp)
                                                    .size(5.dp)
                                                    .clip(RoundedCornerShape(OrganicShape.pill))
                                                    .background(OrganicColors.accent)
                                            )
                                            Text(
                                                text = item,
                                                fontFamily = body,
                                                fontSize = 15.sp,
                                                lineHeight = 25.sp,
                                                color = OrganicColors.neutral700
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            OrganicButton(
                text = if (hasReachedEnd) agreeLabel else keepReadingLabel,
                onClick = onAgree,
                modifier = Modifier.padding(top = 16.dp, bottom = 20.dp),
                enabled = hasReachedEnd
            )
        }
    }
}

/** Matches the auth gutter, so the sheet's text sits on the same margin as the screen behind it. */
private val OrganicSheetPadding = 28.dp

/**
 * A few pixels of slack. Fractional scroll positions mean an exact equality with [maxValue] can
 * be missed by a pixel, leaving the button disabled at the visible bottom of the document.
 */
private const val END_THRESHOLD_PX = 12
