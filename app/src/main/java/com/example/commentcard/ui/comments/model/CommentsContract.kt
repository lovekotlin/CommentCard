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
     * @property commentIdForImageUpdate The ID of the comment currently being updated, used to preserve state during config changes.
     */
    data class State(
        val isLoading: Boolean = false,
        val comments: List<CommentUIModel> = emptyList(),
        val error: String? = null,
        val commentIdForImageUpdate: Int? = null
    )

    /**
     * Represents the events (user actions or intents) that can be triggered from the UI.
     */
    sealed class Event {
        /**
         * Event triggered when the user clicks the profile image to initiate an update.
         *
         * @param commentId The ID of the comment whose image is to be changed.
         */
        data class OnProfileImageClicked(val commentId: Int) : Event()

        /**
         * Event triggered when the user selects a new profile image for a comment.
         *
         * @param imageUri The URI of the newly selected image.
         */
        data class OnProfileImageSelected(val imageUri: Uri) : Event()

        /**
         * Event triggered when the user wants to retry fetching comments.
         */
        data object Retry : Event()
    }
}