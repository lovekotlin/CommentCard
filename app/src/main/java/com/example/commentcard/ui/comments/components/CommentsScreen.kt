package com.example.commentcard.ui.comments.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentsScreen(
    widthSizeClass: WindowWidthSizeClass,
    viewModel: CommentsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            val id = uiState.commentIdForImageUpdate
            if (uri != null && id != null) {
                viewModel.onEvent(CommentsContract.Event.OnProfileImageSelected(uri))
            }
        }
    )

    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "Comments Card") })
    }) { innerPadding ->
        Box {
            when {
                uiState.isLoading -> CircularProgressIndicator()

                uiState.error != null -> {
                    ErrorState(
                        message = uiState.error!!,
                        onRetry = { viewModel.onEvent(CommentsContract.Event.Retry) }
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(items = uiState.comments, key = { it.id }) { comment ->
                            CommentCard(
                                windowWidthSize = widthSizeClass,
                                comment = comment,

                                onProfileImageClick = {
                                    viewModel.onEvent(CommentsContract.Event.OnProfileImageClicked(comment.id))
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
}