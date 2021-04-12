package ru.netology.nmedia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.nmedia.model.CardPostModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.utils.Utils
import javax.inject.Inject


@HiltViewModel
class CardViewModel @Inject constructor(private val repository: PostRepository) : ViewModel() {

    private val _post = MutableLiveData(
        CardPostModel(
            loading = true,
            post = Utils.EmptyPost.empty
        )
    )
    val post: LiveData<CardPostModel>
        get() = _post

    fun getPostById(id: Long) = viewModelScope.launch {
        try {
            _post.value = CardPostModel(loading = true)
            _post.value = CardPostModel(repository.getPostById(id))
        } catch (e: Exception) {
            _post.value = CardPostModel(error = true, post = Utils.EmptyPost.empty)
        }
    }

    fun likeById(id: Long) = viewModelScope.launch {
        try {
            repository.likeById(id)
            getPostById(id)
        } catch (e: Exception) {
            _post.value = CardPostModel(error = true)
        }
    }

    fun unlikeById(id: Long) = viewModelScope.launch {
        try {
            repository.unlikeById(id)
            getPostById(id)
        } catch (e: Exception) {
            _post.value = CardPostModel(error = true)
        }
    }
}
