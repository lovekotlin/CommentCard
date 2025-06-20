package com.example.commentcard.ui.comments.components

import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import coil3.compose.AsyncImage
import com.example.commentcard.R
import com.example.commentcard.ui.comments.components.previewparameterproviders.CommentPreviewParameterProvider
import com.example.commentcard.ui.comments.components.previewparameterproviders.UserInfoPreviewProvider
import com.example.commentcard.ui.comments.components.previewparameterproviders.UserInfoSample
import com.example.commentcard.ui.comments.model.CommentUIModel
import com.example.commentcard.ui.theme.CommentCardTheme
import com.example.commentcard.ui.theme.Dimens
import com.example.commentcard.util.Constants

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
                WindowWidthSizeClass.Compact -> Dimens.PaddingLarge
                WindowWidthSizeClass.Medium -> Dimens.PaddingExtraLarge
                WindowWidthSizeClass.Expanded -> Dimens.PaddingExtraExtraLarge
                else -> Dimens.PaddingLarge
            }
        )
    }

    val avatarSize by remember(windowWidthSize) {
        mutableStateOf(
            when (windowWidthSize) {
                WindowWidthSizeClass.Compact -> Dimens.AvatarSizeCompact
                WindowWidthSizeClass.Medium -> Dimens.AvatarSizeMedium
                WindowWidthSizeClass.Expanded -> Dimens.AvatarSizeExpanded
                else -> Dimens.AvatarSizeDefault // Consider if this default should align with Compact or be distinct
            }
        )
    }

    val commentByUserContentDescription = stringResource(R.string.cd_comment_by_user, comment.name)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.PaddingLarge, vertical = Dimens.PaddingMedium)
            .semantics {
                contentDescription = commentByUserContentDescription
            },
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.CardElevation),
        shape = RoundedCornerShape(Dimens.CardCornerRadius),
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
    val commentIdContentDescription = stringResource(R.string.cd_comment_identifier, comment.id)
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
        Spacer(modifier = Modifier.width(Dimens.PaddingLarge))
        Column(
            modifier = Modifier.weight(Constants.LAYOUT_WEIGHT),
            verticalArrangement = Arrangement.Center
        ) {
            UserInfoSection(name = comment.name, email = comment.email)
            Spacer(Modifier.height(Dimens.PaddingSmall))
            Text(
                text = "${comment.id}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.semantics {
                    contentDescription = commentIdContentDescription
                }
            )
            Spacer(Modifier.width(Dimens.PaddingLarge))
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
    val commentIdContentDescription = stringResource(R.string.cd_comment_identifier, comment.id)
    Row(
        modifier = Modifier.padding(cardPadding),
        verticalAlignment = Alignment.Top
    ) {
        ProfileImageSection(
            modifier = Modifier.padding(end = Dimens.PaddingLarge),
            name = comment.name,
            imageUri = comment.profileImageUri,
            avatarSize = avatarSize,
            onProfileImageClick = onProfileImageClick,
        )
        Column {
            UserInfoSection(name = comment.name, email = comment.email)
            Spacer(Modifier.height(Dimens.PaddingSmall))
            Text(
                text = "${comment.id}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.semantics {
                    contentDescription = commentIdContentDescription
                }
            )
            Spacer(Modifier.height(Dimens.PaddingMedium))
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
    val changeImageLabel = stringResource(R.string.cd_change_profile_picture, name)
    val profilePictureContentDescription = stringResource(R.string.cd_profile_picture, name)
    val imageModifier = Modifier
        .size(avatarSize)
        .clip(CircleShape)
        .background(MaterialTheme.colorScheme.primaryContainer, shape = CircleShape)
        .clickable(onClickLabel = changeImageLabel, onClick = onProfileImageClick)
        .semantics { contentDescription = profilePictureContentDescription }

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
                modifier = imageModifier.padding(avatarSize * Constants.AVATAR_ICON_PADDING_PERCENTAGE),
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
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingMedium),
        itemVerticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )

        // Email Text - No maxLines. It will flow to the next line if the name is long.
        Text(
            text = email,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
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
    val maxLines =
        if (isCompact && !isExpanded) Constants.COMMENT_COLLAPSED_MAX_LINES else Int.MAX_VALUE

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Dimens.PaddingMedium)
    ) {
        Text(
            text = comment.body,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = maxLines,
            overflow = if (maxLines != Int.MAX_VALUE) TextOverflow.Ellipsis else TextOverflow.Visible
        )

        // Show expand/collapse button for compact mode
        if (isCompact && comment.body.length > Constants.COMMENT_EXPAND_CHARACTER_THRESHOLD) {
            TextButton(
                onClick = { isExpanded = !isExpanded },
                modifier = Modifier.align(Alignment.End),
                contentPadding = PaddingValues(
                    horizontal = Dimens.PaddingMedium,
                    vertical = Dimens.PaddingSmall
                )
            ) {
                Text(
                    text = if (isExpanded) stringResource(R.string.show_less) else stringResource(R.string.show_more),
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
@Preview(name = "Landscape", widthDp = 800, heightDp = 400)
@Composable
private fun CommentCardParameterPreview(@PreviewParameter(CommentPreviewParameterProvider::class) comment: CommentUIModel) {
    MaterialTheme {
        CommentCard(
            windowWidthSize = WindowWidthSizeClass.Compact,
            comment = comment
        )
    }
}

@Preview(name = "Short Name", showBackground = true)
@Preview(name = "Long Name (Wrapping)", showBackground = true)
@Composable
private fun UserInfoParameterPreview(@PreviewParameter(UserInfoPreviewProvider::class) userInfo: UserInfoSample) {
    CommentCardTheme {
        UserInfoSection(name = userInfo.name, email = userInfo.email)
    }
}
