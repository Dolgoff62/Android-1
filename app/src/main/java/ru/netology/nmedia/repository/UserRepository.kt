package ru.netology.nmedia.repository

import ru.netology.nmedia.dto.*

interface UserRepository {
    suspend fun updateUser(login: String, pass: String): AuthUser
    suspend fun registrationUser(login: String, pass: String, name: String): AuthUser
    suspend fun registrationUserWithAvatar(
        login: String,
        pass: String,
        name: String,
        upload: MediaUpload?
    ): AuthUser

    suspend fun upload(upload: MediaUpload): Media
}