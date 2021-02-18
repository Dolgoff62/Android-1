package ru.netology.nmedia.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.google.gson.annotations.SerializedName

@Parcelize
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
    val showOrNot: Boolean = false
): Parcelable