package com.example.dz.designsystem.modifiers

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.SuspendingPointerInputModifierNode
import androidx.compose.ui.node.CompositionLocalConsumerModifierNode
import androidx.compose.ui.node.DelegatingNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.currentValueOf
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.platform.LocalFocusManager

/**
 * Puts the keyboard away when the user taps somewhere that doesn't take the tap itself: empty
 * background, a heading, a card. Compose doesn't do this on its own, on either platform.
 *
 * Buttons, rows and text fields take their taps, so those are left alone and tapping from one
 * field into another still just moves the cursor. Scrolls and drags don't count as taps. Nothing is
 * consumed, so whatever sits underneath behaves exactly as before.
 *
 * App puts this on the root, which covers every screen. Sheets and dialogs are separate windows, so
 * a sheet with a text field needs it on its own content.
 */
fun Modifier.hideKeyboardOnBackgroundTap(): Modifier = this then HideKeyboardOnBackgroundTapElement

private data object HideKeyboardOnBackgroundTapElement : ModifierNodeElement<HideKeyboardOnBackgroundTapNode>() {
    override fun create() = HideKeyboardOnBackgroundTapNode()
    override fun update(node: HideKeyboardOnBackgroundTapNode) = Unit
    override fun InspectorInfo.inspectableProperties() {
        name = "hideKeyboardOnBackgroundTap"
    }
}

private class HideKeyboardOnBackgroundTapNode : DelegatingNode(), CompositionLocalConsumerModifierNode {
    init {
        delegate(
            SuspendingPointerInputModifierNode {
                awaitEachGesture {
                    awaitFirstDown(requireUnconsumed = true)
                    // Null when something underneath took the touch (a button, a field, a scroll).
                    waitForUpOrCancellation() ?: return@awaitEachGesture
                    currentValueOf(LocalFocusManager).clearFocus()
                }
            },
        )
    }
}
