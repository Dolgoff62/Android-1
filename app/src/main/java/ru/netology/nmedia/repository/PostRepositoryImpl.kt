package ru.netology.nmedia.repository

<<<<<<< HEAD
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import okio.IOException
=======
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
>>>>>>> 74f650ad9a25f8190adc488187d899c354210929
import ru.netology.nmedia.api.PostApiService
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity
import ru.netology.nmedia.entity.toDto
import ru.netology.nmedia.entity.toEntity
import ru.netology.nmedia.model.ApiError
<<<<<<< HEAD
import ru.netology.nmedia.model.NetworkError
import ru.netology.nmedia.model.UnknownError
import kotlin.concurrent.thread

=======
import java.util.concurrent.TimeUnit
>>>>>>> 74f650ad9a25f8190adc488187d899c354210929

class PostRepositoryImpl(private val dao: PostDao) : PostRepository {
    override val data = dao.getAll().map(List<PostEntity>::toDto)

<<<<<<< HEAD
    override suspend fun getAll() {
        try {
            val response = PostApiService.api.getAll()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
=======
class PostRepositoryImpl : PostRepository {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()

    override fun getAllAsync(callback: PostRepository.Callback<List<Post>>) {
        PostApiService.api.getAll().enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                callback.onSuccess(response.body() ?: throw RuntimeException("body is null"))
>>>>>>> 74f650ad9a25f8190adc488187d899c354210929
            }

            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(body.toEntity())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun getPostById(id: Long) : Post{
        try {
            return dao.getPostById(id)
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

<<<<<<< HEAD
    override suspend fun postCreation(post: Post) {
        try {
            val response = PostApiService.api.postCreation(post)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(PostEntity.fromDto(body))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
=======
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
>>>>>>> 74f650ad9a25f8190adc488187d899c354210929
    }

    override suspend fun likeById(id: Long) {
        dao.likeById(id)
        try {
            val response = PostApiService.api.likeById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
<<<<<<< HEAD

            response.body() ?: throw ApiError(response.code(), response.message())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun unlikeById(id: Long) {
        dao.likeById(id)
        try {
            val response = PostApiService.api.unlikeById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            response.body() ?: throw ApiError(response.code(), response.message())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }    }

    override suspend fun deleteById(id: Long) {
        try {
            val response = PostApiService.api.deleteById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            dao.deleteById(id)
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
=======
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
>>>>>>> 74f650ad9a25f8190adc488187d899c354210929
    }
}