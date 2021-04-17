package ru.netology.nmedia.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.dto.Media
import ru.netology.nmedia.dto.MediaUpload
import ru.netology.nmedia.dto.Post

interface PostRepository {
    val dataPaging: Flow<PagingData<Post>>
    val dataPosts: Flow<List<Post>>
    fun getNewerCount(id: Long): Flow<Int>
    suspend fun getAll()
    suspend fun markPostToShow()
    suspend fun getPostById(id: Long): Post
    suspend fun likeById(id: Long): Post
    suspend fun unlikeById(id: Long): Post
    suspend fun postCreation(post: Post)
    suspend fun deleteById(id: Long)
    suspend fun upload(upload: MediaUpload): Media
    suspend fun postCreationWork(post: Post, upload: MediaUpload?): Long
    suspend fun processWork(id: Long)
}
