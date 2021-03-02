package ru.netology.nmedia.dto

import androidx.room.Embedded
import androidx.room.Entity
import ru.netology.nmedia.enum.AttachmentType

@Entity
data class Attachment(
    val url: String,
    @Embedded
    val type: AttachmentType
)
