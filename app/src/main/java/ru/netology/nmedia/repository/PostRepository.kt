package ru.netology.nmedia.repository

import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.ApiError

interface PostRepository {
    fun getAllAsync(callback: Callback<List<Post>>)
    fun getPostAsync(id: Long, callback: Callback<Post>)
    fun likeById(id: Long, callback: Callback<Post>)
    fun unlikeById(id: Long, callback: Callback<Post>)
    fun postCreation(post: Post, callback: Callback<Post>)
    fun deleteById(id: Long, callback: Callback<Unit>)
    fun findPostById(id: Long, callback: Callback<Post>)
//    fun updatePost(post: Post, callback: Callback<Post>)

    interface Callback<T>{
        fun onSuccess(posts: T) {}
        fun onError(e: ApiError) {}
    }
}