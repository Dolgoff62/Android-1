package ru.netology.nmedia.viewmodel

import android.net.Uri
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.netology.nmedia.api.PostApi
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.auth.AuthState
import ru.netology.nmedia.dto.MediaUpload
import ru.netology.nmedia.model.AvatarModel
import ru.netology.nmedia.repository.UserRepository
import ru.netology.nmedia.repository.UserRepositoryImpl
import ru.netology.nmedia.util.SingleLiveEvent
import ru.netology.nmedia.utils.Utils
import java.io.File
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(private val auth: AppAuth, postApi: PostApi) : ViewModel() {

    private val repository: UserRepository = UserRepositoryImpl(postApi)

    private val noAvatar = AvatarModel()

    val data: LiveData<AuthState> = auth
        .authStateFlow
        .asLiveData(Dispatchers.Default)

    val authenticated: Boolean
        get() = auth.authStateFlow.value.id != 0L

    private val _user = SingleLiveEvent<UserModel>()
    val user: LiveData<UserModel>
        get() = _user

    private val _avatar = MutableLiveData(noAvatar)
    val avatar: LiveData<AvatarModel>
        get() = _avatar

    fun changeAvatar(uri: Uri?, file: File?) {
        _avatar.value = AvatarModel(uri, file)
    }

    fun registrationUser(login: String, pass: String, name: String) = viewModelScope.launch {
        try {
            _user.value = UserModel(repository.registrationUser(login, pass, name))
        } catch (e: Exception) {
            _user.value = UserModel(error = true, user = Utils.EmptyUser.emptyUser)
        }
    }

    fun registrationUserWithAvatar(
        login: String,
        pass: String,
        name: String
    ) = viewModelScope.launch {
        try {
            val regUser = _avatar.value?.file?.let { file ->
                repository.registrationUserWithAvatar(
                    login,
                    pass,
                    name,
                    MediaUpload(file)
                )
            }
            _user.value = regUser?.let { UserModel(it) }
        } catch (e: Exception) {
            _user.value = UserModel(error = true, user = Utils.EmptyUser.emptyUser)
        }
    }

    fun updateUserAuth(login: String, pass: String) = viewModelScope.launch {
        try {
            _user.value = UserModel(repository.updateUser(login, pass))
        } catch (e: Exception) {
            _user.value = UserModel(error = true, user = Utils.EmptyUser.emptyUser)
        }
    }
}
