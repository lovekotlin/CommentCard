package com.example.commentcard.ui.comments.model

import android.net.Uri

/**
 * Defines the contract between the View and the ViewModel for the comments screen.
 */
class CommentsContract {

    /**
     * Represents the immutable state of the comments screen.
     *
     * @property isLoading True if comments are currently being fetched, false otherwise.
     * @property comments The list of comments to display.
     * @property error A descriptive error message if fetching fails, null otherwise.
     */
    data class State(
        val isLoading: Boolean = false,
        val comments: List<CommentUIModel> = emptyList(),
        val error: String? = null
    )

    /**
     * Represents the events (user actions or intents) that can be triggered from the UI.
     */
    sealed interface Event {
        /**
         * Event triggered when the user selects a new profile image for a comment.
         * @param commentId The ID of the comment being updated.
         * @param imageUri The URI of the newly selected image.
         */
        data class OnImageSelected(val commentId: Int, val imageUri: Uri) : Event
    }
}