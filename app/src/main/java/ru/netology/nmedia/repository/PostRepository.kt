package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {
    val data: LiveData<List<Post>>
    suspend fun getAll()
    suspend fun getPostById(id: Long): Post
    suspend fun likeById(id: Long)
    suspend fun unlikeById(id: Long)
    suspend fun postCreation(post: Post)
    suspend fun deleteById(id: Long)
}
