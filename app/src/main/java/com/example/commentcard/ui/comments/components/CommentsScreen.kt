package com.example.commentcard.ui.comments.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.commentcard.ui.comments.model.CommentsContract
import com.example.commentcard.ui.comments.model.CommentsViewModel

/**
 * The main screen that displays the list of comments.
 * It observes state from [CommentsViewModel] and handles user interactions.
 *
 * @param widthSizeClass The window width size class, used for adaptive layouts.
 * @param viewModel The ViewModel providing state and handling events.
 */
@Composable
fun CommentsScreen(
    modifier: Modifier = Modifier,
    widthSizeClass: WindowWidthSizeClass,
    viewModel: CommentsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var commentIdToUpdate by remember { mutableStateOf<Int?>(null) }
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            val id = commentIdToUpdate
            if (uri != null && id != null) {
                viewModel.onEvent(CommentsContract.Event.OnImageSelected(id, uri))
                commentIdToUpdate = null
            }
        }
    )

    Box {
        when {
            uiState.isLoading -> CircularProgressIndicator()
            uiState.error != null -> Text(text = uiState.error!!)
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(items = uiState.comments, key = { it.id }) { comment ->
                        CommentCard(
                            windowWidthSize = widthSizeClass,
                            comment = comment,

                            onProfileImageClick = {
                                commentIdToUpdate = comment.id
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}