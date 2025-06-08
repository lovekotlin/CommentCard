package com.example.commentcard.ui.comments.components

import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.core.net.toUri
import coil3.compose.AsyncImage
import com.example.commentcard.ui.comments.model.CommentUIModel

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
    comment: CommentUIModel,
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
        if (isLandscape && windowWidthSize != WindowWidthSizeClass.Compact) {
            LandscapeLayout(
                comment = comment,
                avatarSize = avatarSize,
                cardPadding = cardPadding,
                onProfileImageClick = onProfileImageClick
            )
        } else {
            PortraitLayout(
                comment = comment,
                avatarSize = avatarSize,
                cardPadding = cardPadding,
                onProfileImageClick = onProfileImageClick
            )
        }
    }
}

@Composable
private fun LandscapeLayout(
    comment: CommentUIModel,
    avatarSize: Dp,
    cardPadding: Dp,
    onProfileImageClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(cardPadding)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfileImageSection(
            name = comment.name,
            imageUri = comment.profileImageUri,
            avatarSize = avatarSize,
            onProfileImageClick = onProfileImageClick
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            UserInfoSection(name = comment.name, email = comment.email)
            Spacer(Modifier.width(16.dp))
            ContentSection(comment = comment, isCompact = true)
        }
    }
}

@Composable
private fun PortraitLayout(
    comment: CommentUIModel,
    avatarSize: Dp,
    cardPadding: Dp,
    onProfileImageClick: () -> Unit
) {
    Row(
        modifier = Modifier.padding(cardPadding),
        verticalAlignment = Alignment.Top
    ) {
        ProfileImageSection(
            modifier = Modifier.padding(end = 16.dp),
            name = comment.name,
            imageUri = comment.profileImageUri,
            avatarSize = avatarSize,
            onProfileImageClick = onProfileImageClick,
        )
        Column {
            UserInfoSection(name = comment.name, email = comment.email)
            Spacer(Modifier.height(8.dp))
            ContentSection(comment = comment, isCompact = false)
        }
    }
}

/**
 * Profile image section with click functionality for image replacement
 */
@Composable
private fun ProfileImageSection(
    modifier: Modifier = Modifier,
    name: String,
    imageUri: Uri?,
    avatarSize: Dp,
    onProfileImageClick: () -> Unit
) {
    val imageModifier = Modifier
        .size(avatarSize)
        .clip(CircleShape)
        .background(MaterialTheme.colorScheme.primaryContainer, shape = CircleShape)
        .clickable(onClickLabel = "Change profile picture for $name", onClick = onProfileImageClick)
        .semantics { contentDescription = "Profile picture for $name" }

    Box(
        modifier = modifier, contentAlignment = Alignment.Center
    ) {
        if (null != imageUri) {
            AsyncImage(
                model = imageUri,
                contentDescription = null,
                modifier = imageModifier,
                contentScale = ContentScale.Crop
            )
        } else {
            // Display default placeholder icon
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = imageModifier.padding(avatarSize * 0.2f),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

/**
 * User information section displaying name and email
 */
@Composable
private fun UserInfoSection(name: String, email: String) {
    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold,
                lineHeight = 20.sp
            ),
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = Int.MAX_VALUE, // Prevent truncation
            overflow = TextOverflow.Visible
        )

        Text(
            text = email,
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
    comment: CommentUIModel,
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
            comment = CommentUIModel(
                id = 1,
                name = "id labore ex et quam laborum",
                email = "Eliseo@gardner.biz",
                body = "laudantium enim quasi est quidem magnam voluptate ipsam eos\ntempora quo necessitatibus\ndolor quam autem quasi\nreiciendis et nam sapiente accusantium",
                profileImageUri = Uri.EMPTY
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
            comment = CommentUIModel(
                id = 2,
                name = "quo vero reiciendis velit similique earum",
                email = "Jayne_Kuhic@sydney.com",
                body = "est natus enim nihil est dolore omnis voluptatem numquam\net omnis occaecati quod ullam at\nvoluptatem error expedita pariatur\nnihil sint nostrum voluptatem reiciendis et",
                profileImageUri = null
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
            comment = CommentUIModel(
                id = 3,
                name = "odio adipisci rerum aut animi",
                email = "Nikita@garfield.biz",
                body = "quia molestiae reprehenderit quasi aspernatur\\naut expedita occaecati aliquam eveniet laudantium\\nomnis quibusdam delectus saepe quia accusamus maiores nam est\\ncum et ducimus et vero voluptates excepturi deleniti ratione. This is a comment with a custom profile image to demonstrate the image loading functionality.",
                profileImageUri = "https://example.com/profile.jpg".toUri()
            )
        )
    }
}