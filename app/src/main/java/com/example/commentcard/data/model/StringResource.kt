package com.example.commentcard.data.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

/**
 * A wrapper to pass string resources from ViewModels to the UI,
 * allowing the UI to handle localization and formatting.
 */
data class StringResource(
    @StringRes val id: Int,
    val args: List<Any> = emptyList()
) {
    @Composable
    fun asString(): String {
        return stringResource(id, *args.toTypedArray())
    }
}