package com.example.commentcard.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Represents the raw data model for a comment fetched from the API.
 * This class maps directly to the JSON structure.
 *
 * @property id The unique identifier for the comment.
 * @property postId The identifier of the post this comment belongs to.
 * @property name A short name or title for the comment.
 * @property email The email address of the commenter.
 * @property body The main content of the comment.
 */
@JsonClass(generateAdapter = true)
data class Comment(
    @Json(name = "id")
    val id: Int,

    @Json(name = "postId")
    val postId: Int,

    @Json(name = "name")
    val name: String,

    @Json(name = "email")
    val email: String,

    @Json(name = "body")
    val body: String
)