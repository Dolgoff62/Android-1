package ru.netology.nmedia.repository

import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.dto.Post

interface PostRepository {
    val data: Flow<List<Post>>
    fun getNewerCount(id: Long): Flow<Int>
    suspend fun getAll()
    suspend fun markPostToShow()
    suspend fun getPostById(id: Long): Post
    suspend fun likeById(id: Long)
    suspend fun unlikeById(id: Long)
    suspend fun postCreation(post: Post)
    suspend fun deleteById(id: Long)
}
