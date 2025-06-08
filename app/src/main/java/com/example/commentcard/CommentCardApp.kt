package com.example.commentcard

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Entry point for the Hilt component graph. Required by Hilt for dependency injection.
 */
@HiltAndroidApp
class CommentCardApp: Application()