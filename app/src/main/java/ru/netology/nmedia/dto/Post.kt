package ru.netology.nmedia.dto

import android.provider.MediaStore
import android.widget.VideoView
import java.net.URL

data class Post(
    val id: Long,
    val avatar: Int,
    val author: String,
    val content: String,
    val video: String?,
    val published: String,
    val likeByMe: Boolean = false,
    val numberOfLikes: Int,
    val numberOfShare: Int,
    val numberOfViews: Int
)