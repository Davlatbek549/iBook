package com.example.dz.presentation.collections.edit

sealed interface CollectionsEditEvent {
    data object BackClicked : CollectionsEditEvent
    data object SaveClicked : CollectionsEditEvent
    data object DeleteClicked : CollectionsEditEvent
    data class NameChanged(val value: String) : CollectionsEditEvent
    data class DescriptionChanged(val value: String) : CollectionsEditEvent
    data class VisibilityChanged(val value: Boolean) : CollectionsEditEvent
    data class ColorSelected(val index: Int) : CollectionsEditEvent
    data class BookRemoved(val bookId: String) : CollectionsEditEvent

    /** Opens the picker, and loads the shelf the first time it is asked for. */
    data object AddBooksClicked : CollectionsEditEvent
    data object PickerDismissed : CollectionsEditEvent

    /**
     * One row in the picker. Toggling rather than adding, so the same sheet takes a book off the
     * shelf as puts it on — a reader who opened the picker to fix a mistake should not have to
     * close it again to undo one.
     */
    data class PickerBookToggled(val bookId: String) : CollectionsEditEvent
}
