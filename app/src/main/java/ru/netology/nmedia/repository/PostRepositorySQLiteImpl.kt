package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity
import ru.netology.nmedia.entity.toPost

class PostRepositorySQLiteImpl(
    private val dao: PostDao
) : PostRepository {

    override fun getAll(): LiveData<List<Post>> = dao.getAll().map {
        it.map(PostEntity::toPost)
    }

    override fun postCreation(post: Post) {
        dao.postCreation(PostEntity.fromPost(post))
    }

    override fun likeById(id: Long) {
        dao.likeById(id)
    }

    override fun toShareById(id: Long) {
        dao.toShareById(id)
    }

    override fun deleteById(id: Long) {
        dao.deleteById(id)
    }

    override fun findPostById(id: Long): Post {
        return dao.findPostById(id)
    }

    override fun increasingNumberOfViews(id: Long) {
        return dao.increasingNumberOfViews(id)
    }
}