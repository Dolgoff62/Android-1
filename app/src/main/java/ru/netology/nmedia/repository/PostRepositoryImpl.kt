package ru.netology.nmedia.repository

import android.os.StrictMode
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.netology.nmedia.BuildConfig.BASE_URL
import ru.netology.nmedia.api.PostApi
import ru.netology.nmedia.api.PostApiService
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.ApiError
import java.io.IOException
import java.util.concurrent.TimeUnit
import java.lang.RuntimeException



class PostRepositoryImpl : PostRepository {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()
    private val typeListToken = object : TypeToken<List<Post>>() {}
    private val typePostToken = object : TypeToken<Post>() {}

//    companion object {
//        private const val BASE_URL = "http://10.0.2.2:9999"
//        private val jsonType = "application/json".toMediaType()
//    }

    override fun getAllAsync(callback: PostRepository.Callback<List<Post>>) {
        PostApiService.api.getAll().enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                callback.onSuccess(response.body()?: throw RuntimeException("body is null"))
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
        val request: Request = Request.Builder()
            .method("POST", body = "".toRequestBody())
            .url("${BASE_URL}/api/posts/$id/likes")
            .build()

        return client.newCall(request)
            .execute()
            .use {
                it.body?.string()
            }
            .let {
                gson.fromJson(it, typePostToken.type)
            }
    }

    override fun unlikeById(id: Long, callback: PostRepository.Callback<Post>) {
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/posts/$id/likes")
            .build()

        return client.newCall(request)
            .execute()
            .use {
                it.body?.string()
            }
            .let {
                gson.fromJson(it, typePostToken.type)
            }
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

//    override fun updatePost(post: Post, callback: PostRepository.Callback<Post>) {
//        val request: Request = Request.Builder()
//            .post(gson.toJson(post).toRequestBody(jsonType))
//            .url("${BASE_URL}/api/posts")
//            .build()
//
//        return client.newCall(request)
//            .execute()
//            .use {
//                it.body?.string()
//            }
//            .let {
//                gson.fromJson(it, typePostToken.type)
//            }
//    }

    override fun deleteById(id: Long, callback: PostRepository.Callback<Unit>) {
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/posts/$id")
            .build()

        client.newCall(request)
            .execute()
    }

    override fun findPostById(id: Long, callback: PostRepository.Callback<Post>) {

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val request: Request = Request.Builder()
            .get()
            .url("${BASE_URL}/api/posts/$id")
            .build()

        return client.newCall(request)
            .execute()
            .use {
                it.body?.string()
            }
            .let {
                gson.fromJson(it, typePostToken.type)
            }
    }
}