package ru.netology.nmedia.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity

@Dao
interface PostDao {
    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
    fun getAll(): LiveData<List<PostEntity>>

    @Query("""SELECT * FROM PostEntity WHERE id = :id""")
    suspend fun getPostById(id: Long): Post

    @Query("SELECT COUNT(*) == 0 FROM PostEntity")
    suspend fun isEmpty(): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: PostEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(posts: List<PostEntity>)

    @Query(
        """
           UPDATE PostEntity SET
               numberOfLikes = numberOfLikes + CASE WHEN likeByMe THEN -1 ELSE 1 END,
               likeByMe = CASE WHEN likeByMe THEN 0 ELSE 1 END
           WHERE id = :id;
        """
    )
    suspend fun likeById(id: Long)


    @Query("DELETE FROM PostEntity WHERE id = :id")
    suspend fun deleteById(id: Long)
}
