package ru.netology.nmedia.utils

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils.isEmpty
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.Nullable
import ru.netology.nmedia.dto.AuthUser
import ru.netology.nmedia.dto.Post
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.floor
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


class Utils {

    companion object {
        fun formatLikes(count: Int): String {
            return when (count) {
                in 0..999 -> count.toString()
                in 1000..1099 -> String.format("%d", count / 1000) + "K"
                in 1100..9999 -> String.format("%.1f", (floor(count / 100.toDouble())) / 10) + "K"
                in 10_000..999_999 -> String.format("%d", count / 1000) + "K"
                in 1_000_000..999_999_999 ->
                    String.format("%.1f", (floor(count / 100_000.toDouble())) / 10) + "M"
                else -> "> 1 Bn"
            }
        }

        fun convertDate(dateInMilliseconds: String): String {
            return try {
                val sdf = SimpleDateFormat("dd MMMM yyyy  hh:mm", Locale.getDefault())
                val netDate = Date(dateInMilliseconds.toLong() * 1000)
                sdf.format(netDate)
            } catch (e: Exception) {
                e.toString()
            }
        }

        fun hideKeyboard(view: View) {
            val imm =
                view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun startIntent(context: Context, @Nullable intent: Intent?): Boolean {
            if (intent == null) {
                return false
            }
            val pm = context.packageManager
            val resolveInfo = pm.resolveActivity(intent, 0)
            if (resolveInfo != null && resolveInfo.activityInfo != null && !isEmpty(resolveInfo.activityInfo.name)
                && !isEmpty(resolveInfo.activityInfo.packageName)
            ) {
                context.startActivity(intent)
                return true
            }
            return false
        }
    }

    object EmptyPost {
        val empty = Post(
            id = 0L,
            authorId = 0L,
            author = "",
            authorAvatar = "",
            content = "",
            published = "",
            likeByMe = false,
            numberOfLikes = 0,
            attachment = null
        )
    }

    object EmptyUser {
        val emptyUser = AuthUser(
            id = 0L,
            token = ""
        )
    }

    object StringArg : ReadWriteProperty<Bundle, String?> {

        override fun setValue(thisRef: Bundle, property: KProperty<*>, value: String?) {
            thisRef.putString(property.name, value)
        }

        override fun getValue(thisRef: Bundle, property: KProperty<*>): String? =
            thisRef.getString(property.name)
    }
}
