package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun getAll(): List<Post>
    fun likeById(id: Long): Post
    fun unlikeById(id: Long): Post
    fun postCreation(post: Post): Post
    fun deleteById(id: Long)
    fun findPostById(id: Long): Post
    fun updatePost(post: Post): Post

    fun getAllAsync(callback: GetAllCallback)
    fun getPostAsync(id: Long, callback: GetPostCallback)

    interface GetAllCallback {
        fun onSuccess(posts: List<Post>) {}
        fun onError(e: Exception) {}
    }

    interface GetPostCallback {
        fun onSuccess(post: Post) {}
        fun onError(e: Exception) {}
    }
}