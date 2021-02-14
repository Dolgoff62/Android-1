package ru.netology.nmedia.model

<<<<<<< HEAD
sealed class AppError(var code: String): RuntimeException()
class ApiError(val status: Int, code: String): AppError(code)
object NetworkError : AppError("error_network")
object UnknownError: AppError("error_unknown")
=======
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
>>>>>>> 74f650ad9a25f8190adc488187d899c354210929
