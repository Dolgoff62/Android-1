package ru.netology.nmedia.repository

import android.net.Uri
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.paging.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okio.IOException
import ru.netology.nmedia.api.PostApi
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dao.PostRemoteKeyDao
import ru.netology.nmedia.dao.PostWorkerDao
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Attachment
import ru.netology.nmedia.dto.Media
import ru.netology.nmedia.dto.MediaUpload
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.*
import ru.netology.nmedia.error.*
import ru.netology.nmedia.model.*
import java.sql.SQLException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRepositoryImpl @Inject constructor(
    private val dao: PostDao,
    private val postWorkerDao: PostWorkerDao,
    private val postApi: PostApi,
    appDb: AppDb,
    postRemoteKeyDao: PostRemoteKeyDao
) : PostRepository {


    @ExperimentalPagingApi
    override val dataPaging: Flow<PagingData<Post>> = Pager(
        config = PagingConfig(pageSize = 10),
        remoteMediator = PostRemoteMediator(postApi, appDb, dao, postRemoteKeyDao),
        pagingSourceFactory = dao::pagingSource
    ).flow.map { pagingData ->
        pagingData.map(PostEntity::toDto)
    }

    override val dataPosts = dao.getAll()
        .map(List<PostEntity>::toDto)
        .flowOn(Dispatchers.Default)

    override fun getNewerCount(id: Long): Flow<Int> = flow {
        while (true) {
            delay(180_000L)
            val response = postApi.getNewer(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(body.toApiEntity())
            emit(body.size)
        }
    }
        .catch { e -> throw AppError.from(e) }
        .flowOn(Dispatchers.Default)

    override suspend fun getAll() {
        try {
            val response = postApi.getAll()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(body.toEntity())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun markPostToShow() {
        try {
            dao.showOrNot(true)
        } catch (e: SQLException) {
            throw DbError
        }
    }

    override suspend fun getPostById(id: Long): Post {
        try {
            return dao.getPostById(id).toDto()
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun postCreation(post: Post) {
        try {
            val response = postApi.postCreation(post)
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
    }

    override suspend fun upload(upload: MediaUpload): Media {
        try {
            val media = MultipartBody.Part.createFormData(
                "file", upload.file.name, upload.file.asRequestBody()
            )
            val response = postApi.upload(media)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            return response.body() ?: throw ApiError(response.code(), response.message())
        } catch (e: java.io.IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun postCreationWork(post: Post, upload: MediaUpload?): Long {
        try {
            val entity = PostWorkerEntity.fromDto(post).apply {
                if (upload != null) {
                    this.uri = upload.file.toUri().toString()
                }
            }
            return postWorkerDao.insert(entity)
        } catch (e: Exception) {
            throw UnknownError
        }
    }


    override suspend fun processWork(id: Long) {
        try {
            val entity: PostWorkerEntity = postWorkerDao.getPostById(id)
            val postFromWorkerEntity = entity.toDto()
            val postToCreate = if (postFromWorkerEntity.newPost) {
                postFromWorkerEntity.copy(id = 0L)
            } else {
                postFromWorkerEntity
            }

            if (entity.uri != null) {
                val upload = MediaUpload(Uri.parse(entity.uri).toFile())
                val media = upload(upload)
                val postWithAttachment =
                    postToCreate.copy(attachment = Attachment(media.id, AttachmentType.IMAGE))
                postCreation(postWithAttachment)
            } else {
                postCreation(postToCreate)
            }
            postWorkerDao.deleteById(postFromWorkerEntity.id)

        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }


    override suspend fun likeById(id: Long): Post =
        try {
            dao.likeById(id).let {
                val response = postApi.likeById(id)
                if (!response.isSuccessful) {
                    throw ApiError(response.code(), response.message())
                }
                response.body() ?: throw ApiError(response.code(), response.message())
            }
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }

    override suspend fun unlikeById(id: Long): Post =
        try {
            dao.likeById(id).let {
                val response = postApi.unlikeById(id)
                if (!response.isSuccessful) {
                    throw ApiError(response.code(), response.message())
                }
                response.body() ?: throw ApiError(response.code(), response.message())
            }
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }


    override suspend fun deleteById(id: Long) {
        try {
            val response = postApi.deleteById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            dao.deleteById(id)
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }
}
