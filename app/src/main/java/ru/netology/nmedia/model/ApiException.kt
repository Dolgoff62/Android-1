package ru.netology.nmedia.model

import ru.netology.nmedia.error.ApiError
import java.io.IOException

class ApiException(val error: ApiError, throwable: Throwable? = null) : IOException(throwable)