package com.example.commentcard.ui.comments.components.previewparameterproviders

import android.net.Uri
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.core.net.toUri
import com.example.commentcard.ui.comments.model.CommentUIModel

class CommentPreviewParameterProvider : PreviewParameterProvider<CommentUIModel> {
    override val values: Sequence<CommentUIModel>
        get() = sequenceOf(
            CommentUIModel(
                id = 1,
                name = "id labore ex et quam laborum asdfasdf asdd fasdf  asddf asd fa sdf asd fa sdf sd fa sdf as df asd f asdf as df asd f asdf as df asd fa sdf asd f asdf a sdf asd f asd f asdf asd f asd fa sdf \n asdf as df asdf as df asd fa sdf asd f sadf\n asdf as df asdf asd f sadf as fdf",
                email = "Eliseo@gardner.biz",
                body = "laudantium enim quasi est quidem magnam voluptate ipsam eos\ntempora quo necessitatibus\ndolor quam autem quasi\nreiciendis et nam sapiente accusantium",
                profileImageUri = Uri.EMPTY
            ),
            CommentUIModel(
                id = 2,
                name = "quo vero reiciendis velit similique earum",
                email = "Jayne_Kuhic@sydney.com",
                body = "est natus enim nihil est dolore omnis voluptatem numquam\net omnis occaecati quod ullam at\nvoluptatem error expedita pariatur\nnihil sint nostrum voluptatem reiciendis et",
                profileImageUri = null
            ),
            CommentUIModel(
                id = 3,
                name = "odio adipisci rerum aut animi",
                email = "Nikita@garfield.biz",
                body = "quia molestiae reprehenderit quasi aspernatur\\naut expedita occaecati aliquam eveniet laudantium\\nomnis quibusdam delectus saepe quia accusamus maiores nam est\\ncum et ducimus et vero voluptates excepturi deleniti ratione. This is a comment with a custom profile image to demonstrate the image loading functionality.",
                profileImageUri = "https://example.com/profile.jpg".toUri()
            )
        )
}