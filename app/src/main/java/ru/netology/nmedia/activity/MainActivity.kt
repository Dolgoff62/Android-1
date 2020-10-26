package ru.netology.nmedia.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewmodel.PostViewModel
import ru.netology.nmedia.adapter.PostAdapter

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        val adapter = PostAdapter({viewModel.likeById(it.id)}) {
            viewModel.toShareById(it.id)
        }
        binding.rvPosts.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.list = posts
        }
    }
}