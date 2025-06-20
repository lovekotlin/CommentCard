package com.example.commentcard.data.remote

import com.example.commentcard.data.model.Comment
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Retrofit service interface for fetching data from the API.
 * Defines all the network endpoints used by the application.
 */
interface APIService {

    /**
     * Fetches a list of comments for a specific post.
     * @return A Retrofit [Response] wrapping a list of [Comment] objects.
     */
    @GET("posts/{postId}/comments")
    suspend fun getComments(@Path("postId") postId: Int): List<Comment>
}