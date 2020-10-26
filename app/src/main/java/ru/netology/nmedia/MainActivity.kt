package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.utils.Utils
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()

        viewModel.data.observe(this) { post ->
            with(binding) {
                logo.setImageResource(post.avatar)
                tvAuthorPost.text = post.author
                published.text = post.published
                content.text = post.content
                numberOfLikes.text = Utils.formatLikes(post.numberOfLikes)
                numberOfShare.text = Utils.formatLikes(post.numberOfShare)
                numberOfViews.text = Utils.formatLikes(post.numberOfViews)
                likeButton.setImageResource(
                    if (post.likeByMe) {
                        R.drawable.ic_baseline_favorite_24
                    } else {
                        R.drawable.ic_baseline_favorite_border_24
                    }
                )
            }

        binding.likeButton.setOnClickListener {
            viewModel.like()
        }

        binding.toShareButton.setOnClickListener {
            viewModel.toShare()
        }
        }
    }
}