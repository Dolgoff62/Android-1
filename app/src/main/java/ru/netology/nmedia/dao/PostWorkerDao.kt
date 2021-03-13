package ru.netology.nmedia.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.netology.nmedia.entity.PostWorkerEntity

@Dao
interface PostWorkerDao {
    @Query("SELECT * FROM PostWorkerEntity WHERE id = :id")
    suspend fun getPostById(id: Long): PostWorkerEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: PostWorkerEntity): Long

    @Query("DELETE FROM PostWorkerEntity WHERE id = :id")
    suspend fun deleteById(id: Long)
}
