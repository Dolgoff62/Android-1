package ru.netology.nmedia.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val video: String,
    val published: String,
    val edited: String,
    val likeByMe: Boolean = false,
    val numberOfLikes: Int,
    val numberOfShare: Int,
    val numberOfViews: Int
) : Parcelable