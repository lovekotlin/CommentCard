package com.example.commentcard.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage

/**
 * Data class representing a comment from the API response
 */
data class Comment(
    val postId: Int,
    val id: Int,
    val name: String,
    val email: String,
    val body: String,
    val profileImageUri: String? = null // Added for profile image functionality
)

/**
 * A composable that displays a comment card with profile image, name, email, and comment body.
 *
 * @param comment The comment data to display
 * @param onProfileImageClick Callback invoked when the profile image is clicked
 * @param modifier Optional modifier for customizing the card's appearance
 */
@Composable
fun CommentCard(
    modifier: Modifier = Modifier,
    windowWidthSize: WindowWidthSizeClass,
    comment: Comment,
    onProfileImageClick: () -> Unit = {}
) {
    // Get current configuration for responsive design
    val configuration = LocalConfiguration.current
    val isLandscape by remember(configuration.orientation) {
        mutableStateOf(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
    }

    val cardPadding by remember(windowWidthSize) {
        mutableStateOf(
            when (windowWidthSize) {
                WindowWidthSizeClass.Compact -> 16.dp
                WindowWidthSizeClass.Medium -> 20.dp
                WindowWidthSizeClass.Expanded -> 24.dp
                else -> 16.dp
            }
        )
    }

    val avatarSize by remember(windowWidthSize) {
        mutableStateOf(
            when (windowWidthSize) {
                WindowWidthSizeClass.Compact -> 48.dp
                WindowWidthSizeClass.Medium -> 52.dp
                WindowWidthSizeClass.Expanded -> 56.dp
                else -> 40.dp // Consider if this default should align with Compact or be distinct
            }
        )
    }

    val contentSpacing by remember(isLandscape, windowWidthSize) {
        derivedStateOf {
            if (isLandscape) {
                when (windowWidthSize) {
                    WindowWidthSizeClass.Compact -> 10.dp
                    else -> 12.dp
                }
            } else {
                16.dp
            }
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .semantics {
                contentDescription = "Comment by ${comment.name}"
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        if (isLandscape) {
            // Landscape layout: Horizontal arrangement for better screen utilization
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(cardPadding),
                horizontalArrangement = Arrangement.spacedBy(contentSpacing)
            ) {
                ProfileImageSection(
                    comment = comment,
                    avatarSize = avatarSize,
                    onProfileImageClick = onProfileImageClick
                )

                ContentSection(
                    comment = comment,
                    modifier = Modifier.weight(1f),
                    isCompact = true
                )
            }
        } else {
            // Portrait layout: Vertical arrangement for readability
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(cardPadding),
                verticalArrangement = Arrangement.spacedBy(contentSpacing)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ProfileImageSection(
                        comment = comment,
                        avatarSize = avatarSize,
                        onProfileImageClick = onProfileImageClick
                    )

                    UserInfoSection(comment = comment)
                }

                ContentSection(
                    comment = comment,
                    modifier = Modifier.fillMaxWidth(),
                    isCompact = false
                )
            }
        }
    }
}

/**
 * Profile image section with click functionality for image replacement
 */
@Composable
private fun ProfileImageSection(
    comment: Comment,
    avatarSize: Dp,
    onProfileImageClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(avatarSize)
            .clip(CircleShape)
            .clickable(
                onClickLabel = "Change profile picture"
            ) { onProfileImageClick() }
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        if (comment.profileImageUri != null) {
            // Display custom profile image using Coil
            AsyncImage(
                model = comment.profileImageUri,
                contentDescription = "Profile picture of ${comment.name}",
                modifier = Modifier.size(avatarSize),
                contentScale = ContentScale.Crop
            )
        } else {
            // Display default placeholder icon
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Default profile picture for ${comment.name}",
                modifier = Modifier.size(avatarSize * 0.6f),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

/**
 * User information section displaying name and email
 */
@Composable
private fun UserInfoSection(comment: Comment) {
    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = comment.name,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold,
                lineHeight = 20.sp
            ),
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = Int.MAX_VALUE, // Prevent truncation
            overflow = TextOverflow.Visible
        )

        Text(
            text = comment.email,
            style = MaterialTheme.typography.bodySmall.copy(
                lineHeight = 16.sp
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = Int.MAX_VALUE, // Prevent truncation
            overflow = TextOverflow.Visible
        )
    }
}

/**
 * Comment content section with expandable text to prevent truncation
 */
@Composable
private fun ContentSection(
    comment: Comment,
    modifier: Modifier = Modifier,
    isCompact: Boolean = false
) {
    var isExpanded by remember { mutableStateOf(value = false) }
    val maxLines = if (isCompact && !isExpanded) 3 else Int.MAX_VALUE

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = comment.body,
            style = MaterialTheme.typography.bodyMedium.copy(
                lineHeight = 20.sp
            ),
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = maxLines,
            overflow = if (maxLines != Int.MAX_VALUE) TextOverflow.Ellipsis else TextOverflow.Visible
        )

        // Show expand/collapse button for compact mode
        if (isCompact && comment.body.length > 150) {
            TextButton(
                onClick = { isExpanded = !isExpanded },
                modifier = Modifier.align(Alignment.End),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = if (isExpanded) "Show less" else "Show more",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

@Preview(name = "Portrait - Light Theme")
@Preview(
    name = "Portrait - Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun CommentCardPreview() {
    MaterialTheme {
        CommentCard(
            windowWidthSize = WindowWidthSizeClass.Compact,
            comment = Comment(
                postId = 1,
                id = 1,
                name = "id labore ex et quam laborum",
                email = "Eliseo@gardner.biz",
                body = "laudantium enim quasi est quidem magnam voluptate ipsam eos\ntempora quo necessitatibus\ndolor quam autem quasi\nreiciendis et nam sapiente accusantium"
            )
        )
    }
}

@Preview(name = "Landscape", widthDp = 800, heightDp = 400)
@Composable
private fun CommentCardLandscapePreview() {
    MaterialTheme {
        CommentCard(
            windowWidthSize = WindowWidthSizeClass.Compact,
            comment = Comment(
                postId = 1,
                id = 2,
                name = "quo vero reiciendis velit similique earum",
                email = "Jayne_Kuhic@sydney.com",
                body = "est natus enim nihil est dolore omnis voluptatem numquam\net omnis occaecati quod ullam at\nvoluptatem error expedita pariatur\nnihil sint nostrum voluptatem reiciendis et"
            )
        )
    }
}

@Preview(name = "With Custom Profile Image")
@Composable
private fun CommentCardWithImagePreview() {
    MaterialTheme {
        CommentCard(
            windowWidthSize = WindowWidthSizeClass.Medium,
            comment = Comment(
                postId = 1,
                id = 3,
                name = "odio adipisci rerum aut animi",
                email = "Nikita@garfield.biz",
                body = "quia molestiae reprehenderit quasi aspernatur\\naut expedita occaecati aliquam eveniet laudantium\\nomnis quibusdam delectus saepe quia accusamus maiores nam est\\ncum et ducimus et vero voluptates excepturi deleniti ratione. This is a comment with a custom profile image to demonstrate the image loading functionality.",
                profileImageUri = "https://example.com/profile.jpg"
            )
        )
    }
}