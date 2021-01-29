package ru.netology.nmedia.activity

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_new_post.*
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.model.getHumanReadableMessage
import ru.netology.nmedia.utils.Utils
import ru.netology.nmedia.viewmodel.PostViewModel


class NewPostFragment : Fragment() {

    companion object {
        var Bundle.textArg: String? by Utils.StringArg
    }

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentNewPostBinding.inflate(
            inflater,
            container,
            false
        )

        val prefs: SharedPreferences? = this.context?.getSharedPreferences("draft", MODE_PRIVATE)

        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (prefs != null) {
                saveDraft(prefs)
                findNavController().navigateUp()
            }
        }

        restoreDraft(prefs, binding)

        arguments?.textArg
            ?.let(binding.etInputArea::setText)

        binding.etInputArea.requestFocus()
        callback.isEnabled

        binding.fabConfirmation.setOnClickListener {
            if (binding.etInputArea.text.isNullOrBlank()) {
                Utils.hideKeyboard(requireView())
                findNavController().navigateUp()
            } else {
                viewModel.changeContent(0, binding.etInputArea.text.toString())
                viewModel.postCreation()
                if (prefs != null) {
                    clearDraft(prefs)
                }
                Utils.hideKeyboard(requireView())
            }
        }

        serverErrorHandler()

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
            findNavController().navigateUp()
        }
    }

    private fun restoreDraft(
        prefs: SharedPreferences?,
        binding: FragmentNewPostBinding
    ) {
        val draftText = prefs?.getString("draftText", "")

        if (draftText != "") {
            binding.etInputArea.setText(draftText)
        }
    }

    private fun saveDraft(prefs: SharedPreferences) {
        val editor = prefs.edit()
        editor.putString("draftText", etInputArea.text.toString())
        editor.apply()
    }

    private fun clearDraft(prefs: SharedPreferences) {
        val editor = prefs.edit()
        editor.remove("draftText")
        editor.apply()
    }
}