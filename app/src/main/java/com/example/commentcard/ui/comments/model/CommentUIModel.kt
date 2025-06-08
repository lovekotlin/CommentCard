package com.example.commentcard.ui.comments.model

import android.net.Uri
import com.example.commentcard.data.model.Comment

/**
 * A UI-specific data model representing a comment to be displayed on the screen.
 * It is derived from the domain [Comment] model but includes additional properties
 * for UI state, such as the [profileImageUri].
 *
 * @property id The unique identifier for the comment, used for stable list keys.
 * @property name The name of the commenter.
 * @property email The email of the commenter.
 * @property body The main content of the comment.
 * @property profileImageUri The URI of a user-selected profile image. Null by default, indicating that a placeholder should be shown.
 */
data class CommentUIModel(
    val id: Int,
    val name: String,
    val email: String,
    val body: String,
    val profileImageUri: Uri? = null
)

/**
 * Extension function to map a list of domain [Comment] models to a list of
 * [CommentUIModel]s, preparing the data for UI consumption.
 */
fun List<Comment>.toUIModel(): List<CommentUIModel> {
    return this.map { comment ->
        CommentUIModel(
            id = comment.id,
            name = comment.name,
            email = comment.email,
            body = comment.body.replace("\n", " ")
        )
    }
}