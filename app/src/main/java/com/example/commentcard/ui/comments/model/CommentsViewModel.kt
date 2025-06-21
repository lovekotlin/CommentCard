package com.example.commentcard.ui.comments.model

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.commentcard.R
import com.example.commentcard.data.model.StringResource
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
import kotlinx.coroutines.launch
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
        viewModelScope.launch {
            when (event) {
                is CommentsContract.Event.OnProfileImageClicked ->
                    _uiState.update { it.copy(commentIdForImageUpdate = event.commentId) }

                is CommentsContract.Event.OnProfileImageSelected ->
                    updateProfileImage(event.imageUri)

                CommentsContract.Event.Retry -> fetchComments()
            }
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
                                    StringResource(R.string.error_no_connection)

                                is TimeoutException ->
                                    StringResource(R.string.error_timeout)

                                is DataParsingException ->
                                    StringResource(R.string.error_data_parsing)

                                is APIException ->
                                    // Handle specific HTTP API error codes
                                    when (error.code) {
                                        404 -> StringResource(R.string.error_not_found)
                                        401, 403 -> StringResource(R.string.error_permission)
                                        in 500..599 -> StringResource(R.string.error_server)
                                        else -> StringResource(
                                            R.string.error_generic_with_code,
                                            listOf(error.code)
                                        )
                                    }

                                else ->
                                    StringResource(R.string.error_unexpected)
                            }
                        )
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    /**
     * Updates the profile image for a specific comment in the UI state.
     *
     * @param imageUri The new image URI.
     */
    private fun updateProfileImage(imageUri: Uri) {
        val commentIdToUpdate = _uiState.value.commentIdForImageUpdate ?: return
        _uiState.update { currentState ->
            val updatedComments = currentState.comments.map { comment ->
                if (comment.id == commentIdToUpdate) {
                    comment.copy(profileImageUri = imageUri)
                } else {
                    comment
                }
            }
            currentState.copy(comments = updatedComments, commentIdForImageUpdate = null)
        }
    }
}