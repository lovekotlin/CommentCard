package com.example.commentcard.data.repository

import app.cash.turbine.test
import com.example.commentcard.data.model.Comment
import com.example.commentcard.data.remote.APIException
import com.example.commentcard.data.remote.APIService
import com.example.commentcard.data.remote.NoConnectivityException
import com.example.commentcard.data.remote.TimeoutException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

class CommentsRepositoryTest {
    private val apiService: APIService = mockk()
    private val repository = CommentsRepository(apiService)

    @Test
    fun `getComments when API is successful should emit success`() = runTest {
        val mockComments = listOf(
            Comment(
                id = 1,
                postId = 1,
                name = "name",
                email = "email",
                body = "body"
            )
        )
        coEvery { apiService.getComments() } returns mockComments

        repository.getComments().test {
            val result = awaitItem()
            assertTrue(result.isSuccess)
            assertEquals(mockComments, result.getOrNull())
            awaitComplete()
        }
    }

    @Test
    fun `getComments when API returns error should emit failure with ApiException`() = runTest {
        val mockResponse = Response.error<List<Comment>>(404, "".toResponseBody(null))
        coEvery { apiService.getComments() } throws HttpException(mockResponse)

        repository.getComments().test {
            val result = awaitItem()
            assertTrue(result.isFailure)
            val exception = result.exceptionOrNull()
            assertTrue(exception is APIException)
            assertEquals(404, (exception as APIException).code)
            awaitComplete()
        }
    }

    @Test
    fun `getComments when network throws IOException should emit failure with NoConnectivityException`() =
        runTest {
            coEvery { apiService.getComments() } throws IOException()

            repository.getComments().test {
                val result = awaitItem()
                assertTrue(result.isFailure)
                assertTrue(result.exceptionOrNull() is NoConnectivityException)
                awaitComplete()
            }
        }

    @Test
    fun `getComments when network throws SocketTimeoutException should emit failure with TimeoutException`() =
        runTest {
            coEvery { apiService.getComments() } throws SocketTimeoutException()

            repository.getComments().test {
                val result = awaitItem()
                assertTrue(result.isFailure)
                assertTrue(result.exceptionOrNull() is TimeoutException)
                awaitComplete()
            }
        }
}
