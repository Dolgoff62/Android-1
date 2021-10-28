package ru.netology.nmedia.service

import com.google.firebase.iid.FirebaseInstanceId
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object FCMModule {
    @Provides
    @Singleton
    fun provideFirebaseMessaging() : FirebaseInstanceId {
        return FirebaseInstanceId.getInstance()
    }
}