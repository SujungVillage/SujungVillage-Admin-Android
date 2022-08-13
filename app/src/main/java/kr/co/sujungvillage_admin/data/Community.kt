package kr.co.sujungvillage_admin.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CommDTO(
    @SerializedName("id")
    val id:Long,
    @SerializedName("title")
    val title:String,
    @SerializedName("writerDomitoryName")
    val dormitory:String,
    @SerializedName("regDate")
    val regDate:String
):Serializable{}

data class CommDetailCommentsWriteDTO(
    @SerializedName("postId")
    val postId:Long,
    @SerializedName("content")
    val content:String
):Serializable{}

data class CommDetailCommentWriteResultDTO(
    @SerializedName("id")
    val id :Long,
    @SerializedName("writerId")
    val writerId:String,
    @SerializedName("content")
    val content:String,
    @SerializedName("regDate")
    val regDate:String,
    @SerializedName("modDate")
    val modDate:String
):Serializable{}

data class CommDetailResultDTO(
    @SerializedName("id")
    val id:Long,
    @SerializedName("writerId")
    val writerId:String,
    @SerializedName("title")
    val title:String,
    @SerializedName("content")
    val content:String,
    @SerializedName("regDate")
    val regDate:String,
    @SerializedName("modDate")
    val modDate:String,
    @SerializedName("comments")
    val comments:MutableList<CommDetailCommentsRequest>
):Serializable{}

data class CommDetailCommentsRequest(
    @SerializedName("id")
    val id:Long,
    @SerializedName("writerId")
    val writerId:String,
    @SerializedName("content")
    val content:String,
    @SerializedName("regDate")
    val regDate:String,
    @SerializedName("modDate")
    val modDate:String
):Serializable{}

data class CommWriteDTO(
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String,
): Serializable {}


data class CommWriteResultDTO(
    @SerializedName("id")
    val id:Long,
    @SerializedName("writerId")
    val writer:String,
    @SerializedName("title")
    val title:String,
    @SerializedName("content")
    val content:String,
    @SerializedName("regDate")
    val reg_date:String,
    @SerializedName("modDate")
    val mod_date:String
):Serializable {}