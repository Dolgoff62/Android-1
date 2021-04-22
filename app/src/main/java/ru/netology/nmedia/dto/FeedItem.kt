package ru.netology.nmedia.dto

import com.google.gson.annotations.SerializedName

sealed class FeedItem{
    abstract val id: Long
}

data class Ad(
    override val id: Long,
    val url: String,
    val image: String,
) : FeedItem()

data class Post(
    override val id: Long,
    val authorId: Long,
    val author: String,
    val authorAvatar: String,
    val content: String,
    val published: String,
    @SerializedName("likedByMe")
    val likeByMe: Boolean = false,
    @SerializedName("likes")
    var numberOfLikes: Int,
    val showOrNot: Boolean = false,
    val newPost: Boolean = false,
    val ownedByMe: Boolean = false,
    val attachment: Attachment? = null
) : FeedItem()