package ru.netology.nmedia.model

import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.utils.Utils

data class CardPostModel(
    val post: Post = Utils.EmptyPost.emptyPost,
    val loading: Boolean = false,
    val errorVisible: Boolean = false,
    val error: ApiError? = null,
    val refreshing: Boolean = false,
)

