package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.ApiError
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl
import ru.netology.nmedia.util.SingleLiveEvent

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

    private val _postCreateError = SingleLiveEvent<ApiError>()
    val postCreateError: LiveData<ApiError>
        get() = _postCreateError

    var actualLikedByMe: MutableMap<Long, Boolean> = emptyMap<Long, Boolean>().toMutableMap()

    init {
        loadPosts()
    }

    fun loadPosts() {
        _data.value = FeedModel(loading = true)
        repository.getAllAsync(object : PostRepository.Callback<List<Post>> {
            override fun onSuccess(posts: List<Post>) {
                posts.map {
                    if (actualLikedByMe.containsKey(it.id)) {
                        actualLikedByMe[it.id]?.let { it1 -> it.copy(likeByMe = it1) }
                    } else {
                        it
                    }
                }
                _data.value = FeedModel(posts = posts, empty = posts.isEmpty())
            }

            override fun onError(e: ApiError) {
                _data.value = FeedModel(errorVisible = true, error = e)
            }
        })
    }

    fun postCreation() {
        edited.value?.let {
            repository.postCreation(it, object : PostRepository.Callback<Post> {
                override fun onSuccess(posts: Post) {
                    _postCreated.value = Unit
                }

                override fun onError(e: ApiError) {
                    _postCreateError.value = e
                }
            })
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

    fun likeById(id: Long) {

        repository.likeById(id, object : PostRepository.Callback<Post> {
            override fun onSuccess(posts: Post) {
                actualLikedByMe[id] = true
                _postCreated.value = Unit
                loadPosts()
            }

            override fun onError(e: ApiError) {
                _postCreateError.value = e
            }
        })
    }

    fun unlikeById(id: Long) {

        repository.unlikeById(id, object : PostRepository.Callback<Post> {
            override fun onSuccess(posts: Post) {
                actualLikedByMe[id] = false
                _postCreated.value = Unit
                loadPosts()
            }

            override fun onError(e: ApiError) {
                _postCreateError.value = e
            }
        })
    }

    fun deleteById(id: Long) {

        val old = _data.value?.posts.orEmpty()
        _data.postValue(
            _data.value?.copy(posts = _data.value?.posts.orEmpty()
                .filter { it.id != id }
            )
        )

        repository.deleteById(id, object : PostRepository.Callback<Unit> {
            override fun onSuccess(posts: Unit) {
                loadPosts()
            }

            override fun onError(e: ApiError) {
                _data.postValue(_data.value?.copy(posts = old))
                _data.value = FeedModel(errorVisible = true, error = e)
            }
        })
    }
}
