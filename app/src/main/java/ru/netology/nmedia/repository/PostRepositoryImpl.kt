package ru.netology.nmedia.repository

import android.os.StrictMode
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nmedia.dto.Post
import java.util.concurrent.TimeUnit


class PostRepositoryImpl : PostRepository {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()
    private val typeListToken = object : TypeToken<List<Post>>() {}
    private val typePostToken = object : TypeToken<Post>() {}

    companion object {
        private const val BASE_URL = "http://10.0.2.2:9999/api"
        private val jsonType = "application/json".toMediaType()
    }

    override fun getAll(): List<Post> {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/posts")
            .build()

        return client.newCall(request)
            .execute()
            .use { it.body?.string() }
            .let {
                gson.fromJson(it, typeListToken.type)
            }
    }

    override fun likeById(id: Long): Post {
        val request: Request = Request.Builder()
            .method("POST", body = "".toRequestBody())
            .url("${BASE_URL}/posts/$id/likes")
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

    override fun unlikeById(id: Long): Post {
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}/posts/$id/likes")
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

    override fun postCreation(post: Post): Post {
        val request: Request = Request.Builder()
            .post(gson.toJson(post).toRequestBody(jsonType))
            .url("${BASE_URL}/posts")
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

    override fun updatePost(post: Post): Post {
        val request: Request = Request.Builder()
            .post(gson.toJson(post).toRequestBody(jsonType))
            .url("${BASE_URL}/posts")
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

    override fun deleteById(id: Long) {
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}/posts/$id")
            .build()

        client.newCall(request)
            .execute()
    }

    override fun findPostById(id: Long): Post {

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val request: Request = Request.Builder()
            .get()
            .url("${BASE_URL}/posts/$id")
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