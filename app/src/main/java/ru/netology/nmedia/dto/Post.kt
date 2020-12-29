package ru.netology.nmedia.dto

import com.google.gson.annotations.SerializedName

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    @SerializedName("likedByMe")
    val likeByMe: Boolean = false,
    @SerializedName("likes")
    var numberOfLikes: Int,
)