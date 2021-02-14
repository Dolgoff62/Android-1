package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {
<<<<<<< HEAD
    val data: LiveData<List<Post>>
    suspend fun getAll()
    suspend fun getPostById(id: Long): Post
    suspend fun likeById(id: Long)
    suspend fun unlikeById(id: Long)
    suspend fun postCreation(post: Post)
    suspend fun deleteById(id: Long)
}
=======
    fun getAllAsync(callback: Callback<List<Post>>)
    fun getPostAsync(id: Long, callback: Callback<Post>)
    fun likeById(id: Long, callback: Callback<Post>)
    fun unlikeById(id: Long, callback: Callback<Post>)
    fun postCreation(post: Post, callback: Callback<Post>)
    fun deleteById(id: Long, callback: Callback<Unit>)

    interface Callback<T> {
        fun onSuccess(posts: T) {}
        fun onError(e: ApiError) {}
    }
}
>>>>>>> 74f650ad9a25f8190adc488187d899c354210929
