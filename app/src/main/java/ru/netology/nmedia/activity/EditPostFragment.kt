package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentEditPostBinding
import ru.netology.nmedia.model.getHumanReadableMessage
import ru.netology.nmedia.utils.Utils
import ru.netology.nmedia.viewmodel.PostViewModel

class EditPostFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentEditPostBinding.inflate(
            inflater,
            container,
            false
        )

        val textForEdit = arguments?.getString("content").toString()
        val postId = arguments?.getLong("postId")

        binding.etInputArea.setText(textForEdit)

        binding.etInputArea.requestFocus()

        binding.fabConfirmation.setOnClickListener {
            if (binding.etInputArea.text.isNullOrBlank()) {
                Utils.hideKeyboard(requireView())
                findNavController().navigateUp()
                return@setOnClickListener
            }

            val content = binding.etInputArea.text.toString()
            if (postId != null) {
                viewModel.changeContent(postId, content)
            }
            viewModel.postCreation()

            Utils.hideKeyboard(requireView())

            serverErrorHandler()
        }

        binding.mbCancelEditing.setOnClickListener {
            Utils.hideKeyboard(requireView())
            findNavController().navigateUp()
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
            findNavController().navigate(R.id.mainFragment)
        }
    }

}