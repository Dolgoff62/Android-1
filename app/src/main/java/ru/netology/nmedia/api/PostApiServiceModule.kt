package ru.netology.nmedia.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.netology.nmedia.auth.AppAuth
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object PostApiServiceModule {
    @Provides
    @Singleton
    fun provideApiService(auth: AppAuth): PostApi {
        return retrofit(okhttp(loggingInterceptor(), authInterceptor(auth)))
            .create(PostApi::class.java)
    }
}