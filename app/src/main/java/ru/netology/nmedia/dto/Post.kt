package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val avatar: Int,
    val author: String,
    val content: String,
    val published: String,
    val likeByMe: Boolean = false,
    val numberOfLikes: Int,
    val numberOfShare: Int,
    val numberOfViews: Int
)