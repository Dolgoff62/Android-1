package ru.netology.nmedia.activity

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.utils.Utils
import ru.netology.nmedia.viewmodel.CardViewModel
import ru.netology.nmedia.viewmodel.PostViewModel


class NewPostFragment : Fragment() {

    companion object {
        var Bundle.textArg: String? by Utils.StringArg
    }

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    private val cardPostViewModel: CardViewModel by viewModels()

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
                saveDraft(prefs, binding.etInputArea.text.toString())
                findNavController().navigateUp()
            }
        }

        restoreDraft(prefs, binding)

        arguments?.textArg
            ?.let(binding.etInputArea::setText)

        binding.etInputArea.requestFocus()
        callback.isEnabled

<<<<<<< HEAD
        cardPostViewModel.post.observe(viewLifecycleOwner, { state ->
            if (state.error) {
                Toast.makeText(
                    requireContext(),
                    R.string.error_loading,
                    Toast.LENGTH_LONG
                )
                    .show()
            } else {
                viewModel.postCreated.observe(viewLifecycleOwner) {
                    viewModel.loadPosts()
                    findNavController().navigateUp()
                }
            }
        })

=======
>>>>>>> 74f650ad9a25f8190adc488187d899c354210929
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
<<<<<<< HEAD
                findNavController().navigateUp()
=======
>>>>>>> 74f650ad9a25f8190adc488187d899c354210929
            }
        }

        serverErrorHandler()

        return binding.root
    }
}

<<<<<<< HEAD
private fun restoreDraft(
    prefs: SharedPreferences?,
    binding: FragmentNewPostBinding
) {
    val draftText = prefs?.getString("draftText", "")
=======
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
>>>>>>> 74f650ad9a25f8190adc488187d899c354210929

    if (draftText != "") {
        binding.etInputArea.setText(draftText)
    }
}

private fun saveDraft(prefs: SharedPreferences, text: String) {
    val editor = prefs.edit()
    editor.putString("draftText", text)
    editor.apply()
}

private fun clearDraft(prefs: SharedPreferences) {
    val editor = prefs.edit()
    editor.remove("draftText")
    editor.apply()
}
