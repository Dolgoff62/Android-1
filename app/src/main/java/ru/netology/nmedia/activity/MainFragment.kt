package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.OnItemClickListener
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.FragmentMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel


@Suppress("DUPLICATE_LABEL_IN_WHEN")
class MainFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentMainBinding.inflate(
            inflater,
            container,
            false
        )

        binding.swiperefresh.setOnRefreshListener {
            if (!binding.progress.isAnimating) {
                viewModel.loadPosts()
            }
            binding.swiperefresh.isRefreshing = false
        }

        val adapter = PostAdapter(object : OnItemClickListener {

            override fun onLike(post: Post) {
                if (!post.likeByMe) {
                    viewModel.likeById(post.id)

                } else {
                    viewModel.unlikeById(post.id)
                }
            }

            override fun onEdit(post: Post) {
                val bundle = Bundle().apply {
                    putString("content", post.content)
                    putLong("postId", post.id)
                }
                findNavController().navigate(R.id.action_mainFragment_to_editPostFragment, bundle)
            }

            override fun onDelete(post: Post) {
                viewModel.deleteById(post.id)
            }

            override fun onPost(post: Post) {
                val bundle = Bundle().apply {
                    putParcelable("post", post)
                }
                findNavController().navigate(R.id.action_mainFragment_to_postCardFragment, bundle)
            }
        })

        binding.rvPosts.adapter = adapter
        viewModel.dataState.observe(viewLifecycleOwner, { state ->
            binding.progress.isVisible = state.loading
            binding.swiperefresh.isRefreshing = state.refreshing
            if (state.error) {
                Snackbar.make(binding.root, R.string.error_loading, Snackbar.LENGTH_LONG)
                    .setAction(R.string.retry_loading) { viewModel.loadPosts() }
                    .show()
            }
        })
        viewModel.data.observe(viewLifecycleOwner, { state ->
            adapter.submitList(state.posts)
            binding.emptyText.isVisible = state.empty
        })


//        binding.rvPosts.adapter = adapter
//        viewModel.data.observe(viewLifecycleOwner, { state ->
//            adapter.submitList(state.posts)
//            binding.progress.isVisible = state.loading
//            binding.emptyText.isVisible = state.empty
//            binding.errorGroup.isVisible = state.errorVisible
//            binding.retryTitle.text = state.error.getHumanReadableMessage(resources)
//        })

        binding.fabAddNewPost.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_newPostFragment)
        }

//        viewModel.postCreateError.observe(viewLifecycleOwner) {
//            Toast.makeText(
//                requireContext(),
//                it.getHumanReadableMessage(resources),
//                Toast.LENGTH_LONG
//            )
//                .show()
//        }

        return binding.root
    }
}