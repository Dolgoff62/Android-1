package ru.netology.nmedia.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.repository.PostRepositoryImpl

class RefreshPostsWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    companion object {
        const val name = "ru.netology.nmedia.work.RefreshPostsWorker"
    }

    override suspend fun doWork(): Result {
        val repository = PostRepositoryImpl(
            AppDb.getInstance(context = applicationContext).postDao(),
            AppDb.getInstance(context = applicationContext).postWorkerDao()
        )
        return try {
            repository.getAll()
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}