package ru.netology.nmedia.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dao.PostWorkerDao
import ru.netology.nmedia.entity.PostEntity
import ru.netology.nmedia.entity.PostWorkerEntity

@Database(
    entities = [PostEntity::class, PostWorkerEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDb : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun postWorkerDao(): PostWorkerDao
}

