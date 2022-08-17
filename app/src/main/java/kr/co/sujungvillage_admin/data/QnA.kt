package kr.co.sujungvillage_admin.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

//QNA답변
data class QnaAnswerDTO(
    @SerializedName("questionId")
    val question: Long,
    @SerializedName("content")
    val content:String
):Serializable{}

data class QnaAnswerResultDTO(
    @SerializedName("id")
    val id:Long,
    @SerializedName("writerId")
    val writerId: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("regDate")
    val regDate: String,
    @SerializedName("modDate")
    val modDate: String
):Serializable{}

//FAQ 작성
data class FaqWriteRequestDTO(
    @SerializedName("question")
    val question:String,
    @SerializedName("answer")
    val answer:String,
    @SerializedName("dormitoryName")
    val dormitory:String
):Serializable{}

data class FaqWriteResultDTO(
    @SerializedName("id")
    val id:Long,
    @SerializedName("writerId")
    val writerId:String,
    @SerializedName("question")
    val question:String,
    @SerializedName("answer")
    val answer:String,
    @SerializedName("dormitoryName")
    val dormitory:String,
    @SerializedName("regDate")
    val regDate:String,
    @SerializedName("modDate")
    val modDate: String
):Serializable{}


data class FaqGetResultDTO(
    @SerializedName("id")
    val id: Long,
    @SerializedName("writerId")
    val userId: String,
    @SerializedName("question")
    val question: String,
    @SerializedName("answer")
    val answer: String,
    @SerializedName("regDate")
    val date: String,
    @SerializedName("modDate")
    val modDate: String,
): Serializable {}

data class MyqDetailGetResultDTO(
    @SerializedName("question")
    val question: QnAQuestion,
    @SerializedName("answer")
    val answer: QnAAnswer,
): Serializable {}

data class QnAQuestion(
    @SerializedName("id")
    val id: Long,
    @SerializedName("writerId")
    val userId: String,
    @SerializedName("writerName")
    val name: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("anonymous")
    val isAnonymous: Boolean,
    @SerializedName("reqDate")
    val reqDate: String,
    @SerializedName("modDate")
    val modDate: String,
): Serializable {}

data class QnAAnswer(
    @SerializedName("id")
    val id: Long,
    @SerializedName("writerName")
    val name: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("regDate")
    val regDate: String,
    @SerializedName("modDate")
    val modDate: String,
): Serializable {}

data class QuestionGetResultDTO(
    @SerializedName("questionId")
    val id: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("redDate")
    val date: String,
    @SerializedName("answered")
    val isAnswered: Boolean
): Serializable {}