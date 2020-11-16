package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_edit_post.*
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityEditPostBinding
import ru.netology.nmedia.utils.Utils

class EditPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val arguments = intent.extras
        val textForEdit = arguments!!["content"].toString()
        val videoLinkForEdit = arguments["videoLink"].toString()
        val binding = ActivityEditPostBinding.inflate(layoutInflater)

        setContentView(binding.root)
        etInputArea.setText(textForEdit)
        etEditedPostVideoLink.setText(videoLinkForEdit)
        binding.etInputArea.requestFocus()
        binding.fabConfirmation.setOnClickListener {
            val intent = Intent()
            if (binding.etInputArea.text.isNullOrBlank()) {
                setResult(Activity.RESULT_CANCELED, intent)
            } else {
                val content = binding.etInputArea.text.toString()
                val videoLink = binding.etEditedPostVideoLink.text.toString()
                if (videoLink != "" && !Utils.urlValidChecker(videoLink)) {
                    Toast.makeText(
                        this@EditPostActivity,
                        getString(R.string.error_url_validation),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                } else {
                    intent.putExtra("content", content)
                    intent.putExtra("videoLink", videoLink)
                    setResult(Activity.RESULT_OK, intent)
                }
                finish()
            }
        }

        binding.mbCancelEditing.setOnClickListener {
            setResult(Activity.RESULT_CANCELED, intent)
            finish()
        }
    }
}