package ru.netology.nmedia.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post

@Entity
data class PostWorkerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val authorId: Long,
    val author: String,
    val authorAvatar: String,
    val content: String,
    val published: String,
    @ColumnInfo(name = "likeByMe")
    val likedByMe: Boolean,
    @ColumnInfo(name = "numberOfLikes")
    val likes: Int = 0,
    val showOrNot: Boolean = false,
    val newPost: Boolean = false,
    val ownedByMe: Boolean = false,
    @Embedded
    var attachment: AttachmentEmbeddable?,
    var uri: String? = null
) {

    fun toDto() = Post(
        id,
        authorId,
        author,
        authorAvatar,
        content,
        published,
        likedByMe,
        likes,
        showOrNot,
        newPost,
        ownedByMe,
        attachment?.toDto()
    )

    companion object {
        fun fromDto(dto: Post) =
            PostWorkerEntity(
                dto.id,
                dto.authorId,
                dto.author,
                dto.authorAvatar,
                dto.content,
                dto.published,
                dto.likeByMe,
                dto.numberOfLikes,
                true,
                dto.newPost,
                dto.ownedByMe,
                AttachmentEmbeddable.fromDto(dto.attachment)
            )
    }
}
