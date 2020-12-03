package ru.netology.nmedia.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity

@Dao
interface PostDao {

    @Query("""SELECT * FROM PostEntity ORDER BY id DESC""")
    fun getAll(): LiveData<List<PostEntity>>

    @Query(
        """
           UPDATE PostEntity SET
               numberOfLikes = numberOfLikes + CASE WHEN likeByMe THEN -1 ELSE 1 END,
               likeByMe = CASE WHEN likeByMe THEN 0 ELSE 1 END
           WHERE id = :id;
        """
    )
    fun likeById(id: Long)

    @Query(
        """
           UPDATE PostEntity SET
               numberOfShare = numberOfShare + 1
           WHERE id = :id;
        """
    )
    fun toShareById(id: Long)

    @Query(
        """
        UPDATE PostEntity SET
            numberOfViews = numberOfViews + 1
        WHERE id = :id
        """
    )
    fun increasingNumberOfViews(id: Long)

    @Insert
    fun insert(post: PostEntity)

    @Query("UPDATE PostEntity SET content = :content, video = :video, edited = :edited WHERE id = :id")
    fun updateContentById(id: Long, content: String, video: String, edited: String)

    fun postCreation(post: PostEntity) =
        if (post.id == 0L) insert(post) else updateContentById(
            post.id,
            post.content,
            post.video,
            post.edited
        )


    @Query("""DELETE FROM PostEntity WHERE id = :id""")
    fun deleteById(id: Long)

    @Query("""SELECT * FROM PostEntity WHERE id = :id""")
    fun findPostById(id: Long): Post
}