package ru.netology.nmedia.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import ru.netology.nmedia.enum.AttachmentType

@Parcelize
data class Attachment(
    val url: String,
    val description: String,
<<<<<<< HEAD
    val type: AttachmentType
)
=======
    val type: AttachmentType,
) : Parcelable
>>>>>>> 74f650ad9a25f8190adc488187d899c354210929
