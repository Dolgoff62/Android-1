package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.OnItemClickListener
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.utils.Utils
import ru.netology.nmedia.viewmodel.PostViewModel

@Suppress("DUPLICATE_LABEL_IN_WHEN")
class MainActivity : AppCompatActivity() {
    private val viewModel: PostViewModel by viewModels()
    private val newPostRequestCode = 1
    private val editPostRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = PostAdapter(object : OnItemClickListener {

            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onShare(post: Post) {
                viewModel.toShareById(post.id)
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }

                val shareIntent =
                    Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(shareIntent)
            }

            override fun onEdit(post: Post) {
                val intent = Intent(this@MainActivity, EditPostActivity::class.java)
                intent.putExtra("content", post.content)
                intent.putExtra("videoLink", post.video)
                startActivityForResult(intent, editPostRequestCode)
                viewModel.edit(post)
            }

            override fun onDelete(post: Post) {
                viewModel.deleteById(post.id)
            }

            override fun onPlayVideo(post: Post) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video))
                val playVideoValidation = Utils.startIntent(this@MainActivity, intent)
                if (!playVideoValidation) {
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.error_play_video_validation),
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                } else {
                    startActivity(intent)
                }
            }
        })

        binding.rvPosts.adapter = adapter
        viewModel.data.observe(this, { posts ->
            adapter.submitList(posts)
        })

        binding.fabAddNewPost.setOnClickListener {
            val intent = Intent(this@MainActivity, NewPostActivity::class.java)
            startActivityForResult(intent, newPostRequestCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            newPostRequestCode -> {
                if (resultCode != Activity.RESULT_OK) {
                    return
                }
                data?.extras.let {
                    val postContent = it?.get("content").toString()
                    val videoLink = it?.get("videoLink").toString()
                    viewModel.changeContent(postContent, videoLink)
                    viewModel.postCreation()
                }
            }
            editPostRequestCode -> {
                if (resultCode != Activity.RESULT_OK) {
                    return
                } else {
                    data?.extras.let {
                        val postContent = it?.get("content").toString()
                        val videoLink = it?.get("videoLink").toString()
                        viewModel.changeContent(postContent, videoLink)
                    }
                }
            }
        }
    }
}