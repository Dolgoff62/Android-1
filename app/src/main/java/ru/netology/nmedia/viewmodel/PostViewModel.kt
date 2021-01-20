package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl
import ru.netology.nmedia.util.SingleLiveEvent
import java.io.IOException
import java.util.concurrent.Executors
import kotlin.concurrent.thread

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    authorAvatar = "",
    likeByMe = false,
    published = "",
    numberOfLikes = 0,
    attachment = null
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryImpl()
    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel>
        get() = _data
    val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    private val executorService = Executors.newFixedThreadPool(64)

    var actualLikedByMe: MutableMap<Long, Boolean> = emptyMap<Long, Boolean>().toMutableMap()

    init {
        loadPosts()
    }

    fun loadPosts() {
        _data.postValue(FeedModel(loading = true))
        repository.getAllAsync(object : PostRepository.GetAllCallback {
            override fun onSuccess(posts: List<Post>) {
                posts.map {
                    if (actualLikedByMe.containsKey(it.id)) {
                        actualLikedByMe[it.id]?.let { it1 -> it.copy(likeByMe = it1) }
                    } else {
                        it
                    }
                }
                _data.postValue(FeedModel(posts = posts, empty = posts.isEmpty()))
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }

    fun postCreation() {
        edited.value?.let {
            val post = edited.value!!
            executorService.execute {
                val newPost = repository.postCreation(post)
                _postCreated.postValue(Unit)
                _data.postValue(
                    _data.value?.copy(posts = listOf(newPost) + _data.value?.posts as List<Post>)
                )
            }
        }
        edited.value = empty

    }

    fun updatePost() {
        edited.value?.let {
            val post = edited.value!!
            executorService.execute {
                val updatedPost = repository.updatePost(post)
                _data.postValue(
                    _data.value?.copy(posts = _data.value?.posts.orEmpty()
                        .map { if (it.id != updatedPost.id) it else updatedPost }
                    )
                )
            }
        }
        edited.value = empty
    }

    fun changeContent(postId: Long, content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(id = postId, content = text)
    }

    fun deleteById(id: Long) = executorService.execute() {
        val old = _data.value?.posts.orEmpty()
        _data.postValue(
            _data.value?.copy(posts = _data.value?.posts.orEmpty()
                .filter { it.id != id }
            )
        )
        try {
            repository.deleteById(id)
        } catch (e: IOException) {
            _data.postValue(_data.value?.copy(posts = old))
        }
    }

    fun likeById(id: Long) = executorService.execute() {
        actualLikedByMe[id] = true

        val updatedPost = repository.likeById(id)
        _data.postValue(
            _data.value?.copy(posts = _data.value?.posts.orEmpty()
                .map { if (it.id != updatedPost.id) it else updatedPost.copy(likeByMe = actualLikedByMe[id]!!) }
            )
        )
    }

    fun unlikeById(id: Long) = executorService.execute() {
        actualLikedByMe[id] = false

        val updatedPost = repository.unlikeById(id)
        _data.postValue(
            _data.value?.copy(posts = _data.value?.posts.orEmpty()
                .map { if (it.id != updatedPost.id) it else updatedPost.copy(likeByMe = actualLikedByMe[id]!!) }
            )
        )
    }

    fun searchPost(id: Long): Post {
        return _data.value?.posts?.find { it.id == id }!!
    }
}
