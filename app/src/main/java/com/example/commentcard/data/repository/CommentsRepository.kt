package com.example.commentcard.data.repository

import com.example.commentcard.data.model.Comment
import com.example.commentcard.data.remote.APIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * Repository for fetching comments from the API.
 * This abstracts the data source and provides a clean API for accessing comment data.
 *
 * @param apiService The retrofit service used for making network requests.
 */
class CommentsRepository @Inject constructor(private val apiService: APIService) {
    /**
     * Fetches a list of comments from the API.
     * The result is wrapped in a [Flow] to allow for asynchronous stream processing.
     * Any potential errors during the network call are caught and rethrown to be handled by the collector (the ViewModel).
     *
     * @return A Flow emitting a [Result] which contains either the list of [Comment]s on success or an [Exception] on failure.
     */
    fun getComments(): Flow<Result<List<Comment>>> = flow {
        try {
            val response = apiService.getComments()
            if (response.isSuccessful && null != response.body()) {
                emit(Result.success(response.body()!!))
            } else {
                emit(Result.failure(RuntimeException("API Error: ${response.message()}")))
            }
        } catch (e: Exception) {
            // Catches network exceptions, timeouts etc.
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)
}