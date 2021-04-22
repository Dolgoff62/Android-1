package ru.netology.nmedia.viewmodel

import android.app.Application
import android.net.Uri
import androidx.core.net.toFile
import androidx.lifecycle.*
import androidx.lifecycle.Transformations.map
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import androidx.work.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.dto.Ad
import ru.netology.nmedia.dto.FeedItem
import ru.netology.nmedia.dto.MediaUpload
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.model.FeedModelState
import ru.netology.nmedia.model.PhotoModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.util.SingleLiveEvent
import ru.netology.nmedia.utils.Utils
import ru.netology.nmedia.work.DeletePostWorker
import ru.netology.nmedia.work.SavePostWorker
import java.io.File
import javax.inject.Inject
import kotlin.random.Random

private val noPhoto = PhotoModel()

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PostViewModel @Inject constructor(
    private val repository: PostRepository,
    private val workManager: WorkManager,
    auth: AppAuth,
    application: Application
) : AndroidViewModel(application) {

    private val cached: Flow<PagingData<FeedItem>> = repository
        .dataPaging
        .map { pagingData ->
            pagingData.insertSeparators(
                generator = { before, after ->
                    if (before?.id?.rem(5) != 0L) null else
                        Ad(
                            Random.nextLong(),
                            "https://netology.ru",
                            "figma.jpg"
                        )
                }
            )
        }
        .cachedIn(viewModelScope)

    private val dataPosts: LiveData<FeedModel> = auth.authStateFlow
        .flatMapLatest { (myId, _) ->
            repository.dataPosts
                .map { posts ->
                    FeedModel(
                        posts.map { it.copy(ownedByMe = it.authorId == myId) },
                        posts.isEmpty()
                    )
                }
        }.asLiveData(Dispatchers.Default)

    private val liked = MutableStateFlow<Map<Long, Post>>(mapOf())

    val dataPaging: Flow<PagingData<FeedItem>> = auth.authStateFlow
        .flatMapLatest { (myId, _) ->
            liked.tryEmit(emptyMap())
            cached
                .cachedIn(viewModelScope)
                .combine(liked) { pagingData, liked ->
                    pagingData.map { feedItem ->
                        if (feedItem is Post) {
                            (liked[feedItem.id] ?: feedItem)
                                .copy(ownedByMe = feedItem.authorId == myId)
                        } else {
                            feedItem
                        }
                    }
                }
        }

    private val _dataState = MutableLiveData<FeedModelState>()
    val dataState: LiveData<FeedModelState>
        get() = _dataState

    val newerCount: LiveData<Int> = dataPosts.switchMap {
        repository.getNewerCount(it.posts.firstOrNull()?.id ?: 0L)
            .catch { e -> e.printStackTrace() }
            .asLiveData()
    }

    private val edited = MutableLiveData(Utils.EmptyPost.empty)

    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    private val _photo = MutableLiveData(noPhoto)
    val photo: LiveData<PhotoModel>
        get() = _photo

    init {
        loadPosts()
    }

    fun loadPosts() = viewModelScope.launch {
        try {
            _dataState.value = FeedModelState(loading = true)
            repository.getAll()
            _dataState.value = FeedModelState()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }

    fun makeReadPosts() = CoroutineScope(Dispatchers.IO).launch {
        repository.markPostToShow()
    }

    fun postCreation() {
        edited.value?.let {
            _postCreated.value = Unit
            viewModelScope.launch {
                try {
                    val id: Long = repository.postCreationWork(
                        it, _photo.value?.uri?.let { MediaUpload(it.toFile()) }
                    )
                    val data = workDataOf(SavePostWorker.postKey to id)
                    val constraints = Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                    val request = OneTimeWorkRequestBuilder<SavePostWorker>()
                        .setInputData(data)
                        .setConstraints(constraints)
                        .build()
                    workManager.enqueue(request)

                    _dataState.value = FeedModelState()
                } catch (e: Exception) {
                    _dataState.value = FeedModelState(error = true)
                }
            }
        }
        edited.value = Utils.EmptyPost.empty
        _photo.value = noPhoto
    }

    fun changeContent(postId: Long, content: String, newPost: Boolean) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value =
            edited.value?.copy(
                id = postId,
                content = text,
                ownedByMe = true,
                newPost = newPost
            )
    }

    fun changePhoto(uri: Uri?, file: File?) {
        _photo.value = PhotoModel(uri, file)
    }

    fun likeById(id: Long) {
        edited.value?.let {
            _postCreated.value = Unit
            viewModelScope.launch {
                try {
                    val newPost = repository.likeById(id)
                    _dataState.value = FeedModelState()
                    liked.tryEmit(liked.value + (newPost.id to newPost))
                } catch (e: Exception) {
                    _dataState.value = FeedModelState(error = true)
                }
            }
        }
        edited.value = Utils.EmptyPost.empty
    }

    fun unlikeById(id: Long) {
        edited.value?.let {
            _postCreated.value = Unit
            viewModelScope.launch {
                try {
                    val newPost = repository.unlikeById(id)
                    _dataState.value = FeedModelState()
                    liked.tryEmit(liked.value + (newPost.id to newPost))
                } catch (e: Exception) {
                    _dataState.value = FeedModelState(error = true)
                }
            }
        }
        edited.value = Utils.EmptyPost.empty
    }

    private val _postDelete = SingleLiveEvent<Boolean>()
    val postDelete: LiveData<Boolean>
        get() = _postDelete

    fun deleteById(id: Long) = viewModelScope.launch {
        try {
            val data = workDataOf(DeletePostWorker.postKey to id)
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val request = OneTimeWorkRequestBuilder<DeletePostWorker>()
                .setInputData(data)
                .setConstraints(constraints)
                .build()

            workManager.enqueue(request)
            _postDelete.value = true
            _dataState.value = FeedModelState()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }
}


