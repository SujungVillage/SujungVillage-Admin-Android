package kr.co.sujungvillage_admin.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class NoticeRequestResultDTO(
    @SerializedName("id")
    val id: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("domitoryName")
    val dormitory: String,
    @SerializedName("regDate")
    val date: String,
): Serializable {}

data class NoticeDetailResultDTO(
    @SerializedName("id")
    val id: Long,
    @SerializedName("writerName")
    val name: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("domitoryName")
    val dormitory: String,
    @SerializedName("regDate")
    val regDate: String,
    @SerializedName("modDate")
    val modDate: String,
): Serializable {}

data class NoticeCreateDTO(
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("domitoryName")
    val dormitory: String,
): Serializable {}

data class NoticeCreateResultDTO(
    @SerializedName("id")
    val id: Long,
    @SerializedName("writerId")
    val writer: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("regDate")
    val regDate: String,
    @SerializedName("modDate")
    val modDate: String,
    @SerializedName("domitoryName")
    val dormitory: String,
): Serializable {}