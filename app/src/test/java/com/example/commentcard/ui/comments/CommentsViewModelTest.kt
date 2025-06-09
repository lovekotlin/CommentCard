package com.example.commentcard.ui.comments

import app.cash.turbine.test
import com.example.commentcard.MainCoroutineRule
import com.example.commentcard.data.model.Comment
import com.example.commentcard.data.remote.APIException
import com.example.commentcard.data.repository.CommentsRepository
import com.example.commentcard.ui.comments.model.CommentsViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

class CommentsViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val commentsRepository: CommentsRepository = mockk()
    private lateinit var viewModel: CommentsViewModel

    @Test
    fun `when fetching comments is successful, state is updated with comments`() = runTest {
        // Arrange
        val mockComments = listOf(
            Comment(
                id = 1,
                postId = 1,
                name = "name",
                email = "email",
                body = "body"
            )
        )
        coEvery { commentsRepository.getComments() } returns flowOf(Result.success(mockComments))

        // Act
        viewModel = CommentsViewModel(commentsRepository)

        // Assert
        viewModel.uiState.test {
            // Initial state is loading
            var state = awaitItem()
            assertEquals(true, state.isLoading)

            // Final state has data and loading is false
            state = awaitItem()
            assertEquals(false, state.isLoading)
            assertEquals(1, state.comments.size)
            assertEquals("name", state.comments[0].name)
            assertNull(state.error)
        }
    }

    @Test
    fun `when fetching comments fails, state is updated with error`() = runTest {
        // Arrange
        val apiException = APIException(404)
        coEvery { commentsRepository.getComments() } returns flowOf(Result.failure(apiException))

        // Act
        viewModel = CommentsViewModel(commentsRepository)

        // Assert
        viewModel.uiState.test {
            // Initial state is loading
            assertEquals(true, awaitItem().isLoading)

            // Final state has an error
            val state = awaitItem()
            assertEquals(false, state.isLoading)
            assertEquals(true, state.comments.isEmpty())
            assertEquals("The requested content could not be found.", state.error)
        }
    }
}