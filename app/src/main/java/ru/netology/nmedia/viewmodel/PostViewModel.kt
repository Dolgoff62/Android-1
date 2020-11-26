package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositorySQLiteImpl

private val empty = Post(
    id = 0,
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
    private val repository: PostRepository = PostRepositorySQLiteImpl(
        AppDb.getInstance(application).postDao
    )

    val data = repository.getAll()
    val edited = MutableLiveData(empty)


    fun postCreation() {
        edited.value?.let {
            repository.postCreation(it)
        }
        edited.value = empty
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

    fun searchPost(id: Long): Post {
        return repository.findPostById(id)
    }

    fun searchAndChangePost(id: Long) {
        val thisPost = repository.findPostById(id)
        val editedPost =
            thisPost.copy(content = edited.value?.content.toString(), video = edited.value?.video)
        repository.postCreation(editedPost)
    }
}
