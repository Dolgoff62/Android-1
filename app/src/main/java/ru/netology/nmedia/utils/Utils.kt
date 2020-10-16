package ru.netology.nmedia.utils

import kotlin.math.floor

class Utils {
    companion object {
        fun formatLikes(count: Int) : String {
            return when (count) {
                in 0..999 -> count.toString()
                in 1000..1099 -> String.format ("%d", count/1000) + "K"
                in 1100..9999 -> String.format ("%.1f", (floor(count/100.toDouble()))/10) + "K"
                in 10_000..999_999 -> String.format ("%d", count / 1000) + "K"
                in 1_000_000..999_999_999 ->
                    String.format ("%.1f", (floor(count/100_000.toDouble()))/10) + "M"
                else -> "> 1 Bn"
            }
        }
    }
}
