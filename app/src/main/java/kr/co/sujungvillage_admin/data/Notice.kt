package kr.co.sujungvillage_admin.data // ktlint-disable package-name

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class NoticeRequestResultDTO(
    @SerializedName("id")
    val id: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("dormitoryName")
    val dormitroy: String,
    @SerializedName("regDate")
    val date: String
) : Serializable

data class NoticeDetailResultDTO(
    @SerializedName("id")
    val id: Long,
    @SerializedName("writerId")
    val writerId: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("regDate")
    val regDate: String,
    @SerializedName("modDate")
    val modDate: String,
    @SerializedName("dormitoryName")
    val dormitory: String
) : Serializable

data class NoticeCreateDTO(
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("dormitoryName")
    val dormitory: String
) : Serializable

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
    @SerializedName("dormitoryName")
    val dormitory: String
) : Serializable
