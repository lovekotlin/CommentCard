package com.example.commentcard.ui.comments.model

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.commentcard.data.remote.APIException
import com.example.commentcard.data.remote.DataParsingException
import com.example.commentcard.data.remote.NoConnectivityException
import com.example.commentcard.data.remote.TimeoutException
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

            CommentsContract.Event.Retry -> fetchComments()
        }
    }

    /**
     * Fetches comments from the repository and updates the UI state accordingly.
     */
    private fun fetchComments() {
        _uiState.update { it.copy(isLoading = true, error = null) }

        repository.getComments()
            .onEach { result ->
                result.onSuccess { comments ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            comments = comments.toUIModel()
                        )
                    }
                }.onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = when (error) {
                                is NoConnectivityException ->
                                    "No internet connection. Please check your network and try again."

                                is TimeoutException ->
                                    "The connection timed out. Please try again."

                                is DataParsingException ->
                                    "An error occurred while processing data. This may be a temporary issue."

                                is APIException ->
                                    // Handle specific HTTP API error codes
                                    when (error.code) {
                                        404 -> "The requested content could not be found."
                                        401, 403 -> "You don't have permission to access this."
                                        in 500..599 -> "The server is currently unavailable. Please try again later."
                                        else -> "Could not retrieve data from the server (Error ${error.code})."
                                    }

                                else ->
                                    "An unexpected error occurred. Please try again."
                            }
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