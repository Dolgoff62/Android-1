package ru.netology.nmedia.dto

import androidx.room.Embedded
import com.google.gson.annotations.SerializedName
import ru.netology.nmedia.enum.AttachmentType

data class Post (
    val id: Long,
    val author: String,
    val authorAvatar: String,
    val content: String,
    val published: String,
    @SerializedName("likedByMe")
    val likeByMe: Boolean = false,
    @SerializedName("likes")
    var numberOfLikes: Int,
    val showOrNot: Boolean = false,
    @Embedded
    val attachment: Attachment? = null
)