package ru.netology.nmedia.application

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import dagger.Provides
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.work.DeletePostWorker
import ru.netology.nmedia.work.DependencyWorkerFactory
import ru.netology.nmedia.work.RefreshPostsWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class NMediaApplication : Application(), Configuration.Provider {
    private val appScope = CoroutineScope(Dispatchers.Default)

    @Inject
    lateinit var auth: AppAuth

    @Inject
    lateinit var workManager: WorkManager

    @Inject
    lateinit var workerFactory: DependencyWorkerFactory

    init {
        DeletePostWorker
    }

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .build()

    override fun onCreate() {
        super.onCreate()
        setupAuth()
    }

    private fun setupAuth() {
        appScope.launch {
            auth.sendPushToken()
        }
    }
}
