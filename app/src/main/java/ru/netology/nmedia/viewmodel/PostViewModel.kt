package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryFilesImpl

private val empty = Post(
    id = 0,
    avatar = 0,
    content = "",
    author = "",
    likeByMe = false,
    published = "",
    numberOfLikes = 0,
    numberOfShare = 0,
    numberOfViews = 0,
    video = ""
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryFilesImpl(application)
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

    fun changeContent(content: String, videoLink: String) {
        val text = content.trim()
        val link = videoLink.trim()
        if (edited.value?.content == text && edited.value?.video == link) {
            return
        }
        edited.value = edited.value?.copy(content = text, video = link)
    }

    fun deleteById(id: Long) = repository.deleteById(id)

    fun likeById(id: Long) = repository.likeById(id)

    fun toShareById(id: Long) = repository.toShareById(id)
}
