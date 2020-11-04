package ru.netology.nmedia.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryInMemoryImpl

private val empty = Post(
    id = 0,
    avatar = 0,
    content = "",
    author = "",
    likeByMe = false,
    published = "",
    numberOfLikes = 0,
    numberOfShare = 0,
    numberOfViews = 0
)

class PostViewModel : ViewModel() {
    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    val data = repository.getAll()
    val edited = MutableLiveData(empty)

    fun postCreation() {
        edited.value?.let {
            repository.postCreation(it)
        }
        edited.value = empty
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }

    fun deleteById(id: Long) = repository.deleteById(id)

    fun likeById(id: Long) = repository.likeById(id)

    fun toShareById(id: Long) = repository.toShareById(id)

    fun cancelEdit() {
        edited.value = edited.value
    }
}
