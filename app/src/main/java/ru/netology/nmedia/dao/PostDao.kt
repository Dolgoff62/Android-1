
package ru.netology.nmedia.dao

import ru.netology.nmedia.dto.Post
import java.io.Closeable

interface PostDao {
    fun getAll(): List<Post>
    fun likeById(id: Long)
    fun toShareById(id: Long)
    fun postCreation(post: Post) : Post
    fun deleteById(id: Long)
    fun findPostById(id: Long): Post
}