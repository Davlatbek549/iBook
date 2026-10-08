package com.example.dz.presentation.book.review

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.organic.OrganicField
import com.example.dz.designsystem.components.organic.OrganicPrimaryButton
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.OrganicShape
import com.example.dz.designsystem.theme.organicHeadingFontFamily
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.reviews_note_hint
import dz.shared.generated.resources.reviews_save
import dz.shared.generated.resources.reviews_your_review
import org.jetbrains.compose.resources.stringResource

/**
 * Writing the review, over the screen it belongs to.
 *
 * A sheet rather than a screen of its own, for the same reason the reader's type sheet is one:
 * what is being written belongs to the book behind it, and a pushed screen would lose that.
 *
 * The score is the only required part. Someone who gives four stars and nothing else has still
 * said something; making them write a sentence to be counted would lose the four stars.
 */
@Composable
internal fun ReviewEditorSheet(
    editor: ReviewEditorUiState,
    onEvent: (BookReviewEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(OrganicColors.scrim.copy(alpha = 0.38f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ) { onEvent(BookReviewEvent.EditorDismissed) }
        )

        Column(
            modifier = modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                    ambientColor = OrganicColors.shadow.copy(alpha = 0.22f),
                    spotColor = OrganicColors.shadow.copy(alpha = 0.22f)
                )
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(OrganicColors.bg)
                // The note field is the whole point of the sheet, so the keyboard must not cover it.
                .imePadding()
                .navigationBarsPadding()
                .padding(start = 28.dp, end = 28.dp, top = 20.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(46.dp)
                    .height(5.dp)
                    .clip(RoundedCornerShape(OrganicShape.pill))
                    .background(OrganicColors.neutral400)
            )

            Text(
                text = stringResource(Res.string.reviews_your_review),
                fontFamily = organicHeadingFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 22.sp,
                color = OrganicColors.text
            )

            StarPicker(
                stars = editor.stars,
                onPick = { onEvent(BookReviewEvent.EditorStarsChanged(it)) },
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )

            OrganicField(
                value = editor.note,
                onValueChange = { onEvent(BookReviewEvent.EditorNoteChanged(it)) },
                placeholder = stringResource(Res.string.reviews_note_hint),
                singleLine = false,
                minHeight = 104.dp,
            )

            // Greyed rather than hidden: the button is what the sheet is for, and a reader who
            // taps it should find out that a score is missing, not that it moved.
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(if (editor.canSave) 1f else 0.45f)
            ) {
                OrganicPrimaryButton(
                    text = stringResource(Res.string.reviews_save),
                    onClick = { if (editor.canSave) onEvent(BookReviewEvent.EditorSaved) },
                )
            }
        }
    }
}
