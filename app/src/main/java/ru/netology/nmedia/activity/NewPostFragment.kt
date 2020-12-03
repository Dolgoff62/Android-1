package ru.netology.nmedia.activity

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_new_post.*
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentNewPostBinding
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
            if (binding.etInputArea.text.isNullOrBlank() && binding.etPostVideoLink.text.isNullOrBlank()) {
                Utils.hideKeyboard(requireView())
                findNavController().navigateUp()
            } else {
                val videoLink = binding.etPostVideoLink.text.toString()
                if (videoLink != "" && !Utils.urlValidChecker(videoLink)) {
                    Toast.makeText(
                        activity,
                        getString(R.string.error_url_validation),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                } else {
                    viewModel.changeContent(
                        binding.etInputArea.text.toString(),
                        binding.etPostVideoLink.text.toString(),
                        dateOfEditing = ""
                    )
                    viewModel.postCreation()
                    if (prefs != null) {
                        clearDraft(prefs)
                    }
                    Utils.hideKeyboard(requireView())
                    findNavController().navigateUp()
                }
            }
        }
        return binding.root
    }

    private fun restoreDraft(
        prefs: SharedPreferences?,
        binding: FragmentNewPostBinding
    ) {
        val draftText = prefs?.getString("draftText", "")
        val draftVideoLink = prefs?.getString("draftVideoLink", "")

        if (draftText != "") {
            binding.etInputArea.setText(draftText)
        }

        if (draftVideoLink != "") {
            binding.etPostVideoLink.setText(draftVideoLink)
        }
    }

    private fun saveDraft(prefs: SharedPreferences) {
        val editor = prefs.edit()
        editor.putString("draftText", etInputArea.text.toString())
        editor.putString("draftVideoLink", etPostVideoLink.text.toString())
        editor.apply()
    }

    private fun clearDraft(prefs: SharedPreferences) {
        val editor = prefs.edit()
        editor.remove("draftText")
        editor.remove("draftVideoLink")
        editor.apply()
    }
}