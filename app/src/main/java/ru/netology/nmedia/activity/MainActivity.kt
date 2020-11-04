package ru.netology.nmedia.activity

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewmodel.PostViewModel
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.adapter.OnItemClickListener
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.utils.Utils

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        val adapter = PostAdapter(object: OnItemClickListener {

            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onShare(post: Post) {
                viewModel.toShareById(post.id)
            }

            override fun onEdit(post: Post) {
                viewModel.edit(post)
            }

            override fun onDelete(post: Post) {
                viewModel.deleteById(post.id)
            }

            override fun onCancelEdit(post: Post) {
                viewModel.cancelEdit()
            }
        })

        binding.rvPosts.adapter = adapter
        viewModel.data.observe(this, { posts ->
            adapter.submitList(posts)
        })

        viewModel.edited.observe(this) {
            if (it.id == 0L) {
                return@observe
            }
            binding.tvPostOnUndoEditing.text = it.content
            group.visibility = View.VISIBLE
            with(binding.etInputArea) {
                requestFocus()
                setText(it.content)
            }
        }

        binding.ibConfirmation.setOnClickListener {
            with(binding.etInputArea) {
                if (TextUtils.isEmpty(text)) {
                    Toast.makeText(
                        this@MainActivity,
                        context.getString(R.string.error_empty_content),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                viewModel.changeContent(text.toString())
                viewModel.postCreation()

                setText("")
                group.visibility = View.GONE
                clearFocus()
                Utils.hideKeyboard(this)
            }
        }

        binding.ibCancelEditing.setOnClickListener {
            with(binding.etInputArea) {
                viewModel.cancelEdit()
                setText("")
                group.visibility = View.GONE
                clearFocus()
                Utils.hideKeyboard(this)
            }
        }
    }
}