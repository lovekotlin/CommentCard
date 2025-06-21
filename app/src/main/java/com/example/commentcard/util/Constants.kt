package com.example.commentcard.util

/**
 * A utility object to hold application-wide constants and magic numbers.
 * This helps in maintaining consistency and improves code readability.
 */
object Constants {
    // The number of lines to show for a comment in its collapsed state.
    const val COMMENT_COLLAPSED_MAX_LINES = 3

    // The character count threshold above which a comment body is considered long
    // and will show an expand/collapse button.
    const val COMMENT_EXPAND_CHARACTER_THRESHOLD = 150

    // The padding for the default placeholder icon, expressed as a percentage of the avatar's total size.
    const val AVATAR_ICON_PADDING_PERCENTAGE = 0.2f

    const val LAYOUT_WEIGHT = 1f
}