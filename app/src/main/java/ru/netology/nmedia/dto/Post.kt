package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val avatar: Int,
    val author: String,
    val content: String,
    val published: String,
    var likeByMe: Boolean = false,
    var numberOfLikes: Int,
    var numberOfShare: Int,
    var numberOfViews: Int
)