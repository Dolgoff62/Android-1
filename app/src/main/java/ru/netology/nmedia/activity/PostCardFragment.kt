package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentPostCardBinding
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

        var post = viewModel.searchPost(arguments?.get("postId") as Long)

        val binding = FragmentPostCardBinding.inflate(
            inflater,
            container,
            false
        )
        binding.apply {
            tvAuthorPostFragment.text = post.author
            publishedPostFragment.text = post.published
            contentPostFragment.text = post.content
            likeButtonPostFragment.text = Utils.formatLikes(post.numberOfLikes)
            likeButtonPostFragment.isChecked = post.likeByMe
        }

        binding.likeButtonPostFragment.setOnClickListener {
            if (!post.likeByMe) {
                viewModel.likeById(post.id)
            } else {
                viewModel.unlikeById(post.id)
            }
            viewModel.data.observe(viewLifecycleOwner, {
                post = viewModel.searchPost(post.id)
                binding.likeButtonPostFragment.text = Utils.formatLikes(post.numberOfLikes)
            })
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
        return binding.root
    }
}
