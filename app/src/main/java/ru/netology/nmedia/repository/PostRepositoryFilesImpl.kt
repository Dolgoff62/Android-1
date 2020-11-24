package ru.netology.nmedia.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.utils.Utils

class PostRepositoryFilesImpl(
    private val context: Context
) : PostRepository {

    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private val file = context.filesDir.resolve("posts.json")
    private val currentUser = " Дмитрий Долгов"
    private var posts = run {
        if (!file.exists()) return@run emptyList<Post>()
        file.readText()
            .ifBlank {
                return@run emptyList<Post>()
            }
            .let {
                gson.fromJson(it, type)
            }
    }
    var nextId = if (posts.isEmpty()) 1 else (posts.first().id + 1)

    private val data = MutableLiveData(posts)

    override fun getAll(): LiveData<List<Post>> = data

    override fun postCreation(post: Post) {
        if (post.id == 0L) {
            posts = listOf(
                post.copy(
                    id = nextId++,
                    author = currentUser,
                    published = Utils.localDateTime(),
                    likeByMe = false,
                    numberOfLikes = 0,
                    numberOfShare = 0,
                    numberOfViews = 0,
                )
            ) + posts
            update()
            return
        }

        posts = posts.map {
            if (it.id != post.id) it else it.copy(content = post.content, video = post.video)
        }
        update()
    }

    override fun deleteById(id: Long) {
        posts = posts.filter { it.id != id }
        update()
    }

    override fun likeById(id: Long) {
        posts = posts.map {
            if (it.id != id) {
                it
            } else {
                it.copy(
                    likeByMe = !it.likeByMe,
                    numberOfLikes = if (it.likeByMe) it.numberOfLikes - 1 else it.numberOfLikes + 1
                )
            }
        }
        update()
    }

    override fun toShareById(id: Long) {
        posts = posts.map {
            if (it.id == id) it.copy(numberOfShare = it.numberOfShare + 1) else it
        }
        update()
    }

    private fun sync() {
        context.openFileOutput("posts.json", Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(gson.toJson(posts))
        }
    }

    override fun findPostById(id: Long): Post {
        return posts.first { it.id == id }
    }

    private fun update() {
        data.value = posts
        sync()
    }
}
