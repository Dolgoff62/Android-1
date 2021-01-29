package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentPostCardBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.getHumanReadableMessage
import ru.netology.nmedia.utils.Utils
import ru.netology.nmedia.viewmodel.PostViewModel


class PostCardFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        var post = Post(
            id = arguments?.getLong("postId") as Long,
            author = arguments?.getString("author") as String,
            authorAvatar = arguments?.getString("authorAvatar") as String,
            content = arguments?.getString("content") as String,
            published = arguments?.getString("published") as String,
            likeByMe = arguments?.getBoolean("likeByMe") as Boolean,
            numberOfLikes = arguments?.getInt("numberOfLikes") as Int,
            attachment = arguments?.getParcelable("attachment")
        )

        serverErrorHandler()

        val binding = FragmentPostCardBinding.inflate(
            inflater,
            container,
            false
        )

        binding.apply {
            tvAuthorPostFragment.text = post.author

            val url = "http://10.0.2.2:9999/avatars/${post.authorAvatar}"
            val urlAttach = "http://10.0.2.2:9999/images/${post.attachment?.url}"

            if (post.authorAvatar != "") {
                Glide.with(binding.logoPostFragment.context)
                    .load(url)
                    .circleCrop()
                    .timeout(30_000)
                    .into(binding.logoPostFragment)
            }

            publishedPostFragment.text = post.published
            contentPostFragment.text = post.content
            likeButtonPostFragment.text = Utils.formatLikes(post.numberOfLikes)
            likeButtonPostFragment.isChecked = post.likeByMe

            if (post.attachment != null) {
                binding.fragmentPostImageAttachment.visibility = View.VISIBLE
                Glide.with(binding.fragmentPostImageAttachment.context)
                    .load(urlAttach)
                    .timeout(30_000)
                    .into(binding.fragmentPostImageAttachment)
            }

            binding.likeButtonPostFragment.setOnClickListener {
                if (post.likeByMe) {
                    viewModel.likeById(post.id)
                } else {
                    viewModel.unlikeById(post.id)
                }
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
        }
        return binding.root
    }

    private fun serverErrorHandler() {
        viewModel.postCreateError.observe(viewLifecycleOwner) {
            Toast.makeText(
                requireContext(),
                it.getHumanReadableMessage(resources),
                Toast.LENGTH_LONG
            )
                .show()
        }
        viewModel.postCreated.observe(viewLifecycleOwner) {
            viewModel.loadPosts()
        }
    }
}
