package ru.netology.nmedia.api

import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import ru.netology.nmedia.BuildConfig.BASE_URL
import ru.netology.nmedia.dto.AuthUser
import ru.netology.nmedia.dto.Media
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.PushToken

fun okhttp(vararg interceptors: Interceptor): OkHttpClient = OkHttpClient.Builder()
    .apply {
        interceptors.forEach {
            this.addInterceptor(it)
        }
    }
    .build()

fun retrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .client(client)
    .build()

interface PostApi {
    @GET("posts")
    suspend fun getAll(): Response<List<Post>>

    @GET("posts/{id}/newer")
    suspend fun getNewer(@Path("id") id: Long): Response<List<Post>>

    @GET("posts/{id}/before")
    suspend fun getBefore(
        @Path("id") id: Long,
        @Query("count") count: Int
    ): Response<List<Post>>

    @GET("posts/{id}/after")
    suspend fun getAfter(
        @Path("id") id: Long,
        @Query("count") count: Int
    ): Response<List<Post>>

    @GET("posts/latest")
    suspend fun getLatest(@Query("count") count: Int): Response<List<Post>>

    @GET("posts/{id}")
    suspend fun getPostById(@Path("id") id: Long): Response<Post>

    @POST("posts")
    suspend fun postCreation(@Body post: Post): Response<Post>

    @POST("posts/{id}/likes")
    suspend fun likeById(@Path("id") id: Long): Response<Post>

    @DELETE("posts/{id}/likes")
    suspend fun unlikeById(@Path("id") id: Long): Response<Post>

    @DELETE("posts/{id}")
    suspend fun deleteById(@Path("id") id: Long): Response<Unit>

    @POST("users/push-tokens")
    suspend fun sendPushToken(@Body token: PushToken): Response<Unit>

    @Multipart
    @POST("media")
    suspend fun upload(@Part media: MultipartBody.Part): Response<Media>

    @FormUrlEncoded
    @POST("users/authentication")
    suspend fun updateUser(
        @Field("login") login: String,
        @Field("pass") pass: String
    ): Response<AuthUser>

    @FormUrlEncoded
    @POST("users/registration")
    suspend fun registrationUser(
        @Field("login") login: String,
        @Field("pass") pass: String,
        @Field("name") name: String
    ): Response<AuthUser>

    @Multipart
    @POST("users/registration")
    suspend fun registrationUserWithAvatar(
        @Part("login") login: RequestBody,
        @Part("pass") pass: RequestBody,
        @Part("name") name: RequestBody,
        @Part media: MultipartBody.Part
    ): Response<AuthUser>
}
