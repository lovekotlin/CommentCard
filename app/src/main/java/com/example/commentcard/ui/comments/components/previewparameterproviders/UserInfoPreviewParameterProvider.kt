package com.example.commentcard.ui.comments.components.previewparameterproviders

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

data class UserInfoSample(val name: String, val email: String)

class UserInfoPreviewProvider : PreviewParameterProvider<UserInfoSample> {
    override val values: Sequence<UserInfoSample>
        get() = sequenceOf(
            UserInfoSample(name = "John Doe", email = "john.doe@example.com"),
            UserInfoSample(
                name = "Johnathan Bartholomew Doe The Third",
                email = "jb.doe.the.third@example.com"
            )
        )
}