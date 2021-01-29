package ru.netology.nmedia.repository

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.netology.nmedia.api.PostApiService
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.ApiError
import java.util.concurrent.TimeUnit


class PostRepositoryImpl : PostRepository {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()

    override fun getAllAsync(callback: PostRepository.Callback<List<Post>>) {
        PostApiService.api.getAll().enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                callback.onSuccess(response.body() ?: throw RuntimeException("body is null"))
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                callback.onError(ApiError.fromThrowable(t))
            }
        })
    }

    override fun getPostAsync(id: Long, callback: PostRepository.Callback<Post>) {
        PostApiService.api.getPostAsync(id).enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                callback.onSuccess(response.body() ?: throw RuntimeException("Body is null"))
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                callback.onError(ApiError.fromThrowable(t))
            }
        })
    }

    override fun likeById(id: Long, callback: PostRepository.Callback<Post>) {
        PostApiService.api.likeById(id).enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                callback.onSuccess(response.body() ?: throw RuntimeException("Body is null"))
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                callback.onError(ApiError.fromThrowable(t))
            }
        })
    }

    override fun unlikeById(id: Long, callback: PostRepository.Callback<Post>) {
        PostApiService.api.unlikeById(id).enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                callback.onSuccess(response.body() ?: throw RuntimeException("Body is null"))
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                callback.onError(ApiError.fromThrowable(t))
            }
        })
    }

    override fun postCreation(post: Post, callback: PostRepository.Callback<Post>) {
        PostApiService.api.postCreation(post).enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                callback.onSuccess(response.body() ?: throw RuntimeException("Body is null"))
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                callback.onError(ApiError.fromThrowable(t))
            }
        })
    }

    override fun deleteById(id: Long, callback: PostRepository.Callback<Unit>) {
        PostApiService.api.deleteById(id).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                callback.onSuccess(response.body() ?: Unit)
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                callback.onError(ApiError.fromThrowable(t))
            }
        })
    }
}