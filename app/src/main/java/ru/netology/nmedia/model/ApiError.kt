package ru.netology.nmedia.model

import android.content.res.Resources
import java.net.ConnectException

sealed class ApiError {
    object ServerError : ApiError()
    object NetworkError : ApiError()
    object UnknownError : ApiError()

    companion object {
        fun fromThrowable(throwable: Throwable): ApiError =
            when (throwable) {
                is ApiException -> throwable.error
                is ConnectException -> NetworkError
                else -> UnknownError
            }
    }
}

fun ApiError?.getHumanReadableMessage(resources: Resources): String =
    when (this) {
        ApiError.ServerError -> "Ошибка сервера"
        ApiError.NetworkError -> "Ошибка сети"
        ApiError.UnknownError, null -> "Неизвестная ошибка"
    }
