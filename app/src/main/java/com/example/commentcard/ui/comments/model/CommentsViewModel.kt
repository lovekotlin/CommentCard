package com.example.commentcard.ui.comments.model

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.commentcard.data.repository.CommentsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * The ViewModel responsible for fetching comment data, managing the screen's state, and handling user interactions.
 *
 * @param repository The repository for fetching comment data.
 */
@HiltViewModel
class CommentsViewModel @Inject constructor(
    private val repository: CommentsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CommentsContract.State())
    val uiState: StateFlow<CommentsContract.State> = _uiState.asStateFlow()

    init {
        fetchComments()
    }

    /**
     * Processes events sent from the UI.
     * @param event The event to process.
     */
    fun onEvent(event: CommentsContract.Event) {
        when (event) {
            is CommentsContract.Event.OnImageSelected ->
                updateProfileImage(event.commentId, event.imageUri)
        }
    }

    /**
     * Fetches comments from the repository and updates the UI state accordingly.
     */
    private fun fetchComments() {
        _uiState.update { it.copy(isLoading = true) }

        repository.getComments()
            .onEach { result ->
                result.onSuccess { comments ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            comments = comments.toUIModel(),
                            error = null
                        )
                    }
                }.onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "An unknown error occurred."
                        )
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    /**
     * Updates the profile image for a specific comment in the UI state.
     * @param commentId The ID of the comment to update.
     * @param imageUri The new image URI.
     */
    private fun updateProfileImage(commentId: Int, imageUri: Uri) {
        _uiState.update { currentState ->
            val updatedComments = currentState.comments.map { comment ->
                if (comment.id == commentId) {
                    comment.copy(profileImageUri = imageUri)
                } else {
                    comment
                }
            }
            currentState.copy(comments = updatedComments)
        }
    }
}