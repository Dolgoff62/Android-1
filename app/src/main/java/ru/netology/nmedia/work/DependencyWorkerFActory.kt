package ru.netology.nmedia.work

import androidx.work.DelegatingWorkerFactory
import ru.netology.nmedia.dao.PostRemoteKeyDao
import ru.netology.nmedia.repository.PostRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DependencyWorkerFactory @Inject constructor(
    repository: PostRepository,
    remoteKeyDao: PostRemoteKeyDao
) : DelegatingWorkerFactory() {
    init {
        addFactory(RefreshPostsWorkerFactory(repository))
        addFactory(SavePostsWorkerFactory(repository))
        addFactory(DeletePostsWorkerFactory(repository))
    }
}
