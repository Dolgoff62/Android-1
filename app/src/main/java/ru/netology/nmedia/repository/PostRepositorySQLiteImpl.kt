package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post

class PostRepositorySQLiteImpl(
    private val dao: PostDao
) : PostRepository {
    private var posts = emptyList<Post>()
    private val data = MutableLiveData(posts)

    init {
        posts = dao.getAll()
        data.value = posts
    }

    override fun getAll(): LiveData<List<Post>> = data

    override fun postCreation(post: Post) {
        val id = post.id
        val saved = dao.postCreation(post)
        posts = if (id == 0L) {
            listOf(saved) + posts
        } else {
            posts.map {
                if (it.id != id) it else saved
            }
        }
        data.value = posts
    }

    override fun likeById(id: Long) {
        dao.likeById(id)
        posts = posts.map {
            if (it.id != id) it else it.copy(
                likeByMe = !it.likeByMe,
                numberOfLikes = if (it.likeByMe) it.numberOfLikes - 1 else it.numberOfLikes + 1
            )
        }
        data.value = posts
    }

    override fun toShareById(id: Long) {
        dao.toShareById(id)
        posts = posts.map {
            if (it.id != id) it else it.copy(
                numberOfShare = it.numberOfShare + 1
            )
        }
        data.value = posts
    }

    override fun deleteById(id: Long) {
        dao.deleteById(id)
        posts = posts.filter { it.id != id }
        data.value = posts
    }

    override fun findPostById(id: Long): Post {
        dao.findPostById(id)
        return posts.first { it.id == id }
    }
}