package ru.netology.nmedia.viewmodel

import ru.netology.nmedia.dto.AuthUser
import ru.netology.nmedia.utils.Utils

data class UserModel(
    val user: AuthUser = Utils.EmptyUser.emptyUser,
    val error: Boolean = false
)
