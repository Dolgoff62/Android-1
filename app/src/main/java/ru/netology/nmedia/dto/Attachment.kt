package ru.netology.nmedia.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import ru.netology.nmedia.enum.AttachmentType

@Parcelize
data class Attachment(
    val url: String,
    val description: String,
    val type: AttachmentType,
) : Parcelable
