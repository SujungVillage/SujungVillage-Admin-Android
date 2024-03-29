package kr.co.sujungvillage_admin.data // ktlint-disable package-name

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Alarm(
    @SerializedName("id")
    val id: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("isRead")
    val isRead: Boolean,
    @SerializedName("date")
    val date: String
) : Serializable
