package ru.netology.nmedia.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import ru.netology.nmedia.repository.PostRepository
import javax.inject.Inject
import javax.inject.Singleton

@HiltWorker
class SavePostWorker @AssistedInject constructor(
    @Assisted
    appContext: Context,
    @Assisted
    params: WorkerParameters,
    private val repository: PostRepository
) : CoroutineWorker(appContext, params) {

    companion object {
        const val postKey = "ru.netology.nmedia.work.SavePostWorker"
    }

    override suspend fun doWork(): Result {
        val id = inputData.getLong(postKey, 0L)
        if (id == 0L) {
            return Result.failure()
        }
        return try {
            repository.processWork(id)
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}

@Singleton
class SavePostsWorkerFactory @Inject constructor(
    private val repository: PostRepository,
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? = when (workerClassName) {
        SavePostWorker::class.java.name ->
            SavePostWorker(appContext, workerParameters, repository)
        else ->
            null
    }
}
