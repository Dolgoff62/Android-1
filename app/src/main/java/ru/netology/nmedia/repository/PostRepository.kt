package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun getAll(): LiveData<List<Post>>
    fun likeById(id: Long)
    fun toShareById(id: Long)
    fun postCreation(post: Post)
    fun deleteById(id: Long)
    fun findPostById(id: Long): Post
    fun increasingNumberOfViews(id: Long)

}