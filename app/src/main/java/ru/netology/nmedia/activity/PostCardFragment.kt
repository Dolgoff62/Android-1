package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentPostCardBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.utils.Utils
import ru.netology.nmedia.viewmodel.PostViewModel


class PostCardFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val post = arguments?.getParcelable<Post>("post") as Post

        val binding = FragmentPostCardBinding.inflate(
            inflater,
            container,
            false
        )
        binding.apply {
            tvAuthorPostFragment.text = post.author
            publishedPostFragment.text = post.published
            if (post.edited == "") {
                tvEditedPostFragment.visibility = View.GONE
            } else {
                tvEditedPostFragment.text = post.edited
                tvEditedPostFragment.visibility = View.VISIBLE
            }
            contentPostFragment.text = post.content
            likeButtonPostFragment.text = Utils.formatLikes(post.numberOfLikes)
            toShareButtonPostFragment.text = Utils.formatLikes(post.numberOfShare)
            numberOfViewsPostFragment.text = Utils.formatLikes(post.numberOfViews)
            likeButtonPostFragment.isChecked = post.likeByMe

            if (post.video == "") {
                videoPostFragment.visibility = View.GONE
            } else {
                videoPostFragment.visibility = View.VISIBLE
            }
        }

        binding.likeButtonPostFragment.setOnClickListener {
            viewModel.likeById(post.id)
            val updatedPost = viewModel.searchPost(post.id)
            binding.likeButtonPostFragment.text = updatedPost.numberOfLikes.toString()
        }

        binding.toShareButtonPostFragment.setOnClickListener {
            viewModel.toShareById(post.id)
            val updatedPost = viewModel.searchPost(post.id)
            binding.toShareButtonPostFragment.text = updatedPost.numberOfShare.toString()
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, post.content)
                type = "text/plain"
            }
            val shareIntent =
                Intent.createChooser(intent, getString(R.string.chooser_share_post))
            startActivity(shareIntent)
        }

        binding.videoPostFragment.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video))
            val playVideoValidation = context?.let { Utils.startIntent(it, intent) }
            if (playVideoValidation == false) {
                Toast.makeText(
                    activity,
                    getString(R.string.error_play_video_validation),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            startActivity(intent)
        }

        binding.ibMenuPostFragment.setOnClickListener {
            PopupMenu(it.context, it).apply {
                inflate(R.menu.menu_main)
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.menuItemDelete -> {
                            viewModel.deleteById(post.id)
                            findNavController().navigateUp()
                            true
                        }
                        R.id.menuItemEdit -> {
                            val bundle = Bundle().apply {
                                putString("content", post.content)
                                putString("videoLink", post.video)
                                putLong("postId", post.id)
                            }
                            findNavController().navigate(
                                R.id.action_postCardFragment_to_editPostFragment,
                                bundle
                            )
                            true
                        }
                        else -> false
                    }
                }
            }.show()
        }
        return binding.root
    }
}
